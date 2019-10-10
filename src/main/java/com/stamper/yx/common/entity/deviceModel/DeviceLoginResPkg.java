package com.stamper.yx.common.entity.deviceModel;

import com.alibaba.fastjson.annotation.JSONField;
import com.stamper.yx.common.sys.AppConstant;

public class DeviceLoginResPkg {
    @JSONField(name = "Head")
	public MHHead head;

	@JSONField(name = "Body")
    public DeviceLoginRes body;

	@JSONField(name = "Crc")
    public String crc;

    public static DeviceLoginResPkg init(DeviceLoginRes body){
        DeviceLoginResPkg pkg = new DeviceLoginResPkg();
        pkg.setBody(body);
        return pkg;
    }

    public DeviceLoginResPkg(){
        head = new MHHead();
        head.setCmd(AppConstant.DEVICE_LOGIN_RES);
        head.setVersion(AppConstant.MH_VERSION);
        head.setMagic(AppConstant.MH_MAGIC);
        body = new DeviceLoginRes();
    }

    public MHHead getHead() {
        return head;
    }

    public void setHead(MHHead head) {
        this.head = head;
    }

	public DeviceLoginRes getBody() {
		return body;
	}

	public void setBody(DeviceLoginRes body) {
		this.body = body;
	}

	public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    @Override
    public String toString() {
        return "DeviceLoginResPkg{" +
                "head=" + head +
                ", body=" + body +
                ", crc='" + crc + '\'' +
                '}';
    }
}