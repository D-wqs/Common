package com.stamper.yx.common.entity;

import java.io.Serializable;

public class OrderLog implements Serializable{
    private Integer id;
    private String createDate;
    private String updateDate;
    private String deleteDate;
    private Integer cmd;//指令命令号
    private String name;//指令名称
    private String jsonData;//指令内容
    private Integer status;//0:指令发送成功  1:指令发送失败
    private String errorMsg; //异常信息

    public OrderLog() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OrderLog{" +
                "id=" + id +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", deleteDate='" + deleteDate + '\'' +
                ", cmd=" + cmd +
                ", name='" + name + '\'' +
                ", jsonData='" + jsonData + '\'' +
                ", status=" + status +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
