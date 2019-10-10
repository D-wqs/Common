package com.stamper.yx.common.websocket;

import com.stamper.yx.common.websocket.container.DefaultWebSocketPool;
import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import com.stamper.yx.common.websocket.handle.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/1 0001 19:13
 */
@Slf4j
@Component
public class WebSocketInit implements CommandLineRunner {

	@Autowired
	private DefaultWebSocketPool pool;
	@Autowired(required = false)
	private OpenChannelFilter onOpenFilter;
	@Autowired(required = false)
	private CloseChannelFilter onCloseFilter;
	@Autowired(required = false)
	private ChannelErrorFilter onErrorFilter;
	@Autowired(required = false)
	private ReceiveMessageFilter onMessageFilter;
	@Autowired(required = false)
	private SendMessageFilter onSendFilter;
	@Autowired(required = false)
	private ChangeKeyFilter changeKeyFilter;

	@Override
	public void run(String... args) throws Exception {
		log.info("...........初始化Websocket容器...........");
		DefaultWebSocket.pool = pool;
		DefaultWebSocket.onOpenFilter=onOpenFilter;
		DefaultWebSocket.onCloseFilter=onCloseFilter;
		DefaultWebSocket.onSendFilter=onSendFilter;
		DefaultWebSocket.onMessageFilter=onMessageFilter;
		DefaultWebSocket.onErrorFilter=onErrorFilter;
		DefaultWebSocket.changeKeyFilter=changeKeyFilter;
		log.info("...........初始化Websocket容器完成...........");
	}
}
