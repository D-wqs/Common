package com.stamper.yx.common.websocket.quartz;

import com.stamper.yx.common.controller.DeviceWebSocket;
import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * springboot简单定时任务
 */
@Slf4j
@Component
@Configurable
@EnableScheduling   //启动定时任务
public class WebsocketMonitorScheduledTasks {

	@Autowired
	private DefaultWebSocketPool pool;

	//每10秒钟,处理一下垃圾池里面的链接信息,存数据库
	@Scheduled(fixedRate = 1000 * 10)
	public void reportCurrentTime() {
		List<DefaultWebSocket> destroy = pool.getDestroy();
//		log.info("垃圾池统计：" + destroy.size());
		if (!destroy.isEmpty()) {
			for (int i = 0; i < destroy.size(); i++) {
				DefaultWebSocket webSocket = destroy.get(i);
				if (webSocket != null) {
					Object key = webSocket.getKey();
					try {
						receive(webSocket);
					} finally {
						log.info("*****移除垃圾桶中的废弃通道{{}}*****", key);
						i--;
					}
				}
				try {
					if (webSocket != null) {
						webSocket.close();
						webSocket.setSession(null);
					}
				} catch (IOException e) {
				}
				destroy.remove(webSocket);
			}
			destroy.clear();
		}

		ConcurrentHashMap<String, DefaultWebSocket> actice = pool.getActive();
//		log.info("socket连接池统计:" + actice.size());
		if (!actice.isEmpty()) {
			Iterator<Map.Entry<String, DefaultWebSocket>> it = actice.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, DefaultWebSocket> en = it.next();
				DefaultWebSocket socket = en.getValue();
				Date updateDate = socket.getUpdateDate();
				if (updateDate == null) {
					it.remove();
					log.info("移除无交互链接{{}}", en.getKey());
					continue;
				}
				Date date = new Date();
				if (date.getTime() - updateDate.getTime() > 10000) {
					try {
						socket.close();
						socket.setSession(null);
					} catch (IOException e) {
					}
					it.remove();
					log.info("移除无心跳链接{{}}", en.getKey());
					continue;
				}
			}
		}
	}

	/**
	 * 处理监控数据
	 */
	private void receive(DefaultWebSocket webSocket) {
		if (webSocket != null) {
			//仅处理认证客户端
			if (!webSocket.isCaller()) {
				DeviceWebSocket socket = (DeviceWebSocket) webSocket;

//				/**
//				 * 处理开关机记录
//				 */
//				DetailSwitch ds = socket.getDetailSwitch();
//				log.info("监控:开关机记录--key{{}}", socket.getKey());
//				if (ds != null && ds.getOffTime() == null) {
//					ds.setUpdateDate(new Date());
//					//如果关机时间不存在,以当前时间为准
//					ds.setOffTime(socket.getDeleteDate());
//				}
//				try {
//					detailSwitchService.add(ds);
//				} catch (Exception e) {
//					e.printStackTrace();
//					log.error("设备开关机记录添加出错-->设备:{{}} 错误:{{}}", socket.getName(), e.getMessage());
//				}
//
//				/**
//				 * 处理指纹记录
//				 */
//				List<DetailFinger> dfs = socket.getDetailFingers();
//				log.info("监控:指纹记录{{}}--key{{}}", dfs.size(), socket.getKey());
//				if (dfs != null && dfs.size() > 0) {
//					for (int i = 0; i < dfs.size(); i++) {
//						DetailFinger df = dfs.get(i);
//						try {
//							detailFingerService.add(df);
//						} catch (Exception e) {
//							e.printStackTrace();
//							log.error("设备指纹交互记录日志添加出错-->设备:{{}} 错误:{{}}", socket.getName(), e.getMessage());
//						}
//					}
//				}
//
//				/**
//				 * 处理开关锁记录
//				 */
//				List<DetailLock> dls = socket.getDetailLocks();
//				log.info("监控:开关锁记录{{}}--key{{}}", dls.size(), socket.getKey());
//				if (dls != null && dls.size() > 0) {
//					for (int i = 0; i < dls.size(); i++) {
//						DetailLock dl = dls.get(i);
//						try {
//							detailLockService.add(dl);
//						} catch (Exception e) {
//							e.printStackTrace();
//							log.error("设备开关锁记录添加出错-->设备:{{}} 错误:{{}}", socket.getName(), e.getMessage());
//						}
//					}
//				}
//
//				/**
//				 * 处理网络状态记录
//				 */
//				List<DetailNetwork> dns = socket.getDetailNetworks();
//				log.info("监控:网络状态记录{{}}--key{{}}", dns.size(), socket.getKey());
//				if (dns != null && dns.size() > 0) {
//					for (int i = 0; i < dns.size(); i++) {
//						DetailNetwork dn = dns.get(i);
//						try {
//							detailNetworkService.add(dn);
//						} catch (Exception e) {
//							e.printStackTrace();
//							log.error("设备网络状态记录添加出错-->设备:{{}} 错误:{{}}", socket.getName(), e.getMessage());
//						}
//					}
//				}
//
//				/**
//				 * 处理申请单推送结束记录
//				 */
//				List<Map<Future, DetailApplication>> detailApplications = socket.getDetailApplications();
//				log.info("监控:申请单推送结束记录{{}}--key{{}}", detailApplications.size(), socket.getKey());
//				if (detailApplications != null && detailApplications.size() > 0) {
//					for (int i = 0; i < detailApplications.size(); i++) {
//						Map<Future, DetailApplication> map = detailApplications.get(i);
//						Map.Entry<Future, DetailApplication> first = map.entrySet().iterator().next();
//						if (first != null) {
//							DetailApplication detailApplication = first.getValue();
//							Future future = first.getKey();
//							detailApplication.setStatus(future.isDone() ? 0 : 1);
//							try {
//								detailApplicationService.add(detailApplication);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//
//				/**
//				 * 处理地址改变记录
//				 */
//				List<DetailAddr> detailAddrs = socket.getDetailAddrs();
//				log.info("监控-->id:{{}} 设备:{{}} 同步移动轨迹记录", socket.getKey(), socket.getName());
//				if (detailAddrs != null && detailAddrs.size() > 0) {
//					for (int i = 0; i < detailAddrs.size(); i++) {
//						DetailAddr detailAddr = detailAddrs.get(i);
//						try {
//							detailAddrService.add(detailAddr);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
			}
		}
	}

//	@Scheduled(cron = "0 */1 *  * * * ")
//	public void reportCurrentByCron() {
//		System.out.println("每0 */1 *  * * * 执行一次");
//	}
}