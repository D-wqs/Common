package com.stamper.yx.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.controller.DeviceWebSocket;
import com.stamper.yx.common.entity.MHPkg;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.deviceModel.*;
import com.stamper.yx.common.service.DeviceAsyncService;
import com.stamper.yx.common.service.DeviceWebSocketService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.service.mysql.MysqlSignetService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.cache.EHCacheGlobal;
import com.stamper.yx.common.sys.cache.EHCacheUtil;
import com.stamper.yx.common.sys.jwt.DeviceToken;
import com.stamper.yx.common.sys.jwt.JwtUtil;
import com.stamper.yx.common.sys.okhttpUtil.OkHttpCli;
import com.stamper.yx.common.sys.security.AES.AesUtil;
import com.stamper.yx.common.sys.security.rsa.KeyFactory;
import com.stamper.yx.common.sys.security.rsa.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/2 0002 19:33
 */
@Slf4j
@Service
public class IDeviceWebSocketService implements DeviceWebSocketService {

    @Autowired
    private SignetService signetService;
    @Autowired
    private DeviceAsyncService deviceAsyncService;
    @Autowired
    private OkHttpCli okHttpCli;
    @Autowired
    private MysqlSignetService mysqlSignetService;

    /**
     * 接待websocket发送过来的请求
     * ps:上一层做了过滤,次数message一定不会空
     *
     * @param message
     * @param webSocket
     */
    @Override
    public void doWork(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        //TODO 通道获取密钥
        String symmetricKey = webSocket.getSymmetricKey();
        //TODO 数据传输过来的格式为json格式，apk版本号最低为3.338
        // 其中 {"message":"***","cmd":"0"}：0代表注册数据  1代表登陆以及其他指令数据
        message = parseEncrypt(message, webSocket);

        if (webSocket.isCaller()) {
            //如果是访客,代表改通道未注册
            //非对称解密
            try {
                byte[] bytes = RsaUtil.decryptByPrivateKeyForSpilt(new BASE64Decoder().decodeBuffer(message), KeyFactory.getPrivateKey());
                message = new String(bytes);
                log.info("非对称解密成功");
            } catch (Exception e) {
                log.error("设备注册：非对称解密异常", e.getMessage());
                e.printStackTrace();
                return;
            }

        } else {
            //对称解密
            try {
                synchronized (this.getClass()) {
                    log.info("对称密钥:" + symmetricKey);
                    log.info("对称密文：" + message);
                    message = AesUtil.decrypt(message, symmetricKey);
                    log.info("对称解密成功:" + message);
                }
            } catch (Exception e) {
                log.error("设备通信：对称解密异常", e.getMessage());
                //让设备去注册

                e.printStackTrace();
                return;
            }
        }
        //对传递过来的head+body+crc做解密
        int cmd = analysisCMD(message);
        switch (cmd) {
            case AppConstant.DEVICE_REG_REQ: //注册请求【数据源】
                deviceRegReq(message, webSocket);
                break;
            case AppConstant.DEVICE_LOGIN_REQ: //登录请求【数据源】
                deviceLoginReq(message, webSocket);
                break;
            case AppConstant.WIFI_INFO_RES:    //网络状态改变返回【数据源】
                getWifiInfoRes(message, webSocket);
                break;
            case AppConstant.DEVICE_USED_RES:    //印章开关锁状态的返回/使用中的返回：使用中不能推送申请单
                deviceUsingRes(message, webSocket);
                break;
            case AppConstant.IS_AUDIT_REQ:    //设备上传地址坐标信息
                updatePosition(message, webSocket);
                break;
            case AppConstant.WIFI_LIST_RES:    //wifi列表返回结果
                getWifiListRes(message, webSocket);
                break;
            case AppConstant.TAKE_PIC_RES:    //印章通知高拍仪进行拍照【没盖一次通知申请单次数减一】
                highDeviceOnUsing(message, webSocket);
                break;
            case AppConstant.USE_MODEL_RETURN_RES:    //开启/关闭指纹模式 状态返回【数据源】
                updateSignetModel(message, webSocket);
                break;
            case AppConstant.REMOTE_LOCK_RETURN_RES:    //远程锁定功能 状态返回【数据源】
                updateRemoteLock(message, webSocket);
                break;
            case AppConstant.SLEEP_TIME_RETURN_RES:    //设置休眠 状态返回【数据源】
                updateSleepTime(message, webSocket);
                break;
            default:
                log.info("未知协议请求-->{{}}", message);
                break;
        }
    }

