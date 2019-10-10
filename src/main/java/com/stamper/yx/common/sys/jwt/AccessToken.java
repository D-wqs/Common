package com.stamper.yx.common.sys.jwt;

public class AccessToken implements TokenEntity{
    private String uuid;
    private long timestamp;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = System.currentTimeMillis();
    }
}
