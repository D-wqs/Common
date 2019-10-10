package com.stamper.yx.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stamper.yx.common.entity.DeviceMessage;
import com.stamper.yx.common.entity.MHPkg;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.User;
import com.stamper.yx.common.entity.deviceModel.*;
import com.stamper.yx.common.service.DeviceMessageService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.service.UserService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.cache.EHCacheGlobal;
import com.stamper.yx.common.sys.cache.EHCacheUtil;
import com.stamper.yx.common.sys.jwt.AccessToken;
import com.stamper.yx.common.sys.jwt.ApplicationToken;
import com.stamper.yx.common.sys.jwt.JwtUtil;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/device")
public class SignetController {
    @Autowired
    private SignetService signetService;
    @Autowired
    private DefaultWebSocketPool pool;
    @Autowired
    private DeviceMessageService deviceMessageService;
    @Autowired
    private UserService userService;

    //    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
//    public ResultVO getAll(@RequestParam(required = false, defaultValue = "10") Integer pageSize,
//                           @RequestParam(required = false, defaultValue = "0") Integer pageNum) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<Signet> all = signetService.getAll();
//        if (all != null && all.size() > 0) {
//            PageInfo<Signet> page = new PageInfo<>(all);
//            return ResultVO.OK(page);
//        }
//        return ResultVO.FAIL(Code.ERROR500);
//    }

