package com.stamper.yx.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.entity.DeviceMessage;
import com.stamper.yx.common.entity.MHPkg;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.deviceModel.*;
import com.stamper.yx.common.entity.mq.MQFinger;
import com.stamper.yx.common.entity.mq.MQPKG;
import com.stamper.yx.common.service.DeviceMessageService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.jwt.ApplicationToken;
import com.stamper.yx.common.sys.jwt.JwtUtil;
import com.stamper.yx.common.sys.mq.MqGlobal;
import com.stamper.yx.common.sys.mq.MqSender;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送指令到消息队列
 *
 * @author D-wqs
 * @data 2019/10/16 14:59
 */
@Slf4j
@RestController
@RequestMapping("/device/mq")
@ConditionalOnExpression("${openType.rabbit}")//关停监听
public class SignetMqSendController {
    @Autowired
    private SignetService service;
    @Autowired
    private DefaultWebSocketPool pool;
    @Autowired
    private MqSender mqSender;
    @Autowired
    private DeviceMessageService deviceMessageService;

    /**
     * 指纹录入
     *
     * @param deviceId   设备id
     * @param userId     用户id
     * @param fingerAddr 指纹地址
     * @param userName   用户名称，将显示在设备显示屏上
     * @return
     */
    @RequestMapping(value = "/finger/add", method = RequestMethod.POST)
    public ResultVO fingerPrint(@RequestParam("deviceId") Integer deviceId,
                                @RequestParam("userId") Integer userId,
                                @RequestParam("fingerAddr") Integer fingerAddr,
                                @RequestParam("userName") String userName) {
        if (deviceId == null || userId == null || fingerAddr == null || StringUtils.isBlank(userName)) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        //不允许录入指纹事，指纹地址为0
        if (fingerAddr.intValue() == 0) {
            return ResultVO.FAIL("指纹地址不能为0");
        }
        //校验设备是否存在以及是否在线
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        //设备是否被锁定
        //设备是否在使用中
        int status = webSocket.getStatus();
        if (status == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        //组包
        FingerPrintRecordReq req = new FingerPrintRecordReq();
        req.setUserID(userId);
        req.setDeviceID(deviceId);
        req.setUserName(userName);
        req.setCodeId(userId);
        req.setFingerAddr(fingerAddr);
        MHPkg res = MHPkg.res(AppConstant.FP_RECORD_REQ, req);
        //组包，发往队列中
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_FINGER_ADD);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setUserId(userId);

        mqpkg.setData(JSONObject.toJSONString(res));

        //发送--->
        mqSender.sendToExchange(MqGlobal.exchange_to_signet, mqpkg);

        return ResultVO.OK("指纹录入指令已下发");
    }

