package com.stamper.yx.common.service;

import com.stamper.yx.common.controller.DeviceWebSocket;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/2 0002 19:32
 */
public interface DeviceWebSocketService {
	void doWork(String message, DeviceWebSocket webSocket);

}
