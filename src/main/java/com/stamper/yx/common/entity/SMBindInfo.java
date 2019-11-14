package com.stamper.yx.common.entity;

import java.io.Serializable;

/**
 * 高拍仪关联详情
 * @author D-wqs
 * @data 2019/11/12 20:14
 */
public class SMBindInfo implements Serializable{
    private Integer meterId;
    private Integer signetId;
    private String meterName;//高拍仪名称
    private String signetName;//印章名称
    private String meterOrigin;//高拍仪来源
    private String signetOrigin;//印章来源
    private String meterOwner;//高拍仪拥有者
    private String signetOwner;//印章拥有者

    public SMBindInfo() {
    }

    public Integer getMeterId() {
        return meterId;
    }

    public void setMeterId(Integer meterId) {
        this.meterId = meterId;
    }

    public Integer getSignetId() {
        return signetId;
    }

    public void setSignetId(Integer signetId) {
        this.signetId = signetId;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getSignetName() {
        return signetName;
    }

    public void setSignetName(String signetName) {
        this.signetName = signetName;
    }

    public String getMeterOrigin() {
        return meterOrigin;
    }

    public void setMeterOrigin(String meterOrigin) {
        this.meterOrigin = meterOrigin;
    }

    public String getSignetOrigin() {
        return signetOrigin;
    }

    public void setSignetOrigin(String signetOrigin) {
        this.signetOrigin = signetOrigin;
    }

    public String getMeterOwner() {
        return meterOwner;
    }

    public void setMeterOwner(String meterOwner) {
        this.meterOwner = meterOwner;
    }

    public String getSignetOwner() {
        return signetOwner;
    }

    public void setSignetOwner(String signetOwner) {
        this.signetOwner = signetOwner;
    }

    @Override
    public String toString() {
        return "SMBindInfo{" +
                "meterId=" + meterId +
                ", signetId=" + signetId +
                ", meterName='" + meterName + '\'' +
                ", signetName='" + signetName + '\'' +
                ", meterOrigin='" + meterOrigin + '\'' +
                ", signetOrigin='" + signetOrigin + '\'' +
                ", meterOwner='" + meterOwner + '\'' +
                ", signetOwner='" + signetOwner + '\'' +
                '}';
    }
}
