package com.stamper.yx.common.sys.mq;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.controller.DeviceWebSocket;
import com.stamper.yx.common.controller.FingerController;
import com.stamper.yx.common.controller.SignetController;
import com.stamper.yx.common.entity.mq.MQPKG;
import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * 消息队列-消费者
 */
@Component
@ConditionalOnExpression("${openType.rabbit}")//关停监听
public class MqReceiver {

    @Autowired
    private SignetController signetController;
    @Autowired
    private FingerController fingerController;
    @Autowired
    private DefaultWebSocketPool pool;


    /**
     * 监听消息
     * durable:消息队列是否持久化
     * autoDelete消息队列是否删除
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = MqGlobal.exchange_to_signet, durable = "false", type = ExchangeTypes.FANOUT),
            value = @Queue(value = "${queue.signet}", durable = "false", autoDelete = "true")))
    public void receiver(String mqpkgJson) {
        try {
            if (StringUtils.isNotBlank(mqpkgJson)) {
                MQPKG pkg = JSONObject.parseObject(mqpkgJson, MQPKG.class);
                if (pkg != null) {
                    //该设备是否在当前服务器
                    Integer deviceId = pkg.getDeviceId();
                    DeviceWebSocket socket = pool.get(deviceId + "");
                    if (socket == null) {
                        return;
                    }
                    int cmd = pkg.getCmd();
                    //TODO 线上项目移植，此处走同一个接口，分开执行的目的是为了后续改动做区分
                    switch (cmd) {
                        case MqGlobal.SIGNET_UPLOAD_LOG://0:通知设备端上传日志
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_APPLICATION_PUSH://1：申请单推送
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_APPLICATION_END:// 2：申请单结束
                            signetController._endApplication(pkg);
                            break;
                        case MqGlobal.SIGNET_FINGER_ADD:// 3：指纹录入
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_FINGER_DEL:// 4：指纹删除
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_FINGER_CLEAN:// 5：指纹清空
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_WIFI_LIST:// 6：wifi列表获取
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_WIFI_LINK:// 7：WiFi链接
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_WIFI_CLOSE:// 8：WiFi断开
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_UNLOCK:// 9：手动解锁
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SINGET_NOTICE_METER:// 10:通知高拍仪拍照
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_INIT:// 11:设备清次(初始化)
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_REMOTE_LOCK:// 12:远程锁定
                            signetController._setRemoteLock(pkg);
                            break;
                        case MqGlobal.SIGNET_SET_SLEEP_TIMES:// 13:设置休眠时间
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_OPEN_OR_CLOSE_FINGER_PATTERN:// 14:开启/关闭指纹模式
                            signetController.commandToDevice(pkg);
                            break;
                        case MqGlobal.SIGNET_MIGRATE:// 15:设备迁移
                            signetController.commandToDevice(pkg);
                            break;
                        default:
                            return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}