package com.stamper.yx.common;

import com.stamper.yx.common.controller.DeviceWebSocket;
import com.stamper.yx.common.entity.Config;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.User;
import com.stamper.yx.common.service.ConfigService;
import com.stamper.yx.common.service.DeviceWebSocketService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.service.UserService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.cache.EHCacheUtil;
import com.stamper.yx.common.sys.md5.MD5;
import com.stamper.yx.common.sys.okhttpUtil.OkHttpCli;
import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerUtils;

import java.util.UUID;

@MapperScan("com.stamper.yx.common.mapper")
@SpringBootApplication
@EnableCaching
@Slf4j
public class CommonApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }

    @Autowired
    private DeviceWebSocketService service;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private SignetService signetService;
    @Autowired
    private OkHttpCli okHttpCli;

    @Override
    public void run(String... args) throws Exception {
        log.info("------------安徽云玺----------");
        String AdminName = AppConstant.USER;
        String password = AppConstant.PASSWORD;
        String configip = AppConstant.CONFIGIP;
        String svrhost = AppConstant.SVRHOST;
        String svrip = AppConstant.SVRIP;
        String modulecallback = AppConstant.MODULECALLBACK;
        DeviceWebSocket.service = service;
        DefaultWebSocket.okHttpCli = okHttpCli;//注入okHttpCli,发送关机指令
        //初始化admin信息
        User user = new User();
        user.setName(AdminName);
        user.setPassword(MD5.toMD5(password));
        user.setCallbackUrl(modulecallback);
        //todo 假如配置文件声明了该值，就不需要重新定义，直接使用固定值
        String ticket = AppConstant.TICKET;
        if (StringUtils.isBlank(ticket)) {
            // 配置企业凭证 MD5(uuid);
            String s1 = UUID.randomUUID().toString();
            String replace = s1.replace("-", "");
            ticket = MD5.toMD5(replace);
            log.info("ticket未设置，自动生成：{{}}", ticket);
        } else {
            log.info("ticket已设置，不需请求此值：{{}}", ticket);
        }
        user.setAccesstoken(ticket);

        User admin = userService.getUser(AdminName);
        if (admin == null) {
            userService.add(user);
        } else {
            admin.setCallbackUrl(user.getCallbackUrl());
            admin.setPassword(user.getPassword());
            admin.setAccesstoken(user.getAccesstoken());
            userService.update(admin);
        }
        log.info("初始化用户名{{}},模块回调地址{{}}", user.getName(), user.getCallbackUrl());
        //初始化测试设备
        Signet signet = new Signet();
        signet.setId(1000);//初始化设备的id，从1000开始
        signet.setUuid(AppConstant.defaultUUID);
        signet.setNetType("4G");
        signet.setSleepTime(2);
        signet.setName("测试章");
        Signet byUUID = signetService.getByUUID(AppConstant.defaultUUID);
        if (byUUID == null) {
            signetService.add(signet);
        } else {
            signetService.update(byUUID);
        }
        log.info("----------初始化测试设备完成---------");
        //初始化全局配置信息
        Config config = new Config();
        config.setUuid(AppConstant.defaultUUID);
        config.setConfigIp(configip);//配置IP[用于获取所有配置参数]
        config.setSvrHost(svrhost);//配置服务IP[回调]
        config.setSvrIp(svrip);//配置通道地址
        Config defaultConfig = configService.getDefaultConfig();
        if (defaultConfig == null) {
            configService.insert(config);
        } else {
            defaultConfig.setConfigIp(configip);//配置IP[用于获取所有配置参数]
            defaultConfig.setSvrHost(svrhost);//配置服务IP[回调]
            defaultConfig.setSvrIp(svrip);//配置通道地址
            configService.update(defaultConfig);
        }
        log.info("----------初始化设备全局配置完成---------");
        CacheManager cacheManager = EhCacheManagerUtils.buildCacheManager("ehCacheCacheManager");
        EHCacheUtil.setCacheManager(cacheManager);
        EHCacheUtil.initCache("user");
//        EHCacheUtil.put("as","demo");
//        Object as = EHCacheUtil.get("as");
//        log.info("缓存{{}}",as);
        //注入
        log.info("------------缓存加载完毕---------");
        log.info("------------运行完毕----------");
    }
}
