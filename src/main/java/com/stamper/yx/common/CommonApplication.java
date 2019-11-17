package com.stamper.yx.common;

import com.stamper.yx.common.controller.DeviceWebSocket;
import com.stamper.yx.common.entity.Config;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.User;
import com.stamper.yx.common.service.ConfigService;
import com.stamper.yx.common.service.DeviceWebSocketService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.service.UserService;
import com.stamper.yx.common.service.mysql.MysqlSignetService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.cache.EHCacheUtil;
import com.stamper.yx.common.sys.error.PrintException;
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
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.MultipartConfigElement;
import java.io.File;
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
    @Autowired
    private MysqlSignetService mysqlSignetService;

    @Override
    public void run(String... args) throws Exception {
        log.info("------------安徽云玺----------");
        log.info("TJ分支");
        //初始化websocket
        DeviceWebSocket.service = service;
        DefaultWebSocket.okHttpCli = okHttpCli;//注入okHttpCli,发送关机指令

        //管理员信息中包含  用户名+密码+模块回调地址+接口ticket
        String AdminName = AppConstant.USER;
        String password = AppConstant.PASSWORD;
        String modulecallback = AppConstant.MODULECALLBACK;

        String configip = AppConstant.CONFIGIP;
        String svrhost = AppConstant.SVRHOST;
        String svrip = AppConstant.SVRIP;
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
        userService.save(user);
        log.info("初始化用户名{{}},模块回调地址{{}}", user.getName(), user.getCallbackUrl());

        //初始化全局配置信息
        Config config = new Config();
        config.setUuid(AppConstant.defaultUUID);
        config.setConfigIp(configip);//配置IP[用于获取所有配置参数]
        config.setSvrHost(svrhost);//配置服务IP[回调]
        config.setSvrIp(svrip);//配置通道地址
        configService.save(config);
        log.info("----------初始化设备全局配置完成---------");

        //初始化测试设备,以后设备id从1000以后开始
        Signet signet = new Signet();
        signet.setId(1000);//初始化设备的id，从1000开始
        signet.setUuid(AppConstant.defaultUUID);
        signet.setNetType("4G");
        signet.setStatus(0);//印章状态: 0:正常 1:异常 2:销毁 3:停用 4:锁定
        signet.setName("测试章");

        signetService.save(signet);

        //mysql 数据源同步数据
        String openMysql = AppConstant.OPEN_MYSQL;
        if (openMysql.equalsIgnoreCase("false")) {
            mysqlSignetService = null;
            log.info("*****停止mysql数据源的使用*****");
        }
        if (mysqlSignetService != null) {
            mysqlSignetService.save(signet);
        }
        log.info("----------初始化测试设备完成---------");

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

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(102400000);
        factory.setMaxRequestSize(102400000);
        String location = System.getProperty("user.dir") + File.separator + "upload";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
