package com.stamper.yx.common.websocket.handle;

import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/1 0001 17:44
 */
public interface CloseChannelFilter<T extends DefaultWebSocket> {
	@Async
	void afterClose(T webSocket);
}
