package com.stamper.yx.common.websocket.handle;

import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/1 0001 17:51
 */
public interface ChannelErrorFilter<T extends DefaultWebSocket> {
	@Async
	void afterError(T t, Throwable error);
}
