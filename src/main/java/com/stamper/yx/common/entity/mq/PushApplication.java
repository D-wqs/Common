package com.stamper.yx.common.entity.mq;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/16 0016 17:48
 */
public class PushApplication {
	private Integer applicationId;//申请单id
	private String title;//申请单标题
	private Integer isQss;//加密类型
	private Integer totalCount;//总次数
	private Integer needCount;//已盖次数
	private Integer useCount;//剩余盖章次数
	private Integer signetId;//印章id
	private String userName;//申请人姓名
	private Integer userId;//申请人id

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getNeedCount() {
		return needCount;
	}

	public void setNeedCount(Integer needCount) {
		this.needCount = needCount;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getIsQss() {
		return isQss;
	}

	public void setIsQss(Integer isQss) {
		this.isQss = isQss;
	}

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	public Integer getSignetId() {
		return signetId;
	}

	public void setSignetId(Integer signetId) {
		this.signetId = signetId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
