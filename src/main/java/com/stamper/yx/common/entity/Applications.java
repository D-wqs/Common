package com.stamper.yx.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 申请单类
 *
 * @author D-wqs
 * @data 2019/11/21 16:16
 */
public class Applications implements Serializable{
    private Integer id;
    private Date createDate;
    private Date updateDate;
    private Date deleteDate;
    private Integer applicationId;//申请单id（流程id）
    private String title;//申请标题
    private Integer totalCount;//该申请单申请的次数
    private Integer needCount;//已用次数
    private Integer deviceId;//设备地
    private Integer userId;//用户id
    private String userName;//用户名

    public Applications() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
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

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Applications{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", deleteDate=" + deleteDate +
                ", applicationId=" + applicationId +
                ", title='" + title + '\'' +
                ", totalCount=" + totalCount +
                ", needCount=" + needCount +
                ", deviceId=" + deviceId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
