package com.stamper.yx.common.sys;

public class AppConstant {
	private static PropertiesUtil prop = new PropertiesUtil("host.properties");
	/**
	 * 服务器信息
	 */
	public static final String HOST = prop.getStr("HOST");//当前服务器地址
	public static final String FILE_PATH = System.getProperty("user.dir");//当前服务器地址
	public static final String OPEN_METER = prop.getStr("OPEN_METER");//当前服务器地址
	public static final String METER_UPLOAD_ADDR = prop.getStr("METER_UPLOAD_ADDR");//高拍仪上传地址配置
	public static final String OPEN_MYSQL=prop.getStr("OPEN_MYSQL");//是否开启MYSQL数据源
	public static final String CONFIGIP = prop.getStr("CONFIGIP");//当前配置服务器
	public static final String SVRHOST = prop.getStr("SVRHOST");//当前回调地址
	public static final String SVRIP = prop.getStr("SVRIP");//当前websocket通道地址
	public static final String USER = prop.getStr("USER");//当前管理员账号
	public static final String PASSWORD = prop.getStr("PASSWORD");//当前管理员密码
	public static final String MODULECALLBACK = prop.getStr("MODULECALLBACK");//当前模块回调地址
	public static final String TICKET = prop.getStr("TICKET");//企业凭据
	public static final String USE_COUNT="USE_COUNT";//使用次数同步，盖章通知返回
	public static final String DEVICE_HISTORY_APPLICATION="DEVICE_HISTORY_APPLICATION";//同步历史申请单信息
	//	public static final String DEVICE_REGIST="DEVICE_REGIST";//设备注册
	public static final String DEVICE_LOGIN="DEVICE_LOGIN";//设备登陆
	public static final String NET_STATUS="NET_STATUS";//网络状态
	public static final String LOCATION_INFO="LOCATION_INFO";//同步地址信息
	public static final String DEVICE_LOGOUT="DEVICE_LOGOUT";//设备关机回调

	public static int pit = 1;

	/**
	 * 印章设备协议号
	 */
	public static final int MH_MAGIC = 42949207;
	public static final int MH_VERSION = 1;

	public static final int MHPKG_MAGIC = 0xFFFF4A52;//-46508
	public static final int MHPKG_VERSION = 1;//版本号

	public static final int FP_PERMIT_REQ = 1;// M->H 单片机MCU通知host 指纹认证已通过的请求
	public static final int FP_PERMIT_RES = 2;// H->M host返回MCU 指纹认证通过的返回
	public static final int TAKE_PIC_REQ = 3;// M->H　拍照请求
	public static final int TAKE_PIC_RES = 4;// M->H　拍照返回
	public static final int FP_RECORD_REQ = 5;// H->M　指纹录入请求
	public static final int FP_RECORD_RES = 6;// M->H　指纹录入返回
	public static final int FP_CLEAR_REQ = 7;// H->H　指纹清空的请求
	public static final int FP_CLEAR_RES = 8;// M->H　指纹清空的返回
	public static final int HOST_STATUS_REQ = 9;// M->H 核心板状态的请求
	public static final int HOST_STATUS_RES = 10;// H->M 核心板状态的返回
	public static final int MCU_DEVICE_ID_REQ = 11;// H->M 核心板唯一设备码的请求
	public static final int MCU_DEVICE_ID_RES = 12;// M->H 核心板唯一设备码的返回
	public static final int DEVICE_LOGIN_REQ = 13;// H->S 核心板通过websocket的登录请求
	public static final int DEVICE_LOGIN_RES = 14;// S->H 核心板通过websocket的登录返回
	public static final int DEVICE_ACTIVATE_REQ = 15;// S->H 服务器激活核心板的请求
	public static final int DEVICE_ACTIVATE_RES = 16;// H->S 服务器激活核心板的返回
	public static final int DEVICE_NETWORK_REQ = 17;// H->M 核心板的网络状态更新请求
	public static final int DEVICE_NETWORK_RES = 18;// M->H 核心板的网络状态更新返回

