package com.stamper.yx.common.websocket.handle;


import com.stamper.yx.common.websocket.core.DefaultWebSocket;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/1 0001 18:00
 */
public interface SendMessageFilter<T extends DefaultWebSocket> {
	boolean isAccess(Object message, T t);
}
