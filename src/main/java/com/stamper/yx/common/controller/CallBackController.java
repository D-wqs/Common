package com.stamper.yx.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.entity.*;
import com.stamper.yx.common.entity.deviceModel.*;
import com.stamper.yx.common.service.AddInfoService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.service.mysql.MyApplicationService;
import com.stamper.yx.common.service.mysql.MysqlFingerService;
import com.stamper.yx.common.service.mysql.MysqlSignetService;
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
import java.util.Date;
import java.util.List;

/**
 * @author wqs
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
    @Autowired
    private MysqlFingerService mysqlFingerService;
    @Autowired
    private MyApplicationService myApplicationService;
    @Autowired
    private MysqlSignetService mysqlSignetService;
    @Autowired
    private AddInfoService addInfoService;

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
            log.error("这里是解析失败的message==>{}",message);
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
            //todo 校验mysql数据源
            boolean b = checkDatasource();
            switch (cmd) {
                case AppConstant.FP_CLEAR_RES:
                    //指纹清空(删除)返回
                    log.info("【回调】接收到的指纹清空的返回：{{}}", message);
                    if(b==true){
                        //数据源启用
                        fpClearRes(message);
                    }
                    break;
                case AppConstant.FP_RECORD_RES:
                    //指纹录入返回
                    log.info("【回调】接收到的指纹录入的返回：{{}}", message);
                    if(b==true){
                        //数据源启用
                        fingerPrintRes(signet, message);
                    }
                    break;
                case AppConstant.APPLICATION_STATUS_RES:
                    //申请单推送的响应：
                    // message：{{"Body":{"Msg":"申请单推送成功！","Res":0},"Crc":"","Head":{"Magic":42949207,"Cmd":22,"SerialNum":0,"Version":1}}}
                    log.info("【回调】接收到的申请单推送成功的返回：{{}}", message);
                    //TODO 推送成功，标记通道已收到申请单，不在处理发送申请单的指令
                    DeviceWebSocket dw = pool.get(signet.getId() + "");
                    if(dw!=null){
                        dw.setReceive(1);//设备已接收申请单，其他下发指令不能执行
                    }
//             deviceUnlockRes(signet, message);
                    break;
                case AppConstant.RECORD_NOTICE_RES:
                    //关闭wifi的返回 【网络状态的返回】：
                    //{"Body":{"SSID":"\"text\"","WifiPwd":"","uuid":"0X3A00255048500520333350","netType":2},"Head":{"Magic":42949207,"Cmd":75,"SerialNum":52,"Version":1}}
                    log.info("【回调】关闭wifi的返回：{{}}", message);
                    break;
                case AppConstant.DEVICE_USED_RES:
                    //设备使用中【开关锁】的返回：
                    log.info("【回调】设备使用中【开关锁】的返回：{{}}", message);
                    //todo 将通道置为0，可以接收申请单,否则仍会返回501
                    DeviceWebSocket closeButton = pool.get(signet.getId() + "");
                    if(closeButton!=null){
                        closeButton.setReceive(0);
                    }
                    // 处理申请单次数同步的问题
                    JSONObject jsonObject = JSON.parseObject(pkg.getBody().toString());
                    JSONArray loginApplicationInfo = jsonObject.getJSONArray("loginApplicationInfo");
                    LoginApplication loginApplication = (LoginApplication) JSONObject.parseObject(loginApplicationInfo.get(0).toString(), LoginApplication.class);
                    log.info(loginApplication.getApplicationId()+"<-applicationId:UseCount->"+loginApplication.getUseCount());
                    // 获取申请单的次数并更新
                    Applications byApplicationId = myApplicationService.getByApplicationId(loginApplication.getApplicationId());
                    if(byApplicationId!=null){
                        byApplicationId.setNeedCount(loginApplication.getUseCount());
                        // 更新申请单使用次数
                        myApplicationService.update(byApplicationId);
                    }

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
                    //todo 将通道置为0，可以接收申请单,否则仍会返回501
                    DeviceWebSocket revice_closeButton = pool.get(signet.getId() + "");
                    if(revice_closeButton!=null){
                        revice_closeButton.setReceive(0);
                    }
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

    /**
     * 指纹录入后,设备回调响应调用的方法
     */
    public void fingerPrintRes(Signet signet, String message) {
        if (signet != null) {
            //解析消息体
            FpRecordRes body = JSONObject.parseObject(message, FpRecordResPkg.class).getBody();

            if (body != null) {
                int res = body.getRes();
                if (res == 0) {
                    //录入成功,获取对应设备的指纹信息
                    int userId = body.getUserID();
                    int deviceId = body.getDeviceID();
                    int fingerAddr = body.getFingerAddr();
                    String userName = body.getUserName();
                    Finger finger = new Finger();
                    finger.setFingerAddr(fingerAddr);
                    finger.setUserId(userId);
                    finger.setUserName(userName);
                    finger.setDeviceId(deviceId);
                    try {
                        Finger finger1 = mysqlFingerService.getFinger(finger);
                        if(finger1==null){
                            mysqlFingerService.insert(finger);
                        }else{
                            finger.setUpdateDate(new Date());
                            finger.setId(finger1.getId());
                            mysqlFingerService.update(finger);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 指纹删除(清空)后,设备回调响应调用的方法
     */
    public void fpClearRes(String message) {
        //解析消息体
        FingerPrintClearRes res = JSONObject.parseObject(message, FingerPrintClearResPkg.class).getBody();
        if (res != null) {
            int fingerAddr = res.getFingerAddr();
            int deviceID = res.getDeviceID();
            int userID = res.getUserID();//要删除的用印人ID
            if (fingerAddr == 0) {
                //全部清空
                mysqlFingerService.delAllBydeviceId(deviceID);
            } else {
                //删除指定位置
                mysqlFingerService.delByFingerAddr(deviceID,fingerAddr,userID);
            }
        }
    }


    /**
     * 模块回调地址
     */
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
            switch (event){
                // 同步历史申请单信息
                case AppConstant.DEVICE_HISTORY_APPLICATION:
//                {"Body":[{"applicationId":19,"useCount":3}],"Head":{"Cmd":13,"Magic":-46510,"SerialNum":0,"Version":1}}
                    log.info("模块回调事件{{}}",event);
                    //todo 更新申请单的使用次数，
                    HistoryApplicationInfo historyApplicationInfo = null;
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(decrypt);
                        String body = jsonObject.getString("Body");
                        List<HistoryApplicationInfo> historyApplicationInfos = JSONArray.parseArray(body, HistoryApplicationInfo.class);
                        historyApplicationInfo = historyApplicationInfos.get(0);
                    } catch (Exception e) {
                        log.error("模块回调事件{{}}，json转换失败",event);
                        e.printStackTrace();
                    }
                    Integer applicationId = historyApplicationInfo.getApplicationId();
                    Integer useCount = historyApplicationInfo.getUseCount();
                    //更新申请单
                    Applications applications=new Applications();
                    applications.setApplicationId(applicationId);
                    applications.setNeedCount(useCount);
                    applications.setDeviceId(Integer.parseInt(deviceId));
                    log.info("【needCount】同步历史申请单信息:applicaionId:{},useCount：{}",historyApplicationInfo.getApplicationId(),historyApplicationInfo.getUseCount());
                    try {
                        myApplicationService.save(applications);
                    } catch (Exception e) {
                        log.error("【历史申请单使用次数同步】更新申请单失败");
                        e.printStackTrace();
                    }
                    break;
                //使用次数，盖章通知的返回
                case AppConstant.USE_COUNT:
                    //{\"Body\":{\"ApplicationID\":1001,\"DeviceID\":1001,\"Res\":0,\"UseTimes\":6},\"Head\":{\"Magic\":42949207,\"Cmd\":4,\"SerialNum\":1403,\"Version\":1}}
                    HighDeviceOnUseRes res = JSONObject.parseObject(decrypt, HighDeviceOnUsingPkg.class).getBody();
                    //通过盖章通知的返回，得到当前已使用的次数，更新needCount
                    Integer applicationID = res.getApplicationID();
                    Integer useTimes = res.getUseTimes();
                    Integer deviceId1 = res.getDeviceId();
//                    log.info("【拍照通知】申请单id{{}},当前设备使用次数{{}},设备id{{}}",applicationID,useTimes,deviceId1);
//                    Signet byId1 = signetService.getById(deviceId1);
//                    if(byId1!=null){
//                        byId1.setCount(useTimes);
//                        signetService.update(byId1);
//                        if ("true".equalsIgnoreCase(AppConstant.OPEN_MYSQL)) {
//                            mysqlSignetService.update(byId1);
//                        }
//                    }
                    Applications apps=new Applications();
                    apps.setApplicationId(applicationID);
                    apps.setNeedCount(useTimes);
                    apps.setDeviceId(deviceId1);
                    if(applicationID.intValue()==0){
                        log.info("applicationID==0,指纹模式不需要同步申请单次数,指纹模式不会触发此接口?待考证");
                        break;
                    }
                    //TODO 通知对应的申请单 已用次数needCount加1
                    Applications needCount_bak = myApplicationService.getByApplicationId(applicationID);
                    Integer needCount = needCount_bak.getNeedCount();
                    needCount_bak.setNeedCount(needCount+1);
                    log.info("【needCount】盖章通知的返回(通知申请单,已用次数+1):applicationId:{},申请单当前的needCount:{},+1后的值:{}",applicationID,needCount,needCount_bak.getNeedCount());
                    int update = myApplicationService.update(needCount_bak);
                    if(update!=1){
                        log.error("盖章通知的返回：通过申请单id（第三方业务id）：{{}}获取剩余次数，递减时保存失败,之前的已用次数needCouture{{}},应为{{}}",applicationID,needCount,needCount+1);
                    }
                    //同步当前设备的使用次数,因为P20设备使用次数一直为0,设备登陆时没有上传
                    break;
                case AppConstant.LOCATION_INFO:
                    /**
                     * 设备每次盖章用印(非审计)时,同步通知服务器
                     * {"addr":"安徽省合肥市蜀山区复兴路666号靠近合肥市梦园小学(西区)","deviceId":1002,"latitude":"31.816948","longitude":"117.138136"}
                     */
                    JSONObject jsonObject = JSONObject.parseObject(decrypt);
                    String addr = jsonObject.getString("addr");
                    int deviceId2 = jsonObject.getIntValue("deviceId");
                    Signet byId = signetService.getById(deviceId2);
                    if(byId!=null){
                        byId.setAddr(addr);
                        signetService.update(byId);
                        if ("true".equalsIgnoreCase(AppConstant.OPEN_MYSQL)) {
                            mysqlSignetService.update(byId);
                        }
                    }else {
                        log.error("【地址更新】设备不存在:{{}}",deviceId2);
                    }
                    //TODO 新增地址坐标信息
                    String latitude = jsonObject.getString("latitude");
                    String longitude = jsonObject.getString("longitude");
                    AddrInfo addrInfo = new AddrInfo();
                    addrInfo.setDeviceId(deviceId2);
                    addrInfo.setLongitude(longitude);
                    addrInfo.setLatitude(latitude);
                    addrInfo.setLocation(addr);
                    try {
                        addInfoService.insert(addrInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("设备坐标更新失败");
                    }
                    break;
                default:
                    log.info("【模块回调】未知事件请求-->{{}}", event);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVO.OK();
    }
    /**
     * 校验mysql数据源
     * @return
     */
    public boolean checkDatasource() {
        //mysql 数据源同步数据
        String openMysql = AppConstant.OPEN_MYSQL;
        if (openMysql.equalsIgnoreCase("false")) {
            mysqlFingerService = null;
            return false;
        }
        return true;
    }
}