	//app 的
	public static final int IS_AUDIT_REQ = 19;//H->S 设备上传地址坐标信息
	public static final int IS_AUDIT_RES = 20;//

	//device 的
	public static final int APPLICATION_STATUS_REQ = 21;//S -> A 申请单状态推送
	public static final int APPLICATION_STATUS_RES = 22;//S -> A 申请单状态推送

	public static final int DEVICE_REG_REQ = 23;// H->S 核心板通过websocket的注册请求
	public static final int DEVICE_REG_RES = 24;// S->H 核心板通过websocket的注册返回

	public static final int CURRENT_APPLICATION_STATUS_REQ = 25;//M->H 当前申请单状态请求
	public static final int CURRENT_APPLICATION_STATUS_RES = 26;//H->M 当前申请单状态返回

	public static final int ANDROID_POWER_CONNECT_STATUS_REQ = 27;//M->H 充电状态请求
	public static final int ANDROID_POWER_CONNECT_STATUS_RES = 28;//H->M 充电状态返回

	//指令号收到后的通知
	public static final int CMD_ACK_REQ = 29;//M->H 指令号收到后的通知
	public static final int CMD_ACK_RES = 30;//H->M 指令号收到后的通知

	//印章设备信息的通知
	public static final int DEVICE_INFO_REQ = 31;//M->H 印章设备信息的通知请求
	public static final int DEVICE_INFO_RES = 32;//H->M 印章设备信息的通知返回

	//印章保存的申请单列表
	public static final int APPLICATION_LIST_REQ = 33;//M->H 印章保存的申请单列表请求
	public static final int APPLICATION_LIST_RES = 34;//H->M 印章保存的申请单列表返回

	//申请单选择
	public static final int APPLICATION_SELECT_REQ = 35;//M->H 印章保存的申请单列表请求
	public static final int APPLICATION_SELECT_RES = 36;//H->M 印章保存的申请单列表返回

	//用章记录提醒
	public static final int RECORD_NOTICE_REQ = 37;//目前占位
	public static final int RECORD_NOTICE_RES = 38;//S->C 盖章后返回   【关闭wifi的返回】

	//通用APP提醒
	public static final int APP_NOTICE_REQ = 39;//目前占位
	public static final int APP_NOTICE_RES = 40;//S->C 盖章后返回

	//印章mcu重启后给host发的通知
	public static final int DEVICE_REBOOT_REQ = 41;//M->H 印章重启的通知请求
	public static final int DEVICE_REBOOT_RES = 42;//H->M 印章重启的通知返回

	public static final int CURRENT_APPLICATION_CLEAR_REQ = 43;//M->H 当前申请单状态结束请求
	public static final int CURRENT_APPLICATION_CLEAR_RES = 44;//H->M 当前申请单状态结束返回

	public static final int APPLICATION_LIST_CLEAR_REQ = 45; //S->C 清空当前申请单列表请求
	public static final int APPLICATION_LIST_CLEAR_RES = 46; //C->S 清空当前申请单列表返回

	public static final int DEVICE_UNLOCK_REQ = 47; //S->C 指纹无效的情况下，扫描解锁请求
	public static final int DEVICE_UNLOCK_RES = 48; //C->S 指纹无效的情况下，扫描解锁返回

	public static final int SLEEP_CHECK_REQ = 49; //M->H 检查是否可休眠的请求
	public static final int SLEEP_CHECK_RES = 50; //H->M 检查是否可休眠的返回

	public static final int DEVICE_SET_WIFI_REQ = 51; //S->H->M 设置wifi的请求
	public static final int DEVICE_SET_WIFI_RES = 52;//H->S->C 设置wifi的返回

	public static final int APPLICATION_END = 53;//S->H 申请单结束