    /**
     * 设备是否在线
     *
     * @param deviceId
     * @return
     */
    @RequestMapping("/isOnline")
    public ResultVO deviceIsOnline(Integer deviceId) {
        if (deviceId == null || deviceId.intValue() == 0) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Signet signet = signetService.get(deviceId);
        if (signet == null) {
            return ResultVO.FAIL(Code.ERROR_DEVICE_NULL);
        }
        boolean open = pool.isOpen(signet.getId() + "");
        return ResultVO.OK(open);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResultVO getAll() {
        List<Signet> all = signetService.getAll();
        if (all != null && all.size() > 0) {
            return ResultVO.OK(all);
        }
        log.info("当前设备列表长度{{}}", all.size());
        return ResultVO.FAIL(Code.ERROR500);
    }

    @RequestMapping("/getAesKey")
    public ResultVO getAesKey(@RequestParam("deviceId") Integer deviceId) {
        //
        Signet byUUID = signetService.get(deviceId);
        if (byUUID == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        //TODO 设备不在线，获取缓存的key，设备在线后该值会更新

        String key = EHCacheGlobal.SIGNET_AESKEY + byUUID.getId();
        Object o = EHCacheUtil.get(key);
        if (o != null && StringUtils.isNotBlank(o.toString())) {
            return ResultVO.OK(o.toString());
        }
        return ResultVO.FAIL("印章不在线");
    }

    /**
     * 获取接口凭证：判断管理员账号的accessToken与传来的ticket是否一致
     *
     * @param ticket
     * @return
     */
    @RequestMapping(value = "/getAccessToken", method = RequestMethod.POST)
    public ResultVO getAccessToken(@RequestParam("ticket") String ticket) {
        if (StringUtils.isBlank(ticket)) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        //验证ticket是否存在
        User admin = userService.getUser(AppConstant.USER);
        if (admin == null) {
            return ResultVO.FAIL(Code.ERROR500);
        }
        String accesstoken = admin.getAccesstoken();
        if (!accesstoken.equalsIgnoreCase(ticket)) {
            //传来的ticket与管理员账户AccessToken不一致
            return ResultVO.FAIL("验证失败");
        }
        //todo 生成token 30分钟
        String yunxiAccesstoken = EHCacheGlobal.YUNXI_ACCESSTOKEN;
        AccessToken accessToken = new AccessToken();
        accessToken.setUuid(ticket);
        try {
            String jwt = JwtUtil.createJWT(accessToken, AppConstant.TOKEN_KEY, EHCacheGlobal.USER_TOKEN_TIMEOUT);

            EHCacheUtil.put(yunxiAccesstoken, jwt);
            return ResultVO.OK(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("初始化接口凭证异常e:{{}}", e.getMessage());
            return ResultVO.FAIL(e.getMessage());
        }
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
        webSocket.send(res);
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
        Signet signet = signetService.get(deviceId);
        if (signet == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        if(signet.getStatus()!=null&&signet.getStatus()==4){
            return ResultVO.FAIL("设备已锁定，需解锁");
        }
        //组包
        DeviceSleepTime dd = new DeviceSleepTime();
        dd.setDeviceId(deviceId);
        dd.setSleepTime(value.intValue());
        MHPkg res = MHPkg.res(AppConstant.SLEEP_TIME_REQ, dd);

        DeviceWebSocket webSocket = pool.get(deviceId + "");
        if (webSocket != null && webSocket.isOpen()) {
            //通道存在并且设备在线，直接推送
            webSocket.send(res);
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
        Signet signet = signetService.get(deviceId);
        if (signet == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        //组包
        DeviceLock dl = new DeviceLock();
        dl.setDeviceId(deviceId);
        dl.setStatus(status);//0解锁，1锁定
        MHPkg res = MHPkg.res(AppConstant.REMOTE_LOCK_REQ, dl);
        //获取通道对象，校验设备状态时，设备在线，说明通道不会为空
        DeviceWebSocket webSocket = pool.get(deviceId + "");
        if (webSocket != null && webSocket.isOpen()) {
            //设备在线，发送
            webSocket.send(res);
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

        //组包
        WifiListReq req = new WifiListReq();
        req.setDeviceID(deviceId);
        req.setUserID(userId);
        MHPkg res = MHPkg.res(AppConstant.WIFI_LIST_REQ, req);
        webSocket.send(res);
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
        //组包
        DeviceSetWifiReq req = new DeviceSetWifiReq();
        req.setDeviceID(deviceId);
        req.setUserID(userId);
        req.setSsid(ssid);
        req.setWifiPwd(wifiPwd);

        MHPkg mhPkg = MHPkg.res(AppConstant.DEVICE_SET_WIFI_REQ, req);
        webSocket.send(mhPkg);
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
        DeviceSetWifiReq req = new DeviceSetWifiReq();
        req.setDeviceID(deviceId);
        req.setUserID(userId);
        req.setSsid(ssid);

        MHPkg res = MHPkg.res(AppConstant.RECORD_NOTICE_REQ, req);
        webSocket.send(res);
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
        //组包
        CSDeviceUnlockReq req = new CSDeviceUnlockReq();
        req.setUserID(userId);
        req.setDeviceID(deviceId);
        req.setUserName(userName);
        MHPkg res = MHPkg.res(AppConstant.DEVICE_UNLOCK_REQ, req);
        webSocket.send(res);
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
        Signet signet = signetService.get(deviceId);
        if (signet == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        if(signet.getStatus()!=null&&signet.getStatus()==4){
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
//    applicationToken.setIs_qss(application.getIsQss());
        String token = JwtUtil.createJWT2(applicationToken);
        if (StringUtils.isNotBlank(token)) {
            /**生成请求实体*/
            ApplicationStatusReq req = new ApplicationStatusReq();
            req.setApplicationToken(token);
            req.setUseCount(totalCount - needCount);
//        req.setIsQss(application.getIsQss());
            req.setStatus(4);
            req.setApplicationTitle(title);
            req.setApplicationID(applicationId);
            req.setUserName(userName);
            req.setUserID(userId);
            req.setTotalCount(totalCount);//申请单总次数，原来的useCount
            req.setNeedCount(needCount);
            MHPkg res = MHPkg.res(AppConstant.APPLICATION_STATUS_REQ, req);
            pool.send(deviceId + "", res);
            //Future future = pool.send(application.getSignetId() + "", res);
            return ResultVO.OK("申请单推送指令已下发");
            //异步记录申请单
            //deviceAsyncService.pushApplication(future, req, pool.get(application.getSignetId() + ""));
            //return ResultVO.FUTRUE(future);
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
        pool.send(deviceId + "", res);
        return ResultVO.OK("结束申请单指令已下发");
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
        Signet signet = signetService.get(deviceId);
        if (signet == null) {
            return 1;
        }
        boolean open = pool.isOpen(deviceId + "");
        if (open == false) {
            return 2;
        }
        if (signet.getStatus() != null && signet.getStatus() == 4) {
            log.info("设备当前为锁定状态，需解除锁定");
            return 3;
        }
        return 0;
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
        pool.send(deviceId + "", res);
        return ResultVO.OK("设备清次指令已下发");
    }
}
