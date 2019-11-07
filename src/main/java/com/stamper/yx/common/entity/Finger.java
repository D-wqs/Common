package com.stamper.yx.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 处理Mysql数据源存储指纹信息
 *
 * @author D-wqs
 * @data 2019/10/31 9:49
 */
public class Finger implements Serializable {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer deviceId;
    private Integer fingerAddr;//指纹地址
    private Date createDate;
    private Date updateDate;
    private Date deleteDate;

    public Finger() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getFingerAddr() {
        return fingerAddr;
    }

    public void setFingerAddr(Integer fingerAddr) {
        this.fingerAddr = fingerAddr;
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

    @Override
    public String toString() {
        return "Finger{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", deviceId=" + deviceId +
                ", fingerAddr=" + fingerAddr +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", deleteDate=" + deleteDate +
                '}';
    }
}
