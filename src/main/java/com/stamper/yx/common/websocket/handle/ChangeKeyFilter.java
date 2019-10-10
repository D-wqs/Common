package com.stamper.yx.common.websocket.handle;

import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zhf_10@163.com
 * @Description 设备标识key更换过滤器
 * @date 2019/5/1 0001 21:50
 */
public interface ChangeKeyFilter<T extends DefaultWebSocket> {
	@Async
	void doFilter(T t);
}
