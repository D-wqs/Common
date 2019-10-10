package com.stamper.yx.common.entity.deviceModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceBeingUsedRes {
	@JsonProperty("UsedStatus")
	public int usedStatus;//0:关锁 1:开锁

	@JsonProperty("FingerUserId")
	public int fingerUserId;//开锁人id(仅开锁状态存在)

	public int getUsedStatus() {
		return usedStatus;
	}

	public void setUsedStatus(int usedStatus) {
		this.usedStatus = usedStatus;
	}

	public int getFingerUserId() {
		return fingerUserId;
	}

	public void setFingerUserId(int fingerUserId) {
		this.fingerUserId = fingerUserId;
	}

	@Override
	public String toString() {
		return "DeviceBeingUsedRes{" +
				"usedStatus=" + usedStatus +
				", fingerUserId=" + fingerUserId +
				'}';
	}
}