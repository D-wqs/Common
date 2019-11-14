package com.stamper.yx.common.entity;

import java.io.Serializable;

/**
 * 印章只需要通知高拍仪拍照即可，参数由高拍仪自己定
 *
 * @author D-wqs
 * @data 2019/11/11 15:11
 */
public class Meter implements Serializable {
    private Integer id;
    private String name;
    private Integer meterIndex;
    private Integer meterType;
    private String uuid;
    private String clientAddr;
    private String createDate;
    private String updateDate;
    private String deleteDate;

    public Meter() {
    }

    public String getClientAddr() {
        return clientAddr;
    }

    public void setClientAddr(String clientAddr) {
        this.clientAddr = clientAddr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMeterIndex() {
        return meterIndex;
    }

    public void setMeterIndex(Integer meterIndex) {
        this.meterIndex = meterIndex;
    }

    public Integer getMeterType() {
        return meterType;
    }

    public void setMeterType(Integer meterType) {
        this.meterType = meterType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public String toString() {
        return "Meter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", meterIndex=" + meterIndex +
                ", meterType=" + meterType +
                ", uuid='" + uuid + '\'' +
                ", clientAddr='" + clientAddr + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", deleteDate='" + deleteDate + '\'' +
                '}';
    }
}