	public static final int TAKE_AUDIT_PIC_REQ = 55; // M->H　审计拍照请求
	public static final int TAKE_AUDIT_PIC_RES = 56; // H->M　审计拍照返回

	public static final int SEAL_RECORD_INFO_DEL_RES = 57;//S->C 记录上传成功的返回

	public static final int OPEN_BLUETOOTH_REQ = 58; //M->H 单片机通知核心板开启蓝牙
	public static final int OPEN_BLUETOOTH_RES = 59; //H->M 核心板返回给单片机是否成功

	public static final int CLOSE_BLUETOOTH_REQ = 60; //M->H 单片机通知核心板关闭蓝牙
	public static final int CLOSE_BLUETOOTH_RES = 61; //H->M 核心板返回给单片机是否成功

	public static final int WIFI_LIST_REQ = 62; //A->C->H app发送wifi list请求，后台传到核心板
	public static final int WIFI_LIST_RES = 63; //H->C->A 核心板返回后台，返回给app

	public static final int WIFI_INFO_RES = 75; //H->C->A 核心板返回给后台网络信息，返回给APP

	public static final int DEVICE_ERR_REQ = 66; //M->H 硬件故障上传请求
	public static final int DEVICE_ERR_RES = 67; //H->S 硬件故障上传返回

	public static final int DEVICE_USED_REQ = 68;//M->H 印章使用中的通知
	public static final int DEVICE_USED_RES = 69;//H->S 印章使用中的返回

	public static final int HIGH_DEVICE_DEAL_REQ = 75;// S->H 审计指令接收
	public static final int HIGH_DEVICE_DEAL_RES = 76;// S->H 审计指令返回

	public static final int DEVICE_INIT_CLEAR_REQ = 77;//S->H 设备次数清0请求
	public static final int DEVICE_INIT_CLEAR_RES = 78;//S->H 设备次数清0返回

	public static final int DEVICE_LOGGER_FILE_UPDATE_REQ = 79;//S->H 设备日志上传请求

	public static final int SLEEP_TIME_REQ = 80;//设置休眠时间
	public static final int SLEEP_TIME_RES = 81;

	public static final int SLEEP_TIME_RETURN_REQ = 82;
	public static final int SLEEP_TIME_RETURN_RES = 83;//设置休眠时间返回

	public static final int USE_MODEL_REQ = 84;//设置使用模式
	public static final int USE_MODEL_RES = 85;

	public static final int USE_MODEL_RETURN_REQ = 86;
	public static final int USE_MODEL_RETURN_RES = 87;//设置使用模式返回

	public static final int REMOTE_LOCK_REQ = 88;//设置远程锁定
	public static final int REMOTE_LOCK_RES = 89;

	public static final int REMOTE_LOCK_RETURN_REQ = 90;
	public static final int REMOTE_LOCK_RETURN_RES = 91;//设置远程锁定返回

	/**
	 * 文件上传
	 */
	public static final long FILE_MAX_SIZE = 1024 * 1024 * 3;//上传文件允许的最大值3M
	public static final double FILE_SMALL_SCALE = 0.5D;//图片压缩比例
	public static final String FILE_SMALL_PREFIX = "small-";//压缩图片前缀


	/**
	 * token
	 */
	public static final String ACCESSTOKEN_KEY_PRIFIX = "Authorization";

	/**
	 * 分页参数
	 */
	public static final Integer PARAM_PAGENOW = 0;//分页参数：默认当前第一页
	public static final Integer PARAM_PAGESIZE = 10;//分页参数：默认每页10条



	/**
	 * 所有的token 使用 123456的md5摘要做key,此处的md5，会做两次摘要
	 */
	public static final String TOKEN_KEY= "4280d89a5a03f812751f504cc10ee8a5";
	//设备配置信息
	public static final String defaultUUID = "0XFFFFFFFFFFFFFFFFFFFFFFFFF";//默认全局配置的uuid值
}
