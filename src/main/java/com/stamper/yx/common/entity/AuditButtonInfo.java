package com.stamper.yx.common.entity;

/**
 * @author D-wqs
 * @data 2019/11/25 15:06
 */
public class AuditButtonInfo {
    private Integer userID;//用户id
    private Integer useTimes;//使用次数
    private String userName;//用户名
    private Integer applicationID;//申请单id

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(Integer applicationID) {
        this.applicationID = applicationID;
    }
}