    /**
     * 设置休眠 状态返回
     * {"Body":{"res":0,"sleepTime":4},"Head":{"Magic":42949207,"Cmd":83,"SerialNum":980,"Version":1}}
     */
    private void updateSleepTime(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        DeviceSleepTime sleepTime = JSONObject.parseObject(message, DeviceSleepTimePkg.class).getBody();

        if (sleepTime != null && sleepTime.getRes() != null && sleepTime.getRes().intValue() == 0) {
            Integer times = sleepTime.getSleepTime();//休眠时间 2~10 分钟

            String uuid = webSocket.getUuid();
            if (StringUtils.isNotBlank(uuid)) {
                Signet signet = signetService.getByUUID(uuid);
                if (signet != null) {
                    signet.setSleepTime(times);
                    signetService.update(signet);
                    //todo 存入mysql
                    String openMysql = AppConstant.OPEN_MYSQL;
                    if (StringUtils.isBlank(openMysql) || openMysql.equalsIgnoreCase("false")) {
                        mysqlSignetService = null;
                    }
                    if (mysqlSignetService != null) {
                        mysqlSignetService.add(signet);
                    }
                    log.info("设备:{{}} 设置休眠:{{}}分钟", signet.getName(), times);
                }
            }
        }
    }

    /**
     * 远程锁定功能 状态返回
     * {"Body":{"res":0,"status":1},"Head":{"Magic":42949207,"Cmd":91,"SerialNum":901,"Version":1}}
     */
    private void updateRemoteLock(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        DeviceLock lock = JSONObject.parseObject(message, DeviceLockPkg.class).getBody();

        if (lock != null && lock.getRes() != null && lock.getRes().intValue() == 0) {
            Integer status = lock.getStatus();//0:解锁  1:锁定

            String uuid = webSocket.getUuid();
            if (StringUtils.isNotBlank(uuid)) {
                Signet signet = signetService.getByUUID(uuid);
                if (signet != null && status != null) {
                    signet.setStatus(status.intValue() == 1 ? 4 : 0);//0:正常 1:异常 2:销毁 3:停用 4:锁定
                    signetService.update(signet);
                    //todo 存入mysql
                    String openMysql = AppConstant.OPEN_MYSQL;
                    if (StringUtils.isBlank(openMysql) || openMysql.equalsIgnoreCase("false")) {
                        mysqlSignetService = null;
                    }
                    if (mysqlSignetService != null) {
                        mysqlSignetService.add(signet);
                    }
                    log.info("设备:{{}} 远程锁定:{{}}", signet.getName(), status.intValue() == 2 ? "锁定" : "解锁");
                }
            }
        }
    }

    /**
     * 印章使用模式状态返回
     * {"Body":{"res":0,"useModel":1},"Head":{"Magic":42949207,"Cmd":87,"SerialNum":672,"Version":1}}
     */
    private void updateSignetModel(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        DeviceModel model = JSONObject.parseObject(message, DeviceModelPkg.class).getBody();

        if (model != null && model.getRes() != null && model.getRes().intValue() == 0) {
            Integer status = model.getUseModel();//0：关闭指纹模式 1：打开指纹模式

            String uuid = webSocket.getUuid();
            if (StringUtils.isNotBlank(uuid)) {
                Signet signet = signetService.getByUUID(uuid);
                if (signet != null && status != null) {
                    signet.setFingerPattern(status.intValue() == 1);//0：不开启 1：开启
                    signetService.update(signet);
                    //todo 存入mysql
                    String openMysql = AppConstant.OPEN_MYSQL;
                    if (StringUtils.isBlank(openMysql) || openMysql.equalsIgnoreCase("false")) {
                        mysqlSignetService = null;
                    }
                    if (mysqlSignetService != null) {
                        mysqlSignetService.add(signet);
                    }
                    log.info("设备:{{}} 使用模式:{{}}", signet.getName(), signet.getFingerPattern() ? "指纹模式(开)" : "指纹模式(关)");
                }
            }
        }

    }

