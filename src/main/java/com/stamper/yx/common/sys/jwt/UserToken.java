package com.stamper.yx.common.sys.jwt;

public class UserToken implements TokenEntity{
    private Integer userId;
    private String userName;
    private long timestamp;

    public UserToken() {
    }

    public UserToken(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = System.currentTimeMillis();
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
        return "UserToken{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