    /**
     * 指纹删除 根据用户的id和指纹地址删除设备（deviceId）的指纹信息
     *
     * @param userId
     * @param fingerAddr
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/finger/del", method = RequestMethod.POST)
    public ResultVO fingerDel(@RequestParam("userId") Integer userId,
                              @RequestParam("fingerAddr") Integer fingerAddr,
                              @RequestParam("deviceId") Integer deviceId) {
        if (userId == null || fingerAddr == null || deviceId == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
//        //删除指纹时，指纹地址不能为0，否则删除虽有响应，但是实际上没有删除
//        if (fingerAddr.intValue() == 0) {
//            return ResultVO.FAIL(Code.ERROR_PARAMETER);
//        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        //设备是否在使用中
        int status = webSocket.getStatus();
        if (status == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        //组包
        FingerPrintClearReq req = new FingerPrintClearReq();
        req.setDeviceID(deviceId);
        req.setUserID(userId);
        req.setFingerAddr(fingerAddr);
        MHPkg res = MHPkg.res(AppConstant.FP_CLEAR_REQ, req);
        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_FINGER_DEL);//指纹删除
        mqpkg.setUserId(userId);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));

        mqSender.sendToExchange(MqGlobal.exchange_to_signet, mqpkg);
        return ResultVO.OK("指纹删除指令已下发");
    }
    /**
     * 指纹清空：只传入设备id就行
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/finger/clean", method = RequestMethod.POST)
    public ResultVO fingerClean(@RequestParam("deviceId") Integer deviceId) {
        if (deviceId == null || deviceId.intValue() == 0) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        //设备是否在使用中
        int status = webSocket.getStatus();
        if (status == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        //组包
        FingerPrintClearReq req = new FingerPrintClearReq();
        req.setDeviceID(deviceId);
        req.setUserID(0);
        req.setFingerAddr(0);
        MHPkg res = MHPkg.res(AppConstant.FP_CLEAR_REQ, req);

        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_FINGER_CLEAN);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));

        mqSender.sendToExchange(MqGlobal.exchange_to_signet, mqpkg);
        return ResultVO.OK("指纹清空指令已下发");
    }

    /**
     * 模式设置
     * 打开或关闭设备指纹模式
     * 1：开启指纹模式
     * 0：关闭指纹模式--申请单模式
     *
     * @param status
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/changePattern", method = RequestMethod.POST)
    public ResultVO deviceChangePattern(@RequestParam("status") Integer status,
                                        @RequestParam("deviceId") Integer deviceId) {
        if (status == null || deviceId == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        //TODO 检查设备是否开锁?
        int open = webSocket.getStatus();
        if (open == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        //组包
        MHPkg res = MHPkg.res(AppConstant.USE_MODEL_REQ, new SignetModel(status));

        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_OPEN_OR_CLOSE_FINGER_PATTERN);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));

        mqSender.sendToExchange(MqGlobal.exchange_to_signet, mqpkg);
        return ResultVO.OK("模式切换指令已下发");
    }
    /**
     * 休眠设置【离线可用】
     * 范围：2-10分钟
     *
     * @param value
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/setSleepTimes", method = RequestMethod.POST)
    public ResultVO setSleepTimes(@RequestParam("value") Integer value,
                                  @RequestParam("deviceId") Integer deviceId) {
        if (value == null && deviceId == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Signet signet = service.get(deviceId);
        if (signet == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        if (signet.getStatus() != null && signet.getStatus().intValue() == 4) {
            return ResultVO.FAIL("设备已锁定，需解锁");
        }
        //组包
        DeviceSleepTime dd = new DeviceSleepTime();
        dd.setDeviceId(deviceId);
        dd.setSleepTime(value.intValue());
        MHPkg res = MHPkg.res(AppConstant.SLEEP_TIME_REQ, dd);

        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_SET_SLEEP_TIMES);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        if (webSocket != null && webSocket.isOpen()) {
            //通道存在并且设备在线，直接推送

            mqSender.sendToExchange(MqGlobal.exchange_to_signet, mqpkg);
            return ResultVO.OK("休眠设置指令已下发");
        } else {
            //设备不在线，存入离线，设备登陆后推送
            DeviceMessage deviceMessage = new DeviceMessage();
            deviceMessage.setBody(JSONObject.toJSONString(res));
            deviceMessage.setTitle("设置锁定模式");
            deviceMessage.setPushStatus(1);//标记未推送
            deviceMessage.setRecipientId(deviceId);//设备id
            deviceMessageService.addOrUpdate(deviceMessage);
            return ResultVO.OK("当前设备不在线，已缓存，待开机后会自动推送");
        }
    }

    /**
     * 锁定设置【离线可用】
     * 0：解锁
     * 1：锁定
     *
     * @param status
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/setRemoteLock", method = RequestMethod.POST)
    public ResultVO setRemoteLock(@RequestParam("status") Integer status,
                                  @RequestParam("deviceId") Integer deviceId) {
        if (status == null || deviceId == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Signet signet = service.get(deviceId);
        if (signet == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        //组包
        DeviceLock dl = new DeviceLock();
        dl.setDeviceId(deviceId);
        dl.setStatus(status);//0解锁，1锁定
        MHPkg res = MHPkg.res(AppConstant.REMOTE_LOCK_REQ, dl);
        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_REMOTE_LOCK);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        if (webSocket != null && webSocket.isOpen()) {
            //设备在线，发送
            mqSender.sendToExchange(MqGlobal.exchange_to_signet, mqpkg);
            return ResultVO.OK("模式切换指令已下发");
        } else {
            //设备不在线，存入离线，设备登陆后推送
            DeviceMessage deviceMessage = new DeviceMessage();
            deviceMessage.setBody(JSONObject.toJSONString(res));
            deviceMessage.setTitle("设置锁定模式");
            deviceMessage.setPushStatus(1);//标记未推送
            deviceMessage.setRecipientId(deviceId);//设备id
            deviceMessageService.addOrUpdate(deviceMessage);
            return ResultVO.OK("当前设备不在线，已缓存，待开机后会自动推送");
        }
    }

    /**
     * 获取WiFi列表
     *
     * @param userId
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getWifiList", method = RequestMethod.POST)
    public ResultVO getWifiList(@RequestParam("userId") Integer userId,
                                @RequestParam("deviceId") Integer deviceId) {
        if (userId == null || deviceId == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        //TODO 检查设备是否开锁?
        int open = webSocket.getStatus();
        if (open == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        //组包
        WifiListReq req = new WifiListReq();
        req.setDeviceID(deviceId);
        req.setUserID(userId);
        MHPkg res = MHPkg.res(AppConstant.WIFI_LIST_REQ, req);
        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_WIFI_LIST);
        mqpkg.setUserId(userId);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));
        mqSender.sendToExchange(MqGlobal.exchange_to_signet,mqpkg);
        return ResultVO.OK("获取wifi列表指令下发成功");
    }

    /**
     * 设置wifi
     *
     * @param userId
     * @param deviceId
     * @param ssid
     * @param wifiPwd
     * @return
     */
    @RequestMapping(value = "/setWifiLink", method = RequestMethod.POST)
    public ResultVO setWifiLink(@RequestParam("userId") Integer userId,
                                @RequestParam("deviceId") Integer deviceId,
                                @RequestParam("ssid") String ssid,
                                @RequestParam("wifiPwd") String wifiPwd) {
        if (userId == null || deviceId == null || StringUtils.isBlank(ssid) || StringUtils.isBlank(wifiPwd)) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        //TODO 检查设备是否开锁?
        int open = webSocket.getStatus();
        if (open == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        //组包
        DeviceSetWifiReq req = new DeviceSetWifiReq();
        req.setDeviceID(deviceId);
        req.setUserID(userId);
        req.setSsid(ssid);
        req.setWifiPwd(wifiPwd);

        MHPkg res = MHPkg.res(AppConstant.DEVICE_SET_WIFI_REQ, req);
        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_WIFI_LINK);
        mqpkg.setUserId(userId);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));
        mqSender.sendToExchange(MqGlobal.exchange_to_signet,mqpkg);
        return ResultVO.OK("连接wifi的指令已下发");
    }

    /**
     * 断开wifi
     *
     * @param userId
     * @param deviceId
     * @param ssid
     * @param wifiPwd
     * @return
     */
    @RequestMapping(value = "/closeWifiLink", method = RequestMethod.POST)
    public ResultVO closeWifiLink(@RequestParam("userId") Integer userId,
                                  @RequestParam("deviceId") Integer deviceId,
                                  @RequestParam("ssid") String ssid,
                                  @RequestParam("wifiPwd") String wifiPwd) {
        if (userId == null || deviceId == null || StringUtils.isBlank(ssid) || StringUtils.isBlank(wifiPwd)) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        //TODO 检查设备是否开锁?
        int open = webSocket.getStatus();
        if (open == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        DeviceSetWifiReq req = new DeviceSetWifiReq();
        req.setDeviceID(deviceId);
        req.setUserID(userId);
        req.setSsid(ssid);

        MHPkg res = MHPkg.res(AppConstant.RECORD_NOTICE_REQ, req);

        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_WIFI_CLOSE);
        mqpkg.setUserId(userId);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));
        mqSender.sendToExchange(MqGlobal.exchange_to_signet,mqpkg);
        return ResultVO.OK("断开wifi指令已下发");
    }

    /**
     * ID解锁
     *
     * @param userId
     * @param deviceId
     * @param userName
     * @return
     */
    @RequestMapping(value = "/unlockById", method = RequestMethod.POST)
    public ResultVO deviceUnlock(@RequestParam("userId") Integer userId,
                                 @RequestParam("deviceId") Integer deviceId,
                                 @RequestParam("userName") String userName) {
        if (userId == null || deviceId == null || StringUtils.isBlank(userName)) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        //TODO 检查设备是否开锁?
        int open = webSocket.getStatus();
        if (open == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        //组包
        CSDeviceUnlockReq req = new CSDeviceUnlockReq();
        req.setUserID(userId);
        req.setDeviceID(deviceId);
        req.setUserName(userName);
        MHPkg res = MHPkg.res(AppConstant.DEVICE_UNLOCK_REQ, req);
        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_UNLOCK);
        mqpkg.setUserId(userId);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));
        mqSender.sendToExchange(MqGlobal.exchange_to_signet,mqpkg);
        return ResultVO.OK("ID解锁指令已下发");
    }

    /**
     * 推送申请单
     *
     * @param applicationId 申请单id
     * @param title         申请单标题
     * @param totalCount    申请申请次数
     * @param needCount     申请单已用次数
     * @param deviceId      设备id
     * @param userName      申请人姓名
     * @param userId        申请人id
     * @return
     */
    @RequestMapping(value = "/pushApplication", method = RequestMethod.POST)
    public ResultVO pushApplication(@RequestParam("applicationId") Integer applicationId,
                                    @RequestParam("title") String title,
                                    @RequestParam("totalCount") Integer totalCount,
                                    @RequestParam("deviceId") Integer deviceId,
                                    @RequestParam("userName") String userName,
                                    @RequestParam("userId") Integer userId,
                                    @RequestParam(value = "needCount", required = false) Integer needCount) {
        if (applicationId == null || deviceId == null || userId == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        if (totalCount == null || totalCount.intValue() == 0) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        if (needCount == null) {
            needCount = 0;
        }
        Signet signet = service.get(deviceId);
        if (signet == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        if (signet.getStatus() != null && signet.getStatus().intValue() == 4) {
            return ResultVO.FAIL("设备已锁定，需解锁");
        }
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        if (webSocket == null) {
            return ResultVO.FAIL("设备不在线");
        }
        //设备是否在使用中
        int status = webSocket.getStatus();
        if (status == 1) {
            log.info("该设备{{}} 正在使用中,请关锁后推送", deviceId);
            return ResultVO.FAIL("该设备正在使用中,请关锁后推送");
        }
        //生成token
        ApplicationToken applicationToken = new ApplicationToken();
        applicationToken.setApplication_id(applicationId);
        applicationToken.setStatus(4);
        String token = JwtUtil.createJWT2(applicationToken);
        if (StringUtils.isNotBlank(token)) {
            /**生成请求实体*/
            ApplicationStatusReq req = new ApplicationStatusReq();
            req.setApplicationToken(token);
            req.setUseCount(totalCount - needCount);
            req.setStatus(4);
            req.setApplicationTitle(title);
            req.setApplicationID(applicationId);
            req.setUserName(userName);
            req.setUserID(userId);
            req.setTotalCount(totalCount);//申请单总次数，原来的useCount
            req.setNeedCount(needCount);
            MHPkg res = MHPkg.res(AppConstant.APPLICATION_STATUS_REQ, req);
            //发送给队列
            MQPKG mqpkg = new MQPKG();
            mqpkg.setCmd(MqGlobal.SIGNET_APPLICATION_PUSH);
            mqpkg.setUserId(userId);
            mqpkg.setDeviceId(deviceId);
            mqpkg.setData(JSONObject.toJSONString(res));
            mqSender.sendToExchange(MqGlobal.exchange_to_signet,mqpkg);
            return ResultVO.OK("申请单推送指令已下发");
        }
        return ResultVO.FAIL(Code.ERROR500);

    }

    /**
     * 结束申请单
     *
     * @param applicationId
     * @param deviceId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/endApplication", method = RequestMethod.POST)
    public ResultVO endApplication(@RequestParam("applicationId") Integer applicationId,
                                   @RequestParam("deviceId") Integer deviceId,
                                   @RequestParam("userId") Integer userId) {
        if (applicationId == null || deviceId == null || userId == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }

        //组包
        MHPkg res = MHPkg.end(applicationId);
        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_APPLICATION_END);
        mqpkg.setUserId(userId);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));
        mqSender.sendToExchange(MqGlobal.exchange_to_signet,mqpkg);
        return ResultVO.OK("结束申请单指令已下发");
    }

    /**
     * 设备清次
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ResultVO endApplication(@RequestParam("deviceId") Integer deviceId,
                                   @RequestParam("total") Integer total,
                                   @RequestParam("orgName") String orgName) {
        if (deviceId == null || total == null || orgName == null || orgName == "") {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Integer integer = deviceStatus(deviceId);
        switch (integer) {
            case 0:
                break;
            case 1:
                return ResultVO.FAIL("设备不存在");
            case 2:
                return ResultVO.FAIL("设备不在线");
            case 3:
                return ResultVO.FAIL("设备已锁定，需解锁");
            default:
                log.info("【检测设备状态：】设备：{{}} 检测异常 ", integer);
        }
        //组包
        DeviceInit di = new DeviceInit();
        di.setInitCount(total);
        di.setInitOrgName(orgName);
        MHPkg res = MHPkg.res(AppConstant.DEVICE_INIT_CLEAR_REQ, di);
        //发送给队列
        MQPKG mqpkg = new MQPKG();
        mqpkg.setCmd(MqGlobal.SIGNET_APPLICATION_END);
        mqpkg.setDeviceId(deviceId);
        mqpkg.setData(JSONObject.toJSONString(res));
        mqSender.sendToExchange(MqGlobal.exchange_to_signet,mqpkg);
        return ResultVO.OK("设备清次指令已下发");
    }

    /**
     * 返回：0 设备存在且在线
     * 返回：1 设备不存在
     * 返回：2 设备不在线
     *
     * @param deviceId
     * @return
     */
    public Integer deviceStatus(Integer deviceId) {
        Signet signet = service.get(deviceId);
        if (signet == null) {
            return 1;
        }
        boolean open = pool.isOpen(deviceId + "");
        if (open == false) {
            return 2;
        }
        if (signet.getStatus() != null && signet.getStatus().intValue() == 4) {
            log.info("设备当前为锁定状态，需解除锁定");
            return 3;
        }
        return 0;
    }

}