    /**
     * 设备上传地址坐标信息
     */
    private void updatePosition(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        //解析消息体
        LocationRes res = JSONObject.parseObject(message, LocationPkg.class).getBody();
        String uuid = webSocket.getUuid();
        if (StringUtils.isBlank(uuid)) {
            log.info("从通道中获取设备信息失败");
            return;
        }
        Signet byUUID = signetService.getByUUID(uuid);
        if (byUUID == null) {
            log.error("当前设备不存在{{}}", uuid);
        }
        log.info("【坐标地址更新通知】模块回调设备地址信息");
        okHttpCli.sendCallback(byUUID, AppConstant.LOCATION_INFO, res);//同步地址信息
//		//坐标与具体地址不为空的情况,才存储并更新设备信息
//		if (res != null && StringUtils.isNoneBlank(res.getLongitude(), res.getLatitude(), res.getAddr())) {
//
//			//查询或保存坐标
//			Addr addr = addrService.getByLocation(res.getAddr());
//			if (addr == null) {
//				addr = new Addr();
//				addr.setCreateDate(new Date());
//				addr.setLatitude(res.getLatitude());
//				addr.setLocation(res.getAddr());
//				addr.setLongitude(res.getLongitude());
//				//工具类解析省市区
//				Location location = LocationUtil.addressResolution(res.getAddr());
//				if (location != null) {
//					addr.setProvince(location.getProvince());
//					addr.setCity(location.getCity());
//					addr.setDistrict(location.getDistrict());
//					addr.setStreet(location.getStreet());
//				}
//				addrService.add(addr);
//			}
//
//			//更新设备地址
//			webSocket.setAddr(addr.getLocation());
//
//			//更新设备地址坐标信息
//			Signet signet = null;
//			try {
//				signet = signetService.get((Integer) webSocket.getKey());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if (signet != null) {
//				Integer signetAddr = signet.getAddr();
//				if (signetAddr == null || signetAddr.intValue() != addr.getId().intValue()) {
//					signet.setAddr(addr.getId());
//					signetService.update(signet);
//					log.info("地址同步:设备id{{}} 设备:{{}} 地址:{{}}", webSocket.getKey(), webSocket.getName(), addr.getLocation());
//
//					//记录地址变更轨迹
//					deviceAsyncService.location(addr, webSocket);
//				}
//			}
//		}
    }

