package com.stamper.yx.common.entity;

import java.io.Serializable;

/**
 * @author D-wqs
 * @data 2019/11/21 20:05
 */
public class HistoryApplicationInfo implements Serializable{
    private Integer applicationId;//申请单id
    private Integer useCount;//当前使用次数

    public HistoryApplicationInfo() {
    }

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

    @Override
    public String toString() {
        return "HistoryApplicationInfo{" +
                "applicationId=" + applicationId +
                ", useCount=" + useCount +
                '}';
    }
}
