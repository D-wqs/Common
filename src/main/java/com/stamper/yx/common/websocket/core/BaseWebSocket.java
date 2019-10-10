package com.stamper.yx.common.websocket.core;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * @author zhf_10@163.com
 * @Description 定义接口:约定websocket通道本身能实现的功能
 * @date 2019/4/30 0030 16:00
 */
public interface BaseWebSocket {
	//向本身通道发送消息
	Future send(Object obj);
	//关闭本身的通道
	void close() throws IOException;
	//当前通道是否打开
	boolean isOpen();
	//当前通道是否关闭
	boolean isClosed();
}
