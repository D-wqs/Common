package com.stamper.yx.common.service;

import com.stamper.yx.common.controller.DeviceWebSocket;
import com.stamper.yx.common.entity.Applications;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.deviceModel.LoginApplication;
import com.stamper.yx.common.entity.deviceModel.WifiInfoRes;

import java.util.List;

/**
 * @author zhf_10@163.com
 * @Description 异步接口
 * @date 2019/5/2 0002 21:12
 */
public interface DeviceAsyncService {
	//推送离线申请单
	void pushUnline(Signet signet, DeviceWebSocket webSocket);

//	//指纹录入返回监控
//	void fingerPrintRes(Finger finger, DeviceWebSocket webSocket);
//
//	//指纹录入下发监控
//	void fingerPrintReq(FingerPrintRecordReq req, DeviceWebSocket webSocket);
//
//	//指纹清空返回监控
//	void fingerPrintEmptyRes(int deivceID, DeviceWebSocket webSocket);
//
//	//指纹清空下发监控
//	void fingerCleanReq(FingerPrintClearReq req, DeviceWebSocket webSocket);
//
//	//指纹删除返回监控
//	void fingerPrintDelRes(int deivceID, int fingerAddr, Integer userID, DeviceWebSocket webSocket);
//
//	//指纹删除下发监控
//	void fingerDelReq(FingerPrintClearReq req, DeviceWebSocket webSocket);

	//异步监控开关锁状态
//	void lock(int status, int userId, DeviceWebSocket webSocket);

	//异步监控网络状态改变
	void wifi(WifiInfoRes body, DeviceWebSocket webSocket);
//
//	//异步监控设备地址改变
//	void location(Addr addr, DeviceWebSocket webSocket);
//
//	//异步监控申请单推送
//	void pushApplication(Future future, ApplicationStatusReq req, DeviceWebSocket webSocket);
//
//	//异步监控申请单结束
//	void endApplication(Future future, ApplicationStatusReq req, DeviceWebSocket webSocket);
//
//	//下发指令,通知印章获取wifi列表
//	void getWifiList(Integer signetId, Integer userId);
//
//	//同步印章最近5次申请单记录,解决印章无网情况下次数同步问题
	void synchApplicationInfo(Signet signet, List<LoginApplication> loginApplicationInfo);

	//同步保存就到第二数据源
	void synchApplications(Applications applications);
}
