package com.stamper.yx.common.entity;

import java.io.Serializable;

public class Config implements Serializable {
    private Integer id;

    /**
     * 设备uuid
     */
    private String uuid;

    /**
     * 0:默认配置(仅只有1个)  1:印章配置  2:高拍仪配置
     */
    private Integer type;
    /**
     * 后台版本标记 0:旧版本后台  1:新版本后台
     */
    private Integer status;
    /**
     * 量子配置id
     */
    private String qssPin;

    private String qssQkud;

    private String qssQssc;

    /**
     * wifi名称
     */
    private String wifiSsid;

    /**
     * WiFi密码
     */
    private String wifiPwd;

    /**
     * 配置服务器ip 如:http://117.50.76.172:8080/
     */
    private String configIp;

    /**
     * 业务服务器host 如:117.50.76.172:8080/device
     */
    private String svrHost;

    /**
     * 业务服务器ip 如:117.50.76.172:8080
     */
    private String svrIp;

    /**
     * 当前版本号
     */
    private String version;

    /**
     * APK文件名称
     */
    private String apkName;

    /**
     * 当前版本更新地址
     */
    private String versionUrl;

    private String createDate;
    private String updateDate;
    private String deleteDate;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取设备uuid
     *
     * @return uuid - 设备uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置设备uuid
     *
     * @param uuid 设备uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取0:默认配置(仅只有1个)  1:印章配置  2:高拍仪配置
     *
     * @return type - 0:默认配置(仅只有1个)  1:印章配置  2:高拍仪配置
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置0:默认配置(仅只有1个)  1:印章配置  2:高拍仪配置
     *
     * @param type 0:默认配置(仅只有1个)  1:印章配置  2:高拍仪配置
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取量子配置id
     *
     * @return qss_pin - 量子配置id
     */
    public String getQssPin() {
        return qssPin;
    }

    /**
     * 设置量子配置id
     *
     * @param qssPin 量子配置id
     */
    public void setQssPin(String qssPin) {
        this.qssPin = qssPin;
    }

    /**
     * @return qss_qkud
     */
    public String getQssQkud() {
        return qssQkud;
    }

    /**
     * @param qssQkud
     */
    public void setQssQkud(String qssQkud) {
        this.qssQkud = qssQkud;
    }

    /**
     * @return qss_qssc
     */
    public String getQssQssc() {
        return qssQssc;
    }

    /**
     * @param qssQssc
     */
    public void setQssQssc(String qssQssc) {
        this.qssQssc = qssQssc;
    }

    /**
     * 获取wifi名称
     *
     * @return wifi_ssid - wifi名称
     */
    public String getWifiSsid() {
        return wifiSsid;
    }

    /**
     * 设置wifi名称
     *
     * @param wifiSsid wifi名称
     */
    public void setWifiSsid(String wifiSsid) {
        this.wifiSsid = wifiSsid;
    }

    /**
     * 获取WiFi密码
     *
     * @return wifi_pwd - WiFi密码
     */
    public String getWifiPwd() {
        return wifiPwd;
    }

    /**
     * 设置WiFi密码
     *
     * @param wifiPwd WiFi密码
     */
    public void setWifiPwd(String wifiPwd) {
        this.wifiPwd = wifiPwd;
    }

    /**
     * 获取配置服务器ip 如:http://117.50.76.172:8080/
     *
     * @return config_ip - 配置服务器ip 如:http://117.50.76.172:8080/
     */
    public String getConfigIp() {
        return configIp;
    }

    /**
     * 设置配置服务器ip 如:http://117.50.76.172:8080/
     *
     * @param configIp 配置服务器ip 如:http://117.50.76.172:8080/
     */
    public void setConfigIp(String configIp) {
        this.configIp = configIp;
    }

    /**
     * 获取业务服务器host 如:117.50.76.172:8080/device
     *
     * @return svr_host - 业务服务器host 如:117.50.76.172:8080/device
     */
    public String getSvrHost() {
        return svrHost;
    }

    /**
     * 设置业务服务器host 如:117.50.76.172:8080/device
     *
     * @param svrHost 业务服务器host 如:117.50.76.172:8080/device
     */
    public void setSvrHost(String svrHost) {
        this.svrHost = svrHost;
    }

    /**
     * 获取业务服务器ip 如:117.50.76.172:8080
     *
     * @return svr_ip - 业务服务器ip 如:117.50.76.172:8080
     */
    public String getSvrIp() {
        return svrIp;
    }

    /**
     * 设置业务服务器ip 如:117.50.76.172:8080
     *
     * @param svrIp 业务服务器ip 如:117.50.76.172:8080
     */
    public void setSvrIp(String svrIp) {
        this.svrIp = svrIp;
    }

    /**
     * 获取当前版本号
     *
     * @return version - 当前版本号
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置当前版本号
     *
     * @param version 当前版本号
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 获取APK文件名称
     *
     * @return apk_name - APK文件名称
     */
    public String getApkName() {
        return apkName;
    }

    /**
     * 设置APK文件名称
     *
     * @param apkName APK文件名称
     */
    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    /**
     * 获取当前版本更新地址
     *
     * @return version_url - 当前版本更新地址
     */
    public String getVersionUrl() {
        return versionUrl;
    }

    /**
     * 设置当前版本更新地址
     *
     * @param versionUrl 当前版本更新地址
     */
    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uuid=").append(uuid);
        sb.append(", type=").append(type);
        sb.append(", qssPin=").append(qssPin);
        sb.append(", qssQkud=").append(qssQkud);
        sb.append(", qssQssc=").append(qssQssc);
        sb.append(", wifiSsid=").append(wifiSsid);
        sb.append(", wifiPwd=").append(wifiPwd);
        sb.append(", configIp=").append(configIp);
        sb.append(", svrHost=").append(svrHost);
        sb.append(", svrIp=").append(svrIp);
        sb.append(", version=").append(version);
        sb.append(", apkName=").append(apkName);
        sb.append(", versionUrl=").append(versionUrl);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", deleteDate=").append(deleteDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {

    }
}