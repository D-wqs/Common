package com.stamper.yx.common.entity.deviceModel;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/7/23 0023 10:45
 */
public class DeviceInit {
	private Integer initCount;//印章迁移时,初始化次数值
	private String initOrgName;//印章迁移时,初始化LED显示屏右上角公司名称

	public String getInitOrgName() {
		return initOrgName;
	}

	public void setInitOrgName(String initOrgName) {
		this.initOrgName = initOrgName;
	}

	public Integer getInitCount() {
		return initCount;
	}

	public void setInitCount(Integer initCount) {
		this.initCount = initCount;
	}
}
