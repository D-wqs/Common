package com.stamper.yx.common.controller;

import com.stamper.yx.common.entity.MHPkg;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.deviceModel.FingerPrintClearReq;
import com.stamper.yx.common.entity.deviceModel.FingerPrintRecordReq;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.service.mysql.MysqlFingerService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 指纹录入
 * 指纹删除
 * 指纹清空
 */
@RestController
@Slf4j
@RequestMapping("/device/finger")
public class FingerController {
    @Autowired
    private SignetService service;
    @Autowired
    private DefaultWebSocketPool pool;
    @Autowired
    private MysqlFingerService mysqlFingerService;

    /**
     * 指纹录入
     *
     * @param deviceId   设备id
     * @param userId     用户id
     * @param fingerAddr 指纹地址
     * @param userName   用户名称，将显示在设备显示屏上
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultVO fingerPrint(@RequestParam("deviceId") Integer deviceId,
                                @RequestParam("userId") Integer userId,
                                @RequestParam("fingerAddr") Integer fingerAddr,
                                @RequestParam("userName") String userName) {
        if (deviceId == null || userId == null || fingerAddr == null || StringUtils.isBlank(userName)) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        boolean b = checkDatasource();
        //不允许录入指纹事，指纹地址为0
        if (fingerAddr.intValue() == 0) {
            if(b==false){
                //数据源
                return ResultVO.FAIL("指纹地址不能为0");
            }else{
                //todo 使用mysql数据源，当传入的指纹地址为0，程序计算指纹地址
                fingerAddr=mysqlFingerService.getFineFingerAddr(deviceId);
            }
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
        webSocket.send(res);
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
    @RequestMapping(value = "/del", method = RequestMethod.POST)
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
        webSocket.send(res);
        return ResultVO.OK("指纹删除指令已下发");
    }

    /**
     * 指纹清空：只传入设备id就行
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/clean", method = RequestMethod.POST)
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
        webSocket.send(res);
        return ResultVO.OK("指纹清空指令已下发");
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
