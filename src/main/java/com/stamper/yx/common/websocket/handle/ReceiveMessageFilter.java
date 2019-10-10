package com.stamper.yx.common.websocket.handle;

import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/1 0001 17:52
 */
public interface ReceiveMessageFilter<T extends DefaultWebSocket> {
	@Async
	boolean doFilter(String message, T webSocket);
}
