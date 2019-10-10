package com.stamper.yx.common.entity.deviceModel;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/1/14 0014 17:18
 */
public class DeviceLoginInfo{
	@JSONField(name = "Stm32UUID")
	public String stm32UUID;

	@JSONField(name = "DeviceID")
	public int deviceID;

	@JSONField(name = "IMEI")
	public String iMEI;//imei　码

	@JSONField(name = "Tel")
	public String tel;//卡的手机号

	@JSONField(name = "ICCID")
	public String iCCID;//卡的iccid

	@JSONField(name = "IMSI")
	public String iMSI;//卡的imsi号

	@JSONField(name = "Addr")
	public String addr;//定位到的地址

	@JSONField(name = "AddrPoi")
	public String addrPoi;//其他可能的地点

	@JSONField(name = "UseCount")
	public int useCount;

	@JSONField(name = "UserID")
	public int userID;

	@JSONField(name = "UserName")
	public String userName;

	@JSONField(name = "SimNum")
	public String simNum;//电话卡

	@JSONField(name = "DeviceType")
	public int deviceType;//设备类型 0:印章 1:高拍仪

	@JSONField(name = "Latitude")
	public String latitude;//纬度

	@JSONField(name = "Longitude")
	public String longitude;//经度

	@JSONField(name = "LocationDescribe")
	public String locationDescribe;//位置描述

	@JSONField(name = "UsedStatus")
	public Integer usedStatus = 0;//0:非使用中  1:使用中

	public String symmetricKey;

	public String getSymmetricKey() {
		return symmetricKey;
	}

	public void setSymmetricKey(String symmetricKey) {
		this.symmetricKey = symmetricKey;
	}

	private List<LoginApplication> loginApplicationInfo;//印章最近5次申请单已使用记录

	public List<LoginApplication> getLoginApplicationInfo() {
		return loginApplicationInfo;
	}

	public void setLoginApplicationInfo(List<LoginApplication> loginApplicationInfo) {
		this.loginApplicationInfo = loginApplicationInfo;
	}

	public Integer getUsedStatus() {
		return usedStatus;
	}

	public void setUsedStatus(Integer usedStatus) {
		this.usedStatus = usedStatus;
	}

	public String getSimNum() {
		return simNum;
	}

	public void setSimNum(String simNum) {
		this.simNum = simNum;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocationDescribe() {
		return locationDescribe;
	}

	public void setLocationDescribe(String locationDescribe) {
		this.locationDescribe = locationDescribe;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public String getStm32UUID() {
		return stm32UUID;
	}

	public void setStm32UUID(String stm32UUID) {
		this.stm32UUID = stm32UUID;
	}

	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public String getiMEI() {
		return iMEI;
	}

	public void setiMEI(String iMEI) {
		this.iMEI = iMEI;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getiCCID() {
		return iCCID;
	}

	public void setiCCID(String iCCID) {
		this.iCCID = iCCID;
	}

	public String getiMSI() {
		return iMSI;
	}

	public void setiMSI(String iMSI) {
		this.iMSI = iMSI;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddrPoi() {
		return addrPoi;
	}

	public void setAddrPoi(String addrPoi) {
		this.addrPoi = addrPoi;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("DeviceLoginInfo{");
		sb.append("stm32UUID='").append(stm32UUID).append('\'');
		sb.append(", deviceID=").append(deviceID);
		sb.append(", iMEI='").append(iMEI).append('\'');
		sb.append(", tel='").append(tel).append('\'');
		sb.append(", iCCID='").append(iCCID).append('\'');
		sb.append(", iMSI='").append(iMSI).append('\'');
		sb.append(", addr='").append(addr).append('\'');
		sb.append(", addrPoi='").append(addrPoi).append('\'');
		sb.append(", useCount=").append(useCount);
		sb.append(", userID=").append(userID);
		sb.append(", userName='").append(userName).append('\'');
		sb.append(", simNum='").append(simNum).append('\'');
		sb.append(", deviceType=").append(deviceType);
		sb.append(", latitude='").append(latitude).append('\'');
		sb.append(", longitude='").append(longitude).append('\'');
		sb.append(", locationDescribe='").append(locationDescribe).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
