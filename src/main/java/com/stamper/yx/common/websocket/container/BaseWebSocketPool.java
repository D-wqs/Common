package com.stamper.yx.common.websocket.container;

import com.stamper.yx.common.websocket.core.DefaultWebSocket;

import java.util.concurrent.Future;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/1 0001 18:12
 */
public interface BaseWebSocketPool {
	// 查
	DefaultWebSocket get(String key);

	// 增
	void add(String key, DefaultWebSocket socket);

	// 删
	void del(String key);

	// 删
	void del(DefaultWebSocket socket);

	// 改
	void update(String srcKey, String desKey);

	Future send(String key, Object obj);

	boolean isOpen(String deviceId);

}