    /**
     * 印章通知高拍仪进行拍照
     * 拍照通知:没盖一次就同步一次次数
     */
    private void highDeviceOnUsing(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        webSocket.setStatus(1);
        HighDeviceOnUsingPkg res = null;
        try {
            res = JSONObject.parseObject(message, HighDeviceOnUsingPkg.class);
        } catch (Exception e) {
            log.error("盖章拍照的返回解析json异常：{{}}", message);
            e.printStackTrace();
        }
        HighDeviceOnUseRes body = res.getBody();
        //解析消息体
        if (res != null) {
            Signet signet = null;
            try {
                signet = signetService.get((Integer) webSocket.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (signet != null) {
                Integer applicationID = body.getApplicationID();
                Integer useTimes = body.getUseTimes();

                //设备盖章通知:设备{印章(新2)} 次数{11} 申请单ID{101}   这里的useTimes是指设备出厂后的总体使用次数（出厂后由0累加）
                log.info("设备盖章通知:设备{{}} 次数{{}} 申请单ID{{}}", signet.getName(), useTimes, applicationID);

                //TODO 回调通知第三方使用次数同步
                okHttpCli.sendCallback(signet, AppConstant.USE_COUNT, res);//使用次数同步
            }
        }
    }

//	/**
//	 * 查询指定设备状态
//	 * return  null:不在线  0:在线、关锁 1:在线、开锁
//	 */
//	protected Integer isOnline(Integer deviceId) {
//		if (deviceId != null) {
//			String key = RedisGlobal.PING + deviceId;
//			Object statusObj = redisUtil.get(key);
//			if (statusObj != null && StringUtil.isNotBlank(statusObj.toString())) {
//				try {
//					int status = Integer.parseInt(statusObj.toString());
//					return status;
//				} catch (NumberFormatException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 通知申请单指定印章使用了一次
//	 *
//	 * @param applicationID 申请单id
//	 * @param useTimes      使用次数
//	 * @param tsValue       阈值
//	 */
//	private void signetToUse(Integer applicationID, Integer useTimes, int tsValue, Integer signetId) {
//		//从缓存中检查当前次数是否已通知过了
//		if (applicationID != null) {
//			String key = RedisGlobal.APPLICATION_IS_USER + applicationID + ":" + signetId + ":" + useTimes;
//			Object obj = redisUtil.get(key);
//			if (obj != null) {
//				//已经通知过了
//				return;
//			} else {
//				try {
//					SignetToUse signetToUse = new SignetToUse();
//					signetToUse.setApplicationId(applicationID);
//					signetToUse.setTsValue(tsValue);
//					signetToUse.setSignetId(signetId);
//					signetToUse.setUseCount(useTimes);
//
//					//加入消息队列通知申请单系统
//					mqSender.sendToQueue(MqGlobal.queue_signet_to_use, signetToUse);
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.info("申请单ID:{{}}  印章ID:{{}} 通知申请单次数-1出错:{{}}", applicationID, signetId, e.getMessage());
//				}
//			}
//		}
//	}

    /**
     * 印章开关锁状态的返回
     */
    private void deviceUsingRes(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        //解析返回值
        DeviceBeingUsedRes body = JSONObject.parseObject(message, DeviceBeingUsedResPkg.class).getBody();

        if (body != null) {
            //如果新的状态与之前的状态不一致,则更新
            int newStatus = body.getUsedStatus();
            int oldStatus = webSocket.getStatus();
            if (oldStatus != newStatus) {
                webSocket.setStatus(newStatus);//0:关锁 1:开锁

                //异步监控开关锁状态
//				deviceAsyncService.lock(newStatus, body.getFingerUserId(), webSocket);

                log.info("开关锁状态更新:设备{{}}	status:{{}}", webSocket.getName(), newStatus == 1 ? "开锁" : "关锁");
            }
        }
    }

    /**
     * 网络状态改变返回
     */
    private void getWifiInfoRes(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        //解析消息体
        WifiInfoRes body = JSONObject.parseObject(message, WifiInfoResPkg.class).getBody();

        if (body != null) {
            //更新设备网络状态
            Object key = webSocket.getKey();
            Signet signet = null;
            if (key != null) {
                try {
                    signet = signetService.get((int) key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (signet != null) {

                //网络状态改变后,才更新,否则不更新
                String ssid = body.getSSID();
                if (!StringUtils.equals(ssid, signet.getNetType())) {
                    signet.setNetType(ssid);
                    signetService.update(signet);

                    //todo 存入mysql
                    String openMysql = AppConstant.OPEN_MYSQL;
                    if (StringUtils.isBlank(openMysql) || openMysql.equalsIgnoreCase("false")) {
                        mysqlSignetService = null;
                    }
                    if (mysqlSignetService != null) {
                        mysqlSignetService.add(signet);
                    }
                    //监控网络状态
                    body.setDeviceId(signet.getId());
                    deviceAsyncService.wifi(body, webSocket);

                    log.info("网络状态更新:设备{{}} 网络{{}}  密码{{}}", webSocket.getName(), body.getSSID());
                }
            }
        }
    }

    /**
     * wifi列表返回结果
     */
    private void getWifiListRes(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        log.info("获取到的wifi列表:" + message);
        //解析消息体
        WifiListRes res = JSONObject.parseObject(message, WifiListResPkg.class).getBody();
        if (res != null) {
            Signet signet = null;
            try {
                signet = signetService.get((Integer) webSocket.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (signet != null) {
                //解析消息体
                List<Object> wifiList = res.getWifiList();
                if (wifiList != null && wifiList.size() > 0) {
                    //创建存储到redis中的返回值wifi列表对象
                    List<String> wifis = new ArrayList<>();

                    //遍历设备传递过来的wifi列表
                    for (int i = 0; i < wifiList.size(); i++) {
                        String ssid = null;
                        JSONObject obj = null;
                        try {
                            obj = (JSONObject) wifiList.get(i);
                        } catch (Exception e) {
                            continue;
                        }
                        try {
                            ssid = obj.get("SSID").toString();
                        } catch (Exception e) {
                            continue;
                        }
                        try {
                            Object bssid = obj.get("BSSID");
                            if (bssid == null || StringUtils.isBlank(bssid.toString()) || "00:00:00:00:00:00".equalsIgnoreCase(bssid.toString())) {
                                continue;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                        if (StringUtils.isNotBlank(ssid)) {
                            wifis.add(ssid);
                        }
                    }

                    //将wifi列表存储到redis中
                    if (wifis.size() > 0) {
                        //TODO 存入缓存，获取列表的时候，可以通过缓存读取
                        String key = EHCacheGlobal.DEVICE_WIFI_LIST + signet.getId();
                        EHCacheUtil.put(key, wifis);
                    }
                }
            }
        }
    }

//	/**
//	 * 指纹清空返回结果
//	 */
//	private void fpClearRes(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
//		//解析消息体
//		FingerPrintClearRes res = JSONObject.parseObject(message, FingerPrintClearResPkg.class).getBody();
//		if (res != null) {
//			int fingerAddr = res.getFingerAddr();
//			int deivceID = res.getDeviceID();
//			int userID = res.getUserID();
//			if (fingerAddr == 0) {
//				//全部清空
//				fingerService.deleteAllByDevice(deivceID);
//				//监控指纹清空
//				deviceAsyncService.fingerPrintEmptyRes(deivceID, webSocket);
//				log.info("指纹清空成功:设备{{}}", webSocket.getName());
//			} else {
//				//删除指定位置
//				fingerService.deleteByDeviceAndAddr(deivceID, fingerAddr, userID);
//				//监控指纹删除
//				deviceAsyncService.fingerPrintDelRes(deivceID, fingerAddr, userID, webSocket);
//				log.info("指纹删除成功:设备{{}} 指纹ID{{}} 指纹Addr{{}}", webSocket.getName(), userID, fingerAddr);
//			}
//
//			//TODO:向指令发送者进行通知,向指纹所属人进行通知
//		}
//	}
//
//	/**
//	 * 指纹录入返回
//	 * ps:(以最后一次为准,将前一次进行覆盖)
//	 */
//	private void fpRecordRes(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
//		Signet signet = signetService.getByUUID(webSocket.getKey().toString());
//		if (signet != null) {
//			//解析消息体
//			FpRecordRes body = JSONObject.parseObject(message, FpRecordResPkg.class).getBody();
//
//			if (body != null) {
//				int res = body.getRes();
//				if (res == 0) {
//					//录入成功
//					int userID = body.getUserID();
//					Finger finger = fingerService.getByUser(userID, signet.getId());
//					if (finger == null) {
//						finger = new Finger();
//						finger.setCreateDate(new Date());
//					}
//					finger.setAddrNum(body.getFingerAddr());
//					finger.setUserId(userID);
//					finger.setUserName(body.getUserName());
//					finger.setUpdateDate(new Date());
//					finger.setCodeId(body.getCodeID());
//					fingerService.add(finger);
//
//					//异步指纹录入监控
//					deviceAsyncService.fingerPrintRes(finger, webSocket);
//
//					//TODO:向操作人发送录入成功通知
//
//					log.info("指纹录入成功:设备{{}} 指纹ID{{}} 指纹Addr{{}} 指纹名{{}}", webSocket.getName(), finger.getUserId(), finger.getAddrNum(), finger.getUserName());
//					return;
//				}
//				log.info("指纹录入失败:设备{{}} 消息{{}}", webSocket.getName(), body.toString());
//			}
//		}
//	}
//

    /**
     * 登录请求
     */
    private void deviceLoginReq(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {
        //解析消息体
        DeviceLoginReq body = JSONObject.parseObject(message, DeviceLoginReqPkg.class).getBody();
        DeviceLoginInfo info = body.getDeviceLoginInfo();

        //业务处理
        if (info != null) {
            int deviceType = info.getDeviceType();
            if (deviceType == 2) {
                //高拍仪登录
                meterLoginReq(body, webSocket);
            } else {
                //印章登录
                signetLoginReq(body, webSocket);
            }
        }
    }

    /**
     * 高拍仪登录请求
     */
    private void meterLoginReq(DeviceLoginReq body, @NotNull DeviceWebSocket webSocket) {
        //TODO:高拍仪登录请求逻辑
    }

    /**
     * 印章登录请求
     */
    private void signetLoginReq(DeviceLoginReq body, @NotNull DeviceWebSocket webSocket) {
        DeviceLoginInfo info = body.getDeviceLoginInfo();
        System.out.println("=================================印章登陆start=============================");
        System.out.println(info.toString());
        System.out.println("===============================印章登陆end=================================");
        //构建返回值对象
        DeviceLoginRes res = new DeviceLoginRes();
        res.setJwtTokenNew("");
        res.setRet(0);//0:登录成功  非0:登录失败,进入注册

        int deviceID = info.getDeviceID();
        Signet signet = signetService.get(deviceID);
        if (signet != null) {

            List<LoginApplication> loginApplicationInfo = info.getLoginApplicationInfo();
            //同步印章最近5次申请单记录,解决印章无网情况下次数同步问题
            log.info("-------------------信息监测-------------");
            log.info("设备登陆，同步申请单：{{}}", JSONObject.toJSON(loginApplicationInfo.get(0)));//{"useCount":2,"applicationId":10}
            deviceAsyncService.synchApplicationInfo(signet, loginApplicationInfo);

            String jwtToken = body.getJwtToken();
            boolean validate = JwtUtil.validate(jwtToken, signet.getUuid());
            if (validate) {
                //确定身份
                webSocket.setCaller(false);
                webSocket.setName(signet.getName());
                webSocket.setUuid(signet.getUuid());
                webSocket.setKey(deviceID);
                webSocket.setStatus(info.getUsedStatus());

                res.setJwtTokenNew("");

                //更新simNum
                String simNum = info.getSimNum();
                if (StringUtils.isBlank(signet.getSimNum()) && StringUtils.isNotBlank(simNum)) {
                    signet.setSimNum(simNum);
                }

                //更新ICCID
                String iccid = info.getiCCID();
                if (StringUtils.isBlank(signet.getIccid()) && StringUtils.isNotBlank(iccid)) {
                    signet.setIccid(iccid);
                }

                //更新imsi
                String imsi = info.getiMSI();
                if (StringUtils.isBlank(signet.getImsi()) && StringUtils.isNotBlank(imsi)) {
                    signet.setImsi(imsi);
                }


                //初始化网络状态
                signet.setNetType("4G");
                signetService.update(signet);

                //todo 存入mysql
                String openMysql = AppConstant.OPEN_MYSQL;
                if ("true".equalsIgnoreCase(openMysql)) {
                    mysqlSignetService.add(signet);
                }

//                String openMysql = AppConstant.OPEN_MYSQL;
//                if (StringUtils.isBlank(openMysql) || openMysql.equalsIgnoreCase("false")) {
//                    mysqlSignetService = null;
//                }
//                if (mysqlSignetService != null) {
//                    mysqlSignetService.add(signet);
//                }
                //异步推送离线消息
                deviceAsyncService.pushUnline(signet, webSocket);

                //组装登录响应返回
                MHPkg pkg_result = MHPkg.res(AppConstant.DEVICE_LOGIN_RES, res);
                String resMsg = JSONObject.toJSONString(pkg_result);
                webSocket.send(resMsg);
                log.info("登录成功:设备{{}}", signet.getId());
                //todo 设备登陆之后，将登陆信息发送给第三方，用于第三方同步设备信息
                okHttpCli.sendCallback(signet, AppConstant.DEVICE_LOGIN, signet);//登陆成功回调第三方
            } else {
                log.error("登录失败:设备{{}} 登录令牌错误", deviceID);
            }
        } else {
            log.error("登录失败:设备{{}} 该设备不存在", deviceID);
        }
    }


    /**
     * 设备注册请求
     */
    private void deviceRegReq(@NotEmpty String message, @NotNull DeviceWebSocket webSocket) {

        //解析消息
        DeviceLoginInfo info = JSONObject.parseObject(message, DeviceRegReqPkg.class).getBody().getDeviceLoginInfo();

        //业务处理
        if (info != null) {
            int deviceType = info.getDeviceType();
            //将注册信息中的对称密钥加入通道
            String symmetricKey = info.getSymmetricKey();
            webSocket.setSymmetricKey(symmetricKey);
            if (deviceType == 2) {
                //高拍仪注册
                meterRegReq(info, webSocket);
            } else {
                //印章注册
                signetRegReq(info, webSocket);
            }

        }

    }

    /**
     * 高拍仪注册
     *
     * @param info
     * @param webSocket
     */
    private void meterRegReq(DeviceLoginInfo info, @NotNull DeviceWebSocket webSocket) {
        //TODO:高拍仪注册逻辑
    }

    /**
     * 印章注册
     */
    private void signetRegReq(DeviceLoginInfo info, @NotNull DeviceWebSocket webSocket) {
        System.out.println("=================================印章注册start=============================");
        System.out.println(info.toString());
        System.out.println("===============================印章注册end=================================");

        String uuid = info.getStm32UUID();
        Signet signet = null;
        if (StringUtils.isBlank(uuid)) {
            log.error("注册失败:设备{{}} 无设备UUID", webSocket.getKey());
            return;
        }

        signet = signetService.getByUUID(uuid);
        if (signet == null) {
            //开始注册
            signet = new Signet();
            signet.setSimNum(info.getSimNum());
            signet.setIccid(info.getiCCID());
            signet.setImsi(info.getiMSI());
            signet.setCount(info.getUseCount());
            signet.setUuid(info.getStm32UUID());
            signet.setImei(info.getiMEI());
            signet.setType(info.getDeviceType());
            signet.setAddr(info.getAddr());
            signetService.add(signet);
            signet.setName("印章(新" + signet.getId() + ")");
            signetService.update(signet);
        } else {
            signet.setAddr(info.getAddr());
            signetService.update(signet);
        }
        //todo 存入mysql
        String openMysql = AppConstant.OPEN_MYSQL;
        if ("true".equalsIgnoreCase(openMysql)) {
            mysqlSignetService.add(signet);
        }
//        String openMysql = AppConstant.OPEN_MYSQL;
//        if (StringUtils.isBlank(openMysql) || openMysql.equalsIgnoreCase("false")) {
//            mysqlSignetService = null;
//        }
//        if (mysqlSignetService != null) {
//            mysqlSignetService.add(signet);
//        }
        //确定身份
        webSocket.setCaller(false);//设置非访客
        webSocket.setName(signet.getName());
        webSocket.setUuid(signet.getUuid());
        webSocket.setKey(signet.getId());

        //创建token
        String token = null;
        try {
            DeviceToken deviceToken = new DeviceToken();
            deviceToken.setId(signet.getId());
            deviceToken.setUuid(signet.getUuid());
            //1小时有效的token
            token = JwtUtil.createJWT(deviceToken, signet.getUuid(), 1000 * 60 * 60);
        } catch (Exception e) {
            log.info("注册失败:设备{{}} token生成出现异常{{}}", signet.getId(), e.getMessage());
            return;
        }


        //组装注册响应对象
        DeviceRegRes res = new DeviceRegRes();
        res.setRet(0);//0:注册成功
        res.setMsg("");

        //组装DeviceLoginReq
        DeviceLoginReq dq = new DeviceLoginReq();
        dq.setJwtToken(token);

        //组装DeviceLoginInfo
        DeviceLoginInfo info_new = new DeviceLoginInfo();
        info_new.setStm32UUID(uuid);
        info_new.setDeviceID(signet.getId());
        dq.setDeviceLoginInfo(info_new);

        //完成响应对象组装
        res.setDeviceLoginReq(dq);
        MHPkg resPkg = MHPkg.res(AppConstant.DEVICE_REG_RES, res);
        String resMsg = JSONObject.toJSONString(resPkg);
        log.info("注册成功返回值：{{}}", resMsg);
        webSocket.send(resMsg);
        //TODO aesKey存入缓存
        EHCacheUtil.put(EHCacheGlobal.SIGNET_AESKEY + signet.getId(), webSocket.getSymmetricKey());
        log.info("注册成功:设备{{}},aesKey{{}}", signet.getId(), webSocket.getSymmetricKey());
//		okHttpCli.sendCallback(signet,AppConstant.DEVICE_REGIST,info);//设备注册
    }

    /**
     * 从消息体中解析出cmd 协议号
     *
     * @param message
     * @return
     */
    private int analysisCMD(String message) {//"cmd":13,
        String cmd_str = null;
        String temp_str = null;
        message = message.replaceAll(" ", "");//处理掉空格
        if (message.contains("\"cmd\"")) {
            temp_str = "\"cmd\"";
        } else if (message.contains("\"Cmd\"")) {
            temp_str = "\"Cmd\"";
        }
        int cmd_index = message.indexOf(temp_str);//Cmd字符串的索引
        if (cmd_index > 0) {
            String cmd_to_end_str = message.substring(cmd_index);
            int flag_index = cmd_to_end_str.indexOf(":");//最前面的冒号的索引
            int flag2_index = cmd_to_end_str.indexOf(",");//最前面的逗号的索引
            cmd_str = cmd_to_end_str.substring(flag_index + 1, flag2_index);//这个就是cmd的协议号字符串形式
        }
        if (StringUtils.isNotBlank(cmd_str)) {
            return Integer.parseInt(cmd_str);
        }
        return -1;
    }

    /**
     * V2版,解析json格式密文，如果设备未注册状态，在解析json成功状态下，尝试将该通道设置为注册状态，以便后续进行业务处理
     *
     * @param message
     * @return
     */
    private String parseEncrypt(@NotEmpty String message, DeviceWebSocket webSocket) {
        JSONObject jsonObject = null;
        try {
            //如果消息体是空的，或者不是json格式的，直接返回，因为可能是V1版本的websocket通道发送的消息
            if (message == null || StringUtils.isBlank(message) || !message.startsWith("{")) {
                return message;
            }

            jsonObject = JSONObject.parseObject(message);

            //设备通道发过来的的密文(RSA/AES)
            Object encrypt = jsonObject.get("encrypt");

            //设备的通道标记 0：注册消息(RSA加密) 1：非注册消息(AES加密)
            int cmd = Integer.parseInt(jsonObject.get("cmd").toString());

            //如果设备发送过来的消息是非注册的 并且 此时websocket依旧是访客状态,该情况下，设备可能认为自己已注册成功，此时尝试将该通道设置为注册状态
            if (cmd == 1 && webSocket.isCaller()) {
                //设备通道的UUID
                Object uuid = jsonObject.get("uuid");
                Signet signet = null;
                if (uuid != null && StringUtils.isNotBlank(uuid.toString())) {
                    signet = signetService.getByUUID(uuid.toString());
                } else {
                    log.info("设备UUID：【{}】 CMD：【{}】 MSG：【{}】 设备不存在1", uuid == null ? "" : uuid.toString(), cmd, encrypt == null ? "" : encrypt.toString());
                    return message;
                }

                if (signet != null) {
                    //确定身份
                    identifyOwner(webSocket, uuid.toString(), signet);

                    //设置对称秘钥
                    String symmetricKey = webSocket.getSymmetricKey();
                    if (StringUtils.isBlank(symmetricKey)) {
                        //如果对称秘钥是空的，从缓存中取
                        String key = EHCacheGlobal.SIGNET_AESKEY + signet.getId();
                        Object o = EHCacheUtil.get(key);
                        if (StringUtils.isNotBlank(o.toString())) {
                            webSocket.setSymmetricKey(o.toString());
                        }
                    }
                } else {
                    log.info("设备UUID：【{}】 CMD：【{}】 MSG：【{}】 设备不存在2", uuid == null ? "" : uuid.toString(), cmd, encrypt == null ? "" : encrypt.toString());
                    return message;
                }
            }
            //TODO: 2020年1月14日14:14:58 若是通信异常,标记0的数据包始终无法重新注册,则设置通道为访客通道
            if (cmd == 0) {
                webSocket.setCaller(true);
            }
            //最后将密文返回进行逻辑处理
            if (encrypt != null && StringUtils.isNotBlank(encrypt.toString())) {
                message = encrypt.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    /**
     * 确定通道身份，标记注册成功，标记非访客，设置版本号等参数
     *
     * @param webSocket 通道对象
     * @param uuid      设备UUID
     * @param signet    设备对象
     */
    private void identifyOwner(@NotNull DeviceWebSocket webSocket, String uuid, Signet signet) {
        webSocket.setCaller(false);//设置非访客
        webSocket.setName(signet.getName());
        webSocket.setUuid(signet.getUuid());
        webSocket.setKey(signet.getId());

//		//查询版本号
//		try {
//			Config config = configService.getByUUID(signet.getUuid());
//			if (config == null) {
//				//查询默认配置
//				config = configService.getDefaultConfig();
//				//创建新配置信息
//				config.setId(null);
//				config.setUuid(uuid);
//				configService.add(config);
//			}
//			webSocket.setVersion(Float.valueOf(config.getVersion()));
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		}
    }
}

