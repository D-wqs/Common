package com.stamper.yx.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.entity.MHPkg;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.cache.EHCacheGlobal;
import com.stamper.yx.common.sys.cache.EHCacheUtil;
import com.stamper.yx.common.sys.response.ResultVO;
import com.stamper.yx.common.sys.security.AES.AesUtil;
import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.Line;

/**
 * @author zhf_10@163.com
 * @Description 设备响应回调控制层
 * @date 2019/8/13 0013 9:52
 */
@Slf4j
@RestController
@RequestMapping("/device/deviceCallBack")
public class CallBackController {

    @Autowired
    private SignetService signetService;
    @Autowired
    private DefaultWebSocketPool pool;

    /**
     * 设备响应第三方回调接口(设备专用)
     * http://114.214.170.66:9030/device/deviceCallBack/res?message=****&uuid=****
     */
    @RequestMapping("/res")
    public ResultVO res(String message, String uuid) {
        //TODO 指令回调和记录回调不走我，那我就不需要处理，这里做个样板
        log.info("获取到的密文message->{{}}, uuid->{{}}", message, uuid);
        //查询设备是否存在
        Signet signet = signetService.getByUUID(uuid);
        if (signet == null) {
            log.info("响应失败===>设备:{{}} 响应:{{}} 原因:uuid无对应设备", uuid, message);
            return ResultVO.FAIL("uuid无对应设备");
        }

        //将密文用aes对称密钥解密
        if (StringUtils.isNotBlank(message)) {
            try {
                //获取aeskey
                String aesKey = null;
//                log.info("【回调】数据：" + message);
                try {
                    String key = EHCacheGlobal.SIGNET_AESKEY + signet.getId();
                    Object o = EHCacheUtil.get(key);
                    if (o != null && StringUtils.isNotBlank(o.toString())) {
                        aesKey = o.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("【回调】从缓存中获取对称密钥失败");
                }
                if (aesKey == null) {
                    //从通道中获取
                    DeviceWebSocket deviceWebSocket = pool.get(signet.getId() + "");
                    if (deviceWebSocket != null) {
                        aesKey = deviceWebSocket.getSymmetricKey();//获取当前通道的对称密钥
                    }
                }
                message = AesUtil.decrypt(message, aesKey);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("【回调】接口异常");
            }
        }

        if (StringUtils.isNoneBlank(message, uuid)) {
            /**
             * 解析响应参数
             */
            MHPkg pkg = JSONObject.parseObject(message, MHPkg.class);
            if (pkg == null) {
                log.info("响应失败===>设备:{{}} 响应:{{}} 原因:响应体为空", signet.getId(), message);
                return ResultVO.FAIL("响应体为空");
            }

            /**
             * 走到这步,代表设备存在,并且响应参数也存在,并且解析成功,真正开始处理响应消息了
             */
            int cmd = pkg.getHead().getCmd();
            log.info("【回调cmd汇总】响应请求===>设备:{{}}，命令号{{}}， 响应:{{}}", signet.getId(), cmd, message);
            switch (cmd) {
                case AppConstant.FP_CLEAR_RES:
                    //指纹清空(删除)返回
//                    fpClearRes(message);
                    log.info("【回调】接收到的指纹清空的返回：{{}}", message);
                    break;
                case AppConstant.FP_RECORD_RES:
                    //指纹录入返回
//                    fingerPrintRes(signet, message);
                    log.info("【回调】接收到的指纹录入的返回：{{}}", message);
                    break;
                case AppConstant.APPLICATION_STATUS_RES:
                    //申请单推送的响应：
                    // message：{{"Body":{"Msg":"申请单推送成功！","Res":0},"Crc":"","Head":{"Magic":42949207,"Cmd":22,"SerialNum":0,"Version":1}}}
                    log.info("【回调】接收到的申请单推送成功的返回：{{}}", message);
//					deviceUnlockRes(signet, message);
                    break;
                case AppConstant.RECORD_NOTICE_RES:
                    //关闭wifi的返回 【网络状态的返回】：
                    //{"Body":{"SSID":"\"text\"","WifiPwd":"","uuid":"0X3A00255048500520333350","netType":2},"Head":{"Magic":42949207,"Cmd":75,"SerialNum":52,"Version":1}}
                    log.info("【回调】关闭wifi的返回：{{}}", message);
                    break;
                case AppConstant.DEVICE_USED_RES:
                    //设备使用中【开关锁】的返回：
                    log.info("【回调】设备使用中【开关锁】的返回：{{}}", message);
                    break;
                case AppConstant.DEVICE_UNLOCK_RES:
                    //ID解锁的返回：
                    log.info("=======!!!=={{}}===========");
                    log.info("【回调】ID解锁的返回：{{}}", message);
                    break;
                case AppConstant.USE_MODEL_RETURN_RES:
                    log.info("【回调】模式设置的返回：{{}}", message);
                    break;
                case AppConstant.SLEEP_TIME_RETURN_RES:
                    log.info("【回调】休眠设置的返回：{{}}", message);
                    break;
                case AppConstant.CURRENT_APPLICATION_CLEAR_RES:
                    log.info("【回调】申请单结束的返回（设备确认按钮按下也会被触发）：{{}}", message);
                    break;
                case AppConstant.REMOTE_LOCK_RETURN_RES:
                    log.info("【回调】锁定设置的返回:{{}}", message);
                    break;
                default:
                    log.info("【回调】未知协议请求-->{{}}", message);
                    break;
            }
        }
        return ResultVO.OK();
    }

//    /**
//     * 指纹录入后,设备回调响应调用的方法
//     */
//    public void fingerPrintRes(Signet signet, String message) {
//        if (signet != null) {
//            //解析消息体
//            FpRecordRes body = JSONObject.parseObject(message, FpRecordResPkg.class).getBody();
//
//            if (body != null) {
//                int res = body.getRes();
//                if (res == 0) {
//                    //录入成功,获取对应设备的指纹信息
//                    int userID = body.getUserID();
//                    int deviceId = body.getDeviceID();
//                    Finger finger = fingerService.getByUser(userID, deviceId);
//                    if (finger == null) {
//                        finger = new Finger();
//                        finger.setCreateDate(new Date());
//                    }
//                    finger.setDeviceId(deviceId);
//                    finger.setAddrNum(body.getFingerAddr());
//                    finger.setUserId(userID);
//                    finger.setUserName(body.getUserName());
//                    finger.setUpdateDate(new Date());
//                    finger.setCodeId(body.getCodeID());
//                    fingerService.add(finger);
//                }
//            }
//        }
//    }
//
//    /**
//     * 指纹删除(清空)后,设备回调响应调用的方法
//     */
//    public void fpClearRes(String message) {
//        //解析消息体
//        FingerPrintClearRes res = JSONObject.parseObject(message, FingerPrintClearResPkg.class).getBody();
//        if (res != null) {
//            int fingerAddr = res.getFingerAddr();
//            int deivceID = res.getDeviceID();
//            int userID = res.getUserID();//要删除的用印人ID
//            if (fingerAddr == 0) {
//                //全部清空
//                fingerService.deleteAllByDevice(deivceID);
//            } else {
//                //删除指定位置
//                fingerService.deleteByDeviceAndAddr(deivceID, fingerAddr, userID);
//            }
//        }
//    }


    //模块回调地址
    @RequestMapping("moduleCallback")
    public ResultVO moduleCallbackInfo(String deviceId,String event,String message){
        //获取aesKey
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        if(webSocket==null){
            log.info("【模块回调】异常：请检查当前设备{{}}的通道",deviceId);
            return ResultVO.FAIL();
        }
        log.info("模块回调deviceId：{}，事件类型：event：{} ， 消息：message：{}",deviceId,event,message);
        //获取aesKey
        String aesKey = webSocket.getSymmetricKey();
        try {
            String decrypt = AesUtil.decrypt(message, aesKey);
            log.info("【模块回调】设备：{}，事件类型：{},解密消息：{}",deviceId,event,decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVO.OK();
    }
}
