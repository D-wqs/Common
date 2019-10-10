package com.stamper.yx.common.sys.response;

public class ResultVO {
    private int code;
    private String msg;
    private Object data;

    public ResultVO() {
    }

    public ResultVO(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultVO OK() {
        return new ResultVO(200, "成功", null);
    }
    public static ResultVO OK(String msg,Object data){
        return new ResultVO(200,msg,data);
    }

    public static ResultVO OK(Object data) {
        return new ResultVO(Code.OK.getCode(), Code.OK.getMsg(), data);
    }


    public static ResultVO FAIL() {
        return new ResultVO(400, "失败", null);
    }

    public static ResultVO FAIL(String msg){
        return new ResultVO(Code.FAIL400.getCode(),msg,null);
    }
    public static ResultVO FAIL(Code code){
        return new ResultVO(code.getCode(),code.getMsg(),null);
    }
    public static ResultVO ERROR(Code code){
        return new ResultVO(code.getCode(),code.getMsg(),null);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
