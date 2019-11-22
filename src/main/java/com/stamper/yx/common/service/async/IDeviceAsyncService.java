package com.stamper.yx.common.service.async;

import com.stamper.yx.common.controller.DeviceWebSocket;
import com.stamper.yx.common.entity.Applications;
import com.stamper.yx.common.entity.DeviceMessage;
import com.stamper.yx.common.entity.MHPkg;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.deviceModel.LoginApplication;
import com.stamper.yx.common.entity.deviceModel.WifiInfoRes;
import com.stamper.yx.common.service.DeviceAsyncService;
import com.stamper.yx.common.service.DeviceMessageService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.service.UserService;
import com.stamper.yx.common.service.mysql.MyApplicationService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.okhttpUtil.OkHttpCli;
import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/2 0002 21:12
 */
@Slf4j
@Service
public class IDeviceAsyncService implements DeviceAsyncService {

	@Autowired
	private DeviceMessageService deviceMessageService;
	@Autowired
	private DefaultWebSocketPool pool;
	@Autowired
	private OkHttpCli okHttpCli;
	@Autowired
	private UserService userService;
	@Autowired
	private SignetService signetService;
	@Autowired
	private MyApplicationService myApplicationService;

//	/**
//	 * 指纹清空下发监控
//	 *
//	 * @param req
//	 * @param webSocket
//	 */
//	@Override
//	@Async
//	public void fingerCleanReq(FingerPrintClearReq req, DeviceWebSocket webSocket) {
//		try {
//			if (req != null && webSocket != null) {
//				DetailFinger df = new DetailFinger();
//				df.setReqTime(new Date());
//				df.setStatus(2);
//				df.setType(2);
//				df.setUserName(req.getCodeName());
//				df.setUserId(req.getCodeID());
//				df.setFingerUserId(req.getFingerUserId());
//				df.setFingerName(req.getFingerUserName());
//				df.setFingerAddr(req.getFingerAddr());
//				df.setFingerId(req.getFingerId());
//				webSocket.setDetailFingers(df);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 指纹清空返回监控
//	 *
//	 * @param deivceID  设备ID
//	 * @param webSocket
//	 */
//	@Override
//	@Async
//	public void fingerPrintEmptyRes(int deivceID, DeviceWebSocket webSocket) {
//		try {
//			boolean bid = false;
//			if (webSocket != null) {
//				List<DetailFinger> detailFingers = webSocket.getDetailFingers();
//				if (detailFingers != null && detailFingers.size() > 0) {
//					for (int i = 0; i < detailFingers.size(); i++) {
//						DetailFinger df = detailFingers.get(i);
//						Integer _deviceId = df.getDeviceId();
//						Integer type = df.getType();
//						if (_deviceId != null && _deviceId.intValue() == deivceID && type == 2) {
//							//命中
//							df.setStatus(0);
//							df.setResTime(new Date());
//							df.setUpdateDate(new Date());
//							bid = true;
//						}
//					}
//				}
//
//				//没有命中,可能是之前延迟返回
//				if (!bid) {
//					DetailFinger df = new DetailFinger();
//					df.setStatus(0);
//					df.setResTime(new Date());
//					df.setDeviceId(deivceID);
//					df.setFingerAddr(0);
//					df.setCreateDate(new Date());
//					df.setUpdateDate(new Date());
//					df.setType(2);
//					webSocket.setDetailFingers(df);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
	/**
	 * 同步印章最近5次申请单记录,解决印章无网情况下次数同步问题
	 *
	 * @param signet           印章
	 * @param applicationInfos 申请单使用记录列表
	 */
	@Override
	@Async
	public void synchApplicationInfo(Signet signet, List<LoginApplication> applicationInfos) {
		//TODO 第三方需要自己处理使用记录同步问题：
		//组包:登陆请求，只发历史申请单数据
		MHPkg res = MHPkg.res(AppConstant.DEVICE_LOGIN_REQ, applicationInfos);
		okHttpCli.sendCallback(signet,AppConstant.DEVICE_HISTORY_APPLICATION,res);//历史申请单同步
	}

//	/**
//	 * 向印章下发指令,获取wifi列表
//	 *
//	 * @param signetId 印章ID
//	 * @param userId   发送指令用户id
//	 */
//	@Override
//	@Async
//	public void getWifiList(Integer signetId, Integer userId) {
//		WifiListReq req = new WifiListReq();
//		req.setDeviceID(signetId);
//		req.setUserID(userId);
//		MHPkg res = MHPkg.res(AppConstant.WIFI_LIST_REQ, req);
//		Future future = pool.send(signetId + "", res);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		log.info("用户ID:{} 印章ID:{} 获取wifi列表指令下发{}", userId, signetId, future.isDone() ? "成功" : "失败");
//	}
//
//	/**
//	 * 异步监控申请单结束
//	 *
//	 * @param future    指令下发结果
//	 * @param req       推送实例
//	 * @param webSocket 客户端
//	 */
//	@Override
//	@Async
//	public void endApplication(Future future, ApplicationStatusReq req, DeviceWebSocket webSocket) {
//		try {
//			if (future != null && req != null && webSocket != null) {
//				DetailApplication da = new DetailApplication();
//				da.setTime(new Date());
//				da.setApplicationId(req.getApplicationID());
//				da.setApplicationTitle(req.getApplicationTitle());
//				da.setType(1);
//				da.setOperatorId(req.getUserID());
//				da.setOperatorName(req.getUserName());
//				webSocket.setDetailApplications(future, da);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 异步监控申请单推送
//	 *
//	 * @param future    指令下发结果
//	 * @param req       推送实例
//	 * @param webSocket 客户端
//	 */
//	@Override
//	@Async
//	public void pushApplication(Future future, ApplicationStatusReq req, DeviceWebSocket webSocket) {
//		try {
//			if (future != null && req != null && webSocket != null) {
//				DetailApplication da = new DetailApplication();
//				da.setTime(new Date());
//				da.setApplicationId(req.getApplicationID());
//				da.setApplicationTitle(req.getApplicationTitle());
//				da.setIsQss(req.getIsQss());
//				da.setUseCount(req.getUseCount());
//				da.setType(0);
//				da.setOperatorId(req.getUserID());
//				da.setOperatorName(req.getUserName());
//				webSocket.setDetailApplications(future, da);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 异步监控设备地址改变
//	 */
//	@Override
//	@Async
//	public void location(Addr addr, DeviceWebSocket webSocket) {
//		try {
//			if (addr != null && webSocket != null) {
//				String location = addr.getLocation();
//				//查询当前地址是否之前已经存在
//				List<DetailAddr> detailAddrs = webSocket.getDetailAddrs();
//
//				if (detailAddrs.size() > 0) {
//					//查询地址列表最后一个是否与当前地址相同,相同则不添加
//					DetailAddr lastAddr = detailAddrs.get(detailAddrs.size() - 1);
//					if (lastAddr.getLocation().equalsIgnoreCase(location)) {
//						return;
//					}
//				}
//
//				//创建新地址轨迹对象
//				DetailAddr da = new DetailAddr();
//				da.setTime(new Date());
//				da.setDeviceId((Integer) webSocket.getKey());
//				da.setAddrId(addr.getId());
//				da.setLocation(location);
//				webSocket.setDetailAddrs(da);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 异步监控网络状态改变
	 *
	 * @param body
	 * @param webSocket
	 */
	@Override
	@Async
	public void wifi(WifiInfoRes body, DeviceWebSocket webSocket) {
		//TODO  网络状态的改变回调出去，我需要监控吗？
		if(body==null){
			log.info("网络状态改变时，参数为空");
			return;
		}
		int deviceId = body.getDeviceId();
		if(deviceId==0){
			log.info("参数有误deviceId{{}}",deviceId);
			return;
		}
		Signet signet = signetService.get(deviceId);
		if(signet==null){
			log.info("网络状态改变时，设备不存在{{}}",deviceId);
			return;
		}
		//组包
		MHPkg res = MHPkg.res(AppConstant.WIFI_INFO_RES,body);
		okHttpCli.sendCallback(signet,AppConstant.NET_STATUS,res);//网络状态返回
		//TODO websocket定时任务处理监控？我是否需要？
//		try {
//			if (body != null && webSocket != null) {
//				DetailNetwork dn = new DetailNetwork();
//				dn.setTime(new Date());
//				dn.setNetName(body.getSSID());
//				dn.setNetType(body.getNetType());
//				dn.setUuid(body.getUuid());
//				dn.setDeviceId(body.getDeviceId());
//				webSocket.setDetailNetworks(dn);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

//	/**
//	 * 异步监控开关锁状态
//	 *
//	 * @param status    0:关锁 1:开锁
//	 * @param userId    开锁人id
//	 * @param webSocket
//	 */
//	@Override
//	@Async
//	public void lock(int status, int userId, DeviceWebSocket webSocket) {
//		try {
//			DetailLock dl = new DetailLock();
//			dl.setTime(new Date());
//			dl.setStatus(0);
//			dl.setType(status);
//			if (userId != 0) {
//				dl.setUserId(userId);
//				//查询指纹所属用户信息
//				User user = userClient.getById(userId);
//				if (user != null && user.getId() != null) {
//					dl.setUserName(user.getUserName());
//				}
//			}
//
//			webSocket.setDetailLocks(dl);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	/**
//	 * 指纹删除下发监控
//	 *
//	 * @param req       下发指令包实体
//	 * @param webSocket 客户端通道
//	 */
//	@Override
//	@Async
//	public void fingerDelReq(FingerPrintClearReq req, DeviceWebSocket webSocket) {
//		try {
//			if (req != null && webSocket != null) {
//				DetailFinger df = new DetailFinger();
//				df.setReqTime(new Date());
//				df.setStatus(2);
//				df.setType(1);
//				df.setUserName(req.getCodeName());
//				df.setUserId(req.getCodeID());
//				df.setFingerUserId(req.getFingerUserId());
//				df.setFingerName(req.getFingerUserName());
//				df.setFingerAddr(req.getFingerAddr());
//				df.setFingerId(req.getFingerId());
//				webSocket.setDetailFingers(df);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 指纹删除返回监控
//	 *
//	 * @param deivceID
//	 * @param fingerAddr
//	 * @param webSocket
//	 */
//	@Override
//	@Async
//	public void fingerPrintDelRes(int deivceID, int fingerAddr, Integer userID, DeviceWebSocket webSocket) {
//		try {
//			boolean bid = false;
//			List<DetailFinger> detailFingers = webSocket.getDetailFingers();
//			if (detailFingers.size() > 0) {
//				for (int i = 0; i < detailFingers.size(); i++) {
//					DetailFinger df = detailFingers.get(i);
//					Integer _deviceId = df.getDeviceId();
//					Integer type = df.getType();
//					Integer _fingerAddr = df.getFingerAddr();
//					if (_deviceId != null && _fingerAddr != null && _deviceId.intValue() == deivceID && type == 1 && _fingerAddr.intValue() == fingerAddr) {
//						//命中
//						df.setStatus(0);
//						df.setResTime(new Date());
//						df.setUpdateDate(new Date());
//						df.setUserId(userID);
//						bid = true;
//					}
//				}
//			}
//
//			//没有命中,可能是之前延迟返回
//			if (!bid) {
//				DetailFinger df = new DetailFinger();
//				df.setStatus(0);
//				df.setResTime(new Date());
//				df.setDeviceId(deivceID);
//				df.setFingerAddr(fingerAddr);
//				df.setCreateDate(new Date());
//				df.setUpdateDate(new Date());
//				df.setFingerUserId(userID);
//				df.setType(1);
//				webSocket.setDetailFingers(df);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 指纹录入下发监控
//	 *
//	 * @param req       下发消息体
//	 * @param webSocket 客户端通道
//	 */
//	@Override
//	@Async
//	public void fingerPrintReq(FingerPrintRecordReq req, DeviceWebSocket webSocket) {
//		try {
//			DetailFinger df = new DetailFinger();
//			df.setReqTime(new Date());
//			df.setUserId(req.getCodeId());
//			df.setUserName(req.getCodeName());
//			df.setDeviceId(req.getDeviceID());
//			df.setFingerAddr(req.getFingerAddr());
//			df.setFingerName(req.getUserName());
//			df.setFingerUserId(req.getUserID());
//			df.setType(0);
//			df.setStatus(2);
//			webSocket.setDetailFingers(df);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * 指纹录入返回监控
//	 *
//	 * @param finger    存储的实例
//	 * @param webSocket 客户端实例
//	 */
//	@Override
//	@Async
//	public void fingerPrintRes(Finger finger, DeviceWebSocket webSocket) {
//		try {
//			//监控该记录
//			List<DetailFinger> detailFingers = webSocket.getDetailFingers();
//			boolean bid = false;
//			if (detailFingers.size() > 0) {
//				for (int i = 0; i < detailFingers.size(); i++) {
//					DetailFinger df = detailFingers.get(i);
//					String fingerName = df.getFingerName();//指纹所属人名称
//					Integer fingerAddr = df.getFingerAddr();//指纹存储地址
//					Integer fingerUserId = df.getFingerUserId();//指纹所属人ID
//					if (fingerName != null
//							&& fingerName.equalsIgnoreCase(finger.getUserName())
//							&& fingerAddr == finger.getAddrNum()
//							&& fingerUserId == finger.getUserId()) {
//						//命中
//						df.setResTime(new Date());
//						df.setStatus(0);
//						df.setUpdateDate(new Date());
//						bid = true;
//					}
//				}
//			}
//
//			//之前没有命中,该指纹记录可能是以前通道延迟发回的
//			if (!bid) {
//				DetailFinger df = new DetailFinger();
//				df.setStatus(0);
//				df.setResTime(new Date());
//				df.setDeviceId(finger.getDeviceId());
//				df.setFingerId(finger.getId());
//				df.setFingerAddr(finger.getAddrNum());
//				df.setFingerName(finger.getUserName());
//				df.setFingerUserId(finger.getUserId());
//				df.setCreateDate(new Date());
//				df.setUpdateDate(new Date());
//				df.setType(0);
//				df.setUserId(finger.getCodeId());
//				webSocket.setDetailFingers(df);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 异步推送离线消息
	 */
	@Override
	@Async
	public void pushUnline(Signet signet, DeviceWebSocket socket) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (signet != null && socket != null && !socket.isCaller() && socket.isOpen()) {
			//TODO:推送离线消息
			List<DeviceMessage> deviceMessages = deviceMessageService.getBySignet(signet.getId());
			if (deviceMessages != null && deviceMessages.size() > 0) {
				for (int i = 0; i < deviceMessages.size(); i++) {
					DeviceMessage dm = deviceMessages.get(i);
					if (dm != null) {
						if (StringUtils.isBlank(dm.getBody())) {
							dm.setPushStatus(0);
							deviceMessageService.update(dm);
						} else if (socket != null && socket.isOpen()) {
							Future future = socket.send(dm.getBody());
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (future != null && future.isDone()) {
								//指令下发成功
								dm.setPushStatus(0);
								deviceMessageService.update(dm);
								log.info("成功===>离线消息{}:设备{} 消息标题:{} 消息内容:{}", i + 1, dm.getRecipientId(), dm.getTitle(), dm.getBody());
							} else {
								//TODO:指令下发失败,记录次数,更新数据库记录,超过指定次数不发送了
								log.info("失败===>离线消息{}:设备{} 消息标题:{} 消息内容:{}", i + 1, dm.getRecipientId(), dm.getTitle(), dm.getBody());
							}
						} else {
							log.info("设备离线/消息体不存在===>离线消息{}:设备{} 消息标题:{} 消息内容:{}", i + 1, dm.getRecipientId(), dm.getTitle(), dm.getBody());
						}
					} else {

					}
				}
				return;
			}
			log.info("离线消息不存在===>设备:{}", signet.getId());
			return;
		}
		try {
			log.info("推送离线消息异常===>设备:{{}} isCaller:{{}} isOpen:{{}}", signet.getId(), socket.isCaller(), socket.isOpen());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Async
	@Override
	public void synchApplications(Applications applications) {
		if(applications!=null){
			try {
				myApplicationService.save(applications);
			} catch (Exception e) {
				log.error("同步保存申请单到第二数据源失败：{{}}",applications.toString());
				e.printStackTrace();
			}
		}
	}
}
