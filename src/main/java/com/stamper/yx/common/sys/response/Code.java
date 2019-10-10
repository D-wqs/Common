package com.stamper.yx.common.sys.response;

public enum Code {
    OK(200,"请求成功"),
    FAIL_USER_NULL(400,"当前用户不存在"),
    FAIL_TOKEN(400,"凭证校验失败"),
    FAIL400(400,"请求失败"),
    ERROR_PARAMETER(501,"参数有误"),
    ERROR500(500,"系统异常"),
    ERROR_DEVICE_NULL(400,"当前设备不存在");
    private int code;
    private String msg;
    Code(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
