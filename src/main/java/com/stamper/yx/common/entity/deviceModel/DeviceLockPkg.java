package com.stamper.yx.common.entity.deviceModel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author zhf_10@163.com
 * @Description 远程锁定功能返回数据包实例
 * @date 2019/8/6 0006 17:32
 */
public class DeviceLockPkg {
	@JsonProperty("Head")
	private MHHead head;//包头

	@JsonProperty("Body")
	private DeviceLock body;//包体

	@JsonProperty("Crc")
	private String crc;

	public MHHead getHead() {
		return head;
	}

	public void setHead(MHHead head) {
		this.head = head;
	}

	public DeviceLock getBody() {
		return body;
	}

	public void setBody(DeviceLock body) {
		this.body = body;
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}
}
