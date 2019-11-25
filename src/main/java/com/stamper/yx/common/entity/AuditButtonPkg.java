package com.stamper.yx.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stamper.yx.common.entity.deviceModel.MHHead;

/**
 * @author D-wqs
 * @data 2019/11/25 15:05
 */
public class AuditButtonPkg {
    @JsonProperty("Head")
    private MHHead head;//包头

    @JsonProperty("Body")
    private AuditButtonInfo body;//包体

    @JsonProperty("Crc")
    private String crc;

    public MHHead getHead() {
        return head;
    }

    public void setHead(MHHead head) {
        this.head = head;
    }

    public AuditButtonInfo getBody() {
        return body;
    }

    public void setBody(AuditButtonInfo body) {
        this.body = body;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }
}
