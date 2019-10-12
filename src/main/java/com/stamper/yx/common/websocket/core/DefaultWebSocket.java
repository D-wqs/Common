package com.stamper.yx.common.websocket.core;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.okhttpUtil.OkHttpCli;
import com.stamper.yx.common.sys.security.AES.AesUtil;
import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import com.stamper.yx.common.websocket.handle.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * @author zhf_10@163.com
 * @Description websocket默认实现抽象类
 * @date 2019/4/30 0030 16:03
 */
@Slf4j
@Component
public abstract class DefaultWebSocket implements BaseWebSocket {

    //与客户端的会话通道对象
    private Session session;

    //当前会话通道的创建时间
    private Date createDate;

    //当前会话通道最后一次接收消息的时间
    private Date updateDate;

    //当前会话通道关闭时间
    private Date deleteDate;

    //当前websocket对应的唯一标识符(当前项目中以设备印章ID作为标识)
    private Object key;

    //访客标记.只有当客户端证明自己身份(注册/登录)后,当前值设置为false
    private boolean isCaller = true;

    //可动态配置的参数,如果为true,则打印更详细的日志,如果为false:则不打印日志
    private boolean out = true;

    //对称密钥
    private String symmetricKey = null;

    public String getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(String symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

    //容器对象
    public static DefaultWebSocketPool pool;

    //通道打开时的触发过滤器
    public static OpenChannelFilter onOpenFilter;

    //通道关闭时的触发过滤器
    public static CloseChannelFilter onCloseFilter;

    //通道发生错误时的触发过滤器
    public static ChannelErrorFilter onErrorFilter;

    //接收到来自通道的消息时,触发的过滤器
    public static ReceiveMessageFilter onMessageFilter;

    //向通道发送消息时,触发的过滤器
    public static SendMessageFilter onSendFilter;

    //通道标识符改变时,触发的过滤器
    public static ChangeKeyFilter changeKeyFilter;

    /**
     * 通道打开时,需要初始化做的事情
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.createDate = new Date();
        this.updateDate = this.createDate;
        this.key = this.createDate.getTime();
        log.info("Websocket:one channel opened key->{{}} ", this.key);
        pool.add(this.key + "", this);
        if (onOpenFilter != null) {
            try {
                onOpenFilter.afterOpen(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通道关闭时,需要处理的后续操作
     */
    public static OkHttpCli okHttpCli;
    @OnClose
    public void onClose() {
        String key = this.key.toString();
        String aesKey = this.symmetricKey;//这个值是绑定在通道中的，新通道里没有，所以我需要在异常之后，通知对方该值已变更
        log.info("-=-=-=-=-=>设备通道触发onClose :{{}}", key);
//        this.deleteDate = new Date();
//        try {
//            this.session.close();
//            log.info("Websocket: 关闭->{{}}", this.key);
//        } catch (IOException e) {
//            log.info("Websocket:关闭出错 key->{{}} exception->{{}}", this.key, e.getMessage());
//        }
//
//        //从容器中删除该设备通道
//        pool.del(this.key + "");
//
//        if (onCloseFilter != null) {
//            try {
//                //触发关闭过滤器
//                onCloseFilter.afterClose(this);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        //todo 通知回调，设备通道已关闭,由于之前pool.del删除了这个key，所以进入方法前，获取这个key值保存
        Map<String,Object> map=new HashMap<>();
        map.put("deviceId",key);
        map.put("online",false);
        String message=null;
        try {
            message=AesUtil.encrypt(JSONObject.toJSONString(map),aesKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("设备关机回调{{}}",key);
        Map<String,String> requestBody=new HashMap<>();
        requestBody.put("message",message);
        requestBody.put("deviceId",key);
        requestBody.put("event",AppConstant.DEVICE_LOGOUT);
        String s = okHttpCli.doPost(AppConstant.MODULECALLBACK, requestBody);
        log.info("设备关机回调{{}}",s);
    }

    /**
     * 通道出错时,触发该方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("websocket:出错 key->{{}} error->{{}}", this.key, error.getMessage());
        if (onErrorFilter != null) {
            try {
                //触发通道出错过滤器
                onErrorFilter.afterError(this, error);
                error.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收消息时触发该方法
     * ps:判断过滤器是否存在,
     * 如果存在,则执行过滤器方法判断是否接收该消息
     * 如果不存在则执行实现类onMessage方法
     *
     * @param message 消息
     * @param session 通道实例对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        this.updateDate = new Date();
        if (out)
            log.info("Websocket:<<===== key:{{}} message:{{}}", this.key, message);
        boolean isAccess = true;
        if (onMessageFilter != null) {
            try {
                //触发消息过滤器,在子类处理之前执行
                isAccess = onMessageFilter.doFilter(message, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isAccess) {
            receiveMessage(message);
        }
    }

    //需要子类实现的方法,当接收到消息时,服务端需要做些什么事情?
    protected abstract void receiveMessage(String message);

    /**
     * 发送消息的方法
     */
    @Override
    public Future send(Object obj) {
        synchronized (this.session) {
            boolean access = true;
            ObjectMessage om = new ObjectMessage(obj);
            if (onSendFilter != null) {
                try {
                    //发送前,进行过滤器操作
                    access = onSendFilter.isAccess(om, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (access) {
                obj = om.getData();
                if (obj instanceof String) {
                    if (out)
                        log.info("WebSocket:=====>> key:{{}},context:{{}}", this.key, obj.toString());
                    return this.session.getAsyncRemote().sendText(obj.toString());
                } else {
                    String msg = JSONObject.toJSONString(obj);
                    if (out)
                        log.info("WebSocket:=====>> key:{{}},context:{{}}", this.key, msg);
                    return this.session.getAsyncRemote().sendText(msg);
                }
            } else {
                if (out)
                    log.info("WebSocket:=====>> key:{{}},message:{{}}", this.key, JSONObject.toJSONString(obj));
                return null;
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (this.session != null) {
            this.session.close();
            this.session = null;
        }
        this.deleteDate = new Date();
        log.info("Websocket:关闭 key->{{}}", this.key);
        pool.del(this.key + "");
        if (onCloseFilter != null) {
            try {
                onCloseFilter.afterClose(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isClosed() {
        if (this.session != null && this.deleteDate != null) {
            return true;
        }
        return false;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public Object getKey() {
        return key;
    }

    //确认了客户端身份后,调用此方法,将客户端key设置为指定值区别该客户端
    public void setKey(Object key) {
        pool.update(this.key + "", key + "");
        this.key = key;
        if (changeKeyFilter != null) {
            try {
                changeKeyFilter.doFilter(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultWebSocket)) return false;
        DefaultWebSocket that = (DefaultWebSocket) o;
        return isCaller == that.isCaller &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(deleteDate, that.deleteDate) &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createDate, deleteDate, key, isCaller);
    }

    public boolean isCaller() {
        return isCaller;
    }

    public void setCaller(boolean caller) {
        isCaller = caller;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public static DefaultWebSocketPool getPool() {
        return pool;
    }

    public static void setPool(DefaultWebSocketPool pool) {
        DefaultWebSocket.pool = pool;
    }

    public static OpenChannelFilter getOnOpenFilter() {
        return onOpenFilter;
    }

    public static void setOnOpenFilter(OpenChannelFilter onOpenFilter) {
        DefaultWebSocket.onOpenFilter = onOpenFilter;
    }

    public static CloseChannelFilter getOnCloseFilter() {
        return onCloseFilter;
    }

    public static void setOnCloseFilter(CloseChannelFilter onCloseFilter) {
        DefaultWebSocket.onCloseFilter = onCloseFilter;
    }

    public static ChannelErrorFilter getOnErrorFilter() {
        return onErrorFilter;
    }

    public static void setOnErrorFilter(ChannelErrorFilter onErrorFilter) {
        DefaultWebSocket.onErrorFilter = onErrorFilter;
    }

    public static ReceiveMessageFilter getOnMessageFilter() {
        return onMessageFilter;
    }

    public static void setOnMessageFilter(ReceiveMessageFilter onMessageFilter) {
        DefaultWebSocket.onMessageFilter = onMessageFilter;
    }

    public static SendMessageFilter getOnSendFilter() {
        return onSendFilter;
    }

    public static void setOnSendFilter(SendMessageFilter onSendFilter) {
        DefaultWebSocket.onSendFilter = onSendFilter;
    }

    public static ChangeKeyFilter getChangeKeyFilter() {
        return changeKeyFilter;
    }

    public static void setChangeKeyFilter(ChangeKeyFilter changeKeyFilter) {
        DefaultWebSocket.changeKeyFilter = changeKeyFilter;
    }
}
