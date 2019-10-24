package com.stamper.yx.common.sys.mq;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/7/16 0016 17:12
 */
public class MqGlobal {
	public static final String exchange_to_signet = "to.signetCommon";//发往印章通用交换机
	public static final String queue_signet_to_use = "signet.to.use";

	//命令码
	// 0：通知设备端上传日志
	public static final int SIGNET_UPLOAD_LOG = 0;
	// 1：申请单推送
	public static final int SIGNET_APPLICATION_PUSH = 1;
	// 2：申请单结束
	public static final int SIGNET_APPLICATION_END = 2;
	// 3：指纹录入
	public static final int SIGNET_FINGER_ADD = 3;
	// 4：指纹删除
	public static final int SIGNET_FINGER_DEL = 4;
	// 5：指纹清空
	public static final int SIGNET_FINGER_CLEAN = 5;
	// 6：wifi列表获取
	public static final int SIGNET_WIFI_LIST = 6;
	// 7：WiFi链接
	public static final int SIGNET_WIFI_LINK = 7;
	// 8：WiFi断开
	public static final int SIGNET_WIFI_CLOSE = 8;
	// 9：手动解锁
	public static final int SIGNET_UNLOCK = 9;
	// 10:通知高拍仪拍照
	public static final int SINGET_NOTICE_METER = 10;
	// 11:设备清次(初始化)
	public static final int SIGNET_INIT = 11;
	// 12:远程锁定
	public static final int SIGNET_REMOTE_LOCK = 12;
	// 13:设置休眠时间
	public static final int SIGNET_SET_SLEEP_TIMES = 13;
	// 14:开启/关闭指纹模式
	public static final int SIGNET_OPEN_OR_CLOSE_FINGER_PATTERN = 14;
	// 15:设备迁移
	public static final int SIGNET_MIGRATE = 15;

}
