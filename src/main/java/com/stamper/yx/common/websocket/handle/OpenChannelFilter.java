package com.stamper.yx.common.websocket.handle;

import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/1 0001 17:40
 */
public interface OpenChannelFilter<T extends DefaultWebSocket> {
	@Async
	void afterOpen(T webSocket);
}
