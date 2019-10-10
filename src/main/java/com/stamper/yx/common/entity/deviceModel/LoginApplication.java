package com.stamper.yx.common.entity.deviceModel;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/7/23 0023 10:10
 */
public class LoginApplication {
	private Integer applicationId;//申请单号
	private Integer useCount;//已用次数
	private Integer signet;//印章id
	private Integer tsValue;//印章阈值

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	public Integer getSignet() {
		return signet;
	}

	public void setSignet(Integer signet) {
		this.signet = signet;
	}

	public Integer getTsValue() {
		return tsValue;
	}

	public void setTsValue(Integer tsValue) {
		this.tsValue = tsValue;
	}
}
