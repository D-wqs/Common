package com.stamper.yx.common.controller;

import com.stamper.yx.common.service.DeviceWebSocketService;
import com.stamper.yx.common.sys.DateUtil;
import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/2 0002 19:13
 */
@Slf4j
@Component
@ServerEndpoint("/device/ws")
public class DeviceWebSocket extends DefaultWebSocket {

    public static DeviceWebSocketService service;

    private String name = null;//当前客户端名称
    private String uuid;//设备uuid
    private int status = 0;//当前客户端状态,初始化为'关锁状态'  0:关锁 1:开锁
    private String addr = null;//当前客户端地址
    private String network = null;//当前客户端网络

    @Override
    protected void receiveMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            if (heartbeat(message)) return;

            try {
                service.doWork(message, this);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("业务出错:{{}}---->设备key:{{}},开机时间:{{}},活跃时间:{{}},设备名称:{{}},设备uuid:{{}},接收消息:{{}}",
                        e.getMessage(),
                        getKey(),
                        DateUtil.format(getCreateDate()),
                        DateUtil.format(getUpdateDate()),
                        this.name,
                        this.uuid,
                        message);
            }
        }
    }

    /**
     * 心跳回应
     */
    private boolean heartbeat(String message) {
        if (message.startsWith("ping")) {
            if (isCaller()) {
                log.info("ping:您当前处于访客状态,请先注册/登录");
                return true;
            }

            if (message.startsWith("ping-null")) {
                log.info("ping:您的数据包格式不正确,请重新注册/登录->{}", message);
                return true;
            }

            if (getDeleteDate() != null) {
                log.info("ping:该通道已被关闭,请重新开启新通道");
                return true;
            }

            if (getKey() == null || pool.get(getKey().toString()) == null) {
                log.info("ping:该通道已被容器销毁,请重新开启新通道");
                return true;
            }

            send("pong");

            return true;
        }
        return false;
    }

    @Override
    public boolean isOpen() {
        //当前通道是否打开
        Session session = this.getSession();
        if(session!=null){
            return session.isOpen();
        }
        return false;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getNetwork() {
        return this.network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

}
