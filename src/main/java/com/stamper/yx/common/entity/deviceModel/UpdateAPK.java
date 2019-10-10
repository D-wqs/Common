package com.stamper.yx.common.entity.deviceModel;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/7/2 0002 15:01
 */
public class UpdateAPK {
	private float version;//最新版本号
	private String url;//更新该版本的URL地址

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("UpdateAPK{");
		sb.append("version=").append(version);
		sb.append(", url='").append(url).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
