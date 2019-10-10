package com.stamper.yx.common.websocket.container;

import com.stamper.yx.common.controller.DeviceWebSocket;
import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/1 0001 18:16
 */
@Slf4j
@Component
public class DefaultWebSocketPool implements BaseWebSocketPool {

	private static ConcurrentHashMap<String, DefaultWebSocket> active = new ConcurrentHashMap<>();
	private static List<DefaultWebSocket> destroy = new LinkedList<>();

	/**
	 * 向容器中添加
	 *
	 * @param key 未登录情况下,该参数为时间戳   登录情况下,该参数为设备ID
	 */
	@Override
	public void add(String key, DefaultWebSocket socket) {
		if (socket != null && StringUtils.isNotBlank(key.toString())) {
			active.put(key, socket);
			log.info("POOL:添加新链接->key:{{}} 总数量:{{}} 待销毁:{{}}", key, active.size(), destroy.size());
		} else {
			log.info("向容器中添加连接失败");
		}
	}


	@Override
	public void del(String key) {
		if (StringUtils.isNotBlank(key.toString())) {
			synchronized (active) {
				DefaultWebSocket socket = active.remove(key);
				destroy.add(socket);
				//log.info("Pool:channel was deleted key->{{}}", key);
			}
		}
	}

	@Override
	public void del(DefaultWebSocket socket) {
		if (socket != null && !active.isEmpty() && active.containsValue(socket)) {
			synchronized (active) {
				Iterator<Map.Entry<String, DefaultWebSocket>> it = active.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, DefaultWebSocket> en = it.next();
					if (en.getValue() == socket) {
						it.remove();
						if (!socket.isCaller()) {
							//不是访客的时候才进行监控
							destroy.add(socket);
						}
						//log.info("Pool:channel was deleted key->{{}}", en.getKey());
						return;
					}
				}
			}
		}
	}

	@Override
	public void update(String srcKey, String desKey) {
		if (StringUtils.isNoneBlank(srcKey.toString(), desKey.toString())) {
			synchronized (active) {
				DefaultWebSocket webSocket = active.remove(srcKey);
				if (webSocket != null) {
					active.put(desKey, webSocket);
					//log.info("Pool:the channel key->{{}} was modified to key->{{}}", srcKey, desKey);
				}
			}
		}
	}

	/**
	 * 查询指定deviceId是否在容器中
	 *
	 * @param key 设备ID
	 * @return
	 */
	@Override
	public DeviceWebSocket get(String key) {
		if (StringUtils.isNotBlank(key.toString()) && !active.isEmpty()) {
			DefaultWebSocket defaultWebSocket = active.get(key);
			if (defaultWebSocket != null) {
				return (DeviceWebSocket) defaultWebSocket;
			}
		}
		return null;
	}


	@Override
	public Future send(String key, Object obj) {
		if (StringUtils.isNotBlank(key) && obj != null) {
			DefaultWebSocket webSocket = active.get(key);
			if (webSocket != null) {
				return webSocket.send(obj);
			}
		}
		return null;
	}

	@Override
	public boolean isOpen(String deviceId) {
		if (StringUtils.isNotBlank(deviceId.toString()) && !active.isEmpty()) {
			DefaultWebSocket webSocket = active.get(deviceId);
			if (webSocket != null) {
				return webSocket.isOpen();
			} else {
//				log.info("online:设备{{}}通道不存在,设备不在线", deviceId);
			}
		} else {
//			log.info("online:设备{{}}容器为空,设备不在线", deviceId);
		}
		return false;
	}

	public static ConcurrentHashMap<String, DefaultWebSocket> getActive() {
		return active;
	}

	public static List<DefaultWebSocket> getDestroy() {
		return destroy;
	}
}
