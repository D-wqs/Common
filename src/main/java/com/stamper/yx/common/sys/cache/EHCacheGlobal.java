package com.stamper.yx.common.sys.cache;

public class EHCacheGlobal {
    //cacheName容器名
    public static final String USER_TOKEN_KEY="USER:TOEKN:";//示例：USER:TOKEN:${userId}
    public static final long USER_TOKEN_TIMEOUT=1000*60*60*12;

    public static final String DEVICE_WIFI_LIST="DEVICE:WIFI:LIST:";//示例：DEVICE:WIFI:LIST:${signetId}
    public static final long DEVICE_WIFI_LIST_TIMEOUT=1000*60*30;
    //signet aesKey
    public static final  String SIGNET_AESKEY="SIGNET:AESKEY:";//示例：SIGNET:AESKEY:${signetId}
    //accessToken
    public static final String YUNXI_ACCESSTOKEN="YUNXI_ACCESSTOKEN";
    /**
     * 元素最大数量
     */

    public static int MAXELEMENTSINMEMORY = 50000;

    /**
     *
     * 是否把溢出数据持久化到硬盘
     */

    public static boolean OVERFLOWTODISK = true;

    /**
     *
     * 是否会死亡:
     * TODO 不死亡
     */

    public static boolean ETERNAL = true;

    /**
     *
     * 缓存的间歇时间
     */

    public static int TIMETOIDLESECONDS = 600;

    /**
     *
     * 存活时间(默认一天)
     */

    public static int TIMETOlIVESECONDS = 86400;

    /**
     *
     * 需要持久化到硬盘否
     */

    public static boolean DISKPERSISTENT = false;

    /**
     *
     * 内存存取策略
     */

    public static String MEMORYSTOREEVICTIONPOLICY = "LFU";
}
