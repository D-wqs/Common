package com.stamper.yx.common.entity;

import java.io.Serializable;

public class Signet implements Serializable {
    private Integer id;
    private String createDate;
    private String updateDate;
    private String deleteDate;

    /**
     * 印章名称
     */
    private String name;

    /**
     * 网络状态
     */
    private String netType;

    /**
     * 印章关联地址表
     */
    private String addr;

    /**
     * 印章使用次数
     */
    private Integer count;

    /**
     * 印章唯一码
     */
    private String uuid;

    /**
     * sim电话卡号码
     */
    private String simNum;

    /**
     * 联通物联网卡信息
     */
    private String iccid;

    /**
     * 联通物联网卡信息
     */
    private String imsi;

    private String imei;
    private Integer type;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 印章状态: 0:正常 1:异常 2:销毁 3:停用 4:锁定
     */
    private Integer status;

    /**
     * 休眠时间
     */
    private Integer sleepTime;

    /**
     * 指纹模式
     */
    private Boolean fingerPattern;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return create_date
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return update_date
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return delete_date
     */
    public String getDeleteDate() {
        return deleteDate;
    }

    /**
     * @param deleteDate
     */
    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    /**
     * 获取印章名称
     *
     * @return name - 印章名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置印章名称
     *
     * @param name 印章名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取网络状态
     *
     * @return net_type - 网络状态
     */
    public String getNetType() {
        return netType;
    }

    /**
     * 设置网络状态
     *
     * @param netType 网络状态
     */
    public void setNetType(String netType) {
        this.netType = netType;
    }

    /**
     * 获取印章关联地址表
     *
     * @return addr - 印章关联地址表
     */
    public String getAddr() {
        return addr;
    }

    /**
     * 设置印章关联地址表
     *
     * @param addr 印章关联地址表
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * 获取印章使用次数
     *
     * @return count - 印章使用次数
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置印章使用次数
     *
     * @param count 印章使用次数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取印章唯一码
     *
     * @return uuid - 印章唯一码
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置印章唯一码
     *
     * @param uuid 印章唯一码
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 获取sim电话卡号码
     *
     * @return sim_num - sim电话卡号码
     */
    public String getSimNum() {
        return simNum;
    }

    /**
     * 设置sim电话卡号码
     *
     * @param simNum sim电话卡号码
     */
    public void setSimNum(String simNum) {
        this.simNum = simNum;
    }

    /**
     * 获取联通物联网卡信息
     *
     * @return iccid - 联通物联网卡信息
     */
    public String getIccid() {
        return iccid;
    }

    /**
     * 设置联通物联网卡信息
     *
     * @param iccid 联通物联网卡信息
     */
    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    /**
     * 获取联通物联网卡信息
     *
     * @return imsi - 联通物联网卡信息
     */
    public String getImsi() {
        return imsi;
    }

    /**
     * 设置联通物联网卡信息
     *
     * @param imsi 联通物联网卡信息
     */
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    /**
     * 获取印章状态: 0:正常 1:异常 2:销毁 3:停用 4:锁定
     *
     * @return status - 印章状态: 0:正常 1:异常 2:销毁 3:停用 4:锁定
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置印章状态: 0:正常 1:异常 2:销毁 3:停用 4:锁定
     *
     * @param status 印章状态: 0:正常 1:异常 2:销毁 3:停用 4:锁定
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取休眠时间
     *
     * @return sleep_time - 休眠时间
     */
    public Integer getSleepTime() {
        return sleepTime;
    }

    /**
     * 设置休眠时间
     *
     * @param sleepTime 休眠时间
     */
    public void setSleepTime(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * 获取指纹模式
     *
     * @return finger_pattern - 指纹模式
     */
    public Boolean getFingerPattern() {
        return fingerPattern;
    }

    /**
     * 设置指纹模式
     *
     * @param fingerPattern 指纹模式
     */
    public void setFingerPattern(Boolean fingerPattern) {
        this.fingerPattern = fingerPattern;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", deleteDate=").append(deleteDate);
        sb.append(", name=").append(name);
        sb.append(", netType=").append(netType);
        sb.append(", addr=").append(addr);
        sb.append(", count=").append(count);
        sb.append(", uuid=").append(uuid);
        sb.append(", simNum=").append(simNum);
        sb.append(", iccid=").append(iccid);
        sb.append(", imsi=").append(imsi);
        sb.append(", status=").append(status);
        sb.append(", sleepTime=").append(sleepTime);
        sb.append(", fingerPattern=").append(fingerPattern);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}