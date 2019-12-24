package com.stamper.yx.common.entity;

import java.util.Date;

public class SealRecordInfo {
    private Integer id;
    private Date createDate;
    private Date updateDate;
    private Date deleteDate;
    private Integer deviceID;//当前设备id
    private String uuid;//当前设备uuid
    private String identity;//用印人名称
    private Integer picUseId;//用印人id
    private String location;//当前地址
    private String recsn;//未知变量
    private String fpCode;//未知变量
    private Integer count;//当前使用记录对应的次数
    private Integer applicationID;//申请单的id
    private Integer isAudit;//0:该条记录是盖章上传创建的  1:该条记录是审计上传创建的'
    private Integer idAudit;//未知变量
    private String time;//真实盖章时间
    private Integer sealCount;//高拍仪关联的-印章次数
    private Integer sealId;//高拍仪关联的-印章ID
    private Integer alarm;//0:正常 1:长按警报 2:防拆卸报警
    private String fileupload;//文件密文数据
    private Integer fileId;//关联上传的文件信息
    private Integer sriType;//0:申请单模式 1:申请单模式(量子) 2:指纹模式 3:指纹模式(量子)

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

    public String getFileupload() {
        return fileupload;
    }

    public void setFileupload(String fileupload) {
        this.fileupload = fileupload;
    }

    public SealRecordInfo() {
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Integer getPicUseId() {
        return picUseId;
    }

    public void setPicUseId(Integer picUseId) {
        this.picUseId = picUseId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRecsn() {
        return recsn;
    }

    public void setRecsn(String recsn) {
        this.recsn = recsn;
    }

    public String getFpCode() {
        return fpCode;
    }

    public void setFpCode(String fpCode) {
        this.fpCode = fpCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(Integer applicationID) {
        this.applicationID = applicationID;
    }

    public Integer getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(Integer isAudit) {
        this.isAudit = isAudit;
    }

    public Integer getIdAudit() {
        return idAudit;
    }

    public void setIdAudit(Integer idAudit) {
        this.idAudit = idAudit;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getSealCount() {
        return sealCount;
    }

    public void setSealCount(Integer sealCount) {
        this.sealCount = sealCount;
    }

    public Integer getSealId() {
        return sealId;
    }

    public void setSealId(Integer sealId) {
        this.sealId = sealId;
    }

    public Integer getAlarm() {
        return alarm;
    }

    public void setAlarm(Integer alarm) {
        this.alarm = alarm;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getSriType() {
        return sriType;
    }

    public void setSriType(Integer sriType) {
        this.sriType = sriType;
    }

    @Override
    public String toString() {
        return "SealRecordInfo{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", deleteDate=" + deleteDate +
                ", deviceID=" + deviceID +
                ", uuid='" + uuid + '\'' +
                ", identity='" + identity + '\'' +
                ", picUseId=" + picUseId +
                ", location='" + location + '\'' +
                ", recsn='" + recsn + '\'' +
                ", fpCode='" + fpCode + '\'' +
                ", count=" + count +
                ", applicationID=" + applicationID +
                ", isAudit=" + isAudit +
                ", idAudit=" + idAudit +
                ", time='" + time + '\'' +
                ", sealCount=" + sealCount +
                ", sealId=" + sealId +
                ", alarm=" + alarm +
                ", fileupload='" + fileupload + '\'' +
                ", fileId=" + fileId +
                ", sriType=" + sriType +
                '}';
    }
}
