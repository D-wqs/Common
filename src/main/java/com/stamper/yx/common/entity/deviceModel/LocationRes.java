package com.stamper.yx.common.entity.deviceModel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/3 0003 1:37
 */
public class LocationRes {

	@JsonProperty("Addr")
	private String addr;

	@JsonProperty("Latitude")
	private String latitude;

	@JsonProperty("Longitude")
	private String longitude;

	@JsonProperty("DeviceId")
	private int deviceId;

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
}
