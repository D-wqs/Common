package com.stamper.yx.common.websocket.core;


public class ObjectMessage {
	private Object data;//推送的消息对象

	public ObjectMessage(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
