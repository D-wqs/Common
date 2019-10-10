package com.stamper.yx.common.entity.deviceModel;

import com.alibaba.fastjson.annotation.JSONField;

public class DeviceLoginRes {

	public int ret;

    public String msg;

    public String jwtTokenNew="";

	@JSONField(name = "Ret")
	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	@JSONField(name = "Msg")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@JSONField(name = "JwtTokenNew")
	public String getJwtTokenNew() {
		return jwtTokenNew;
	}

	public void setJwtTokenNew(String jwtTokenNew) {
		this.jwtTokenNew = jwtTokenNew;
	}

	@Override
	public String toString() {
		return "DeviceLoginRes{" +
				"ret=" + ret +
				", msg='" + msg + '\'' +
				", jwtTokenNew='" + jwtTokenNew + '\'' +
				'}';
	}
}