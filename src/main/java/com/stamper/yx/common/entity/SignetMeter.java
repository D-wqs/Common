package com.stamper.yx.common.entity;

/**
 * @author D-wqs
 * @data 2019/11/11 18:07
 */
public class SignetMeter {
    private Integer signetId;
    private Integer meterId;

    public SignetMeter() {
    }

    public Integer getSignetId() {
        return signetId;
    }

    public void setSignetId(Integer signetId) {
        this.signetId = signetId;
    }

    public Integer getMeterId() {
        return meterId;
    }

    public void setMeterId(Integer meterId) {
        this.meterId = meterId;
    }

    @Override
    public String toString() {
        return "SignetMeter{" +
                "signetId=" + signetId +
                ", meterId=" + meterId +
                '}';
    }
}
