package com.stamper.yx.common.controller;

import com.stamper.yx.common.entity.Config;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.deviceModel.UpdateAPK;
import com.stamper.yx.common.service.ConfigService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.sys.context.SpringContextUtils;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/device/config", method = {RequestMethod.POST, RequestMethod.GET})
public class ConfigController {
    @Autowired
    private ConfigService service;
    @Autowired
    private SignetService signetService;

    /**
     * 查询指定uuid的设备配置信息(设备专用)
     */
    @RequestMapping("/getConfigByUUID")
    @Transactional
    public ResultVO getConfigByUUID(@RequestParam(value = "uuid", required = false) String uuid) {
        if (StringUtils.isNotBlank(uuid)) {
            Config config = service.getByUUID(uuid);
            String svrHost = config.getSvrHost();
            log.info("配置回调：：：：",svrHost);
            System.out.println("当前设备参数："+config.toString());
            if (config == null) {
                //查询默认配置
                config = service.getDefaultConfig();

                //查询印章是否存在
                Signet signet = signetService.getByUUID(uuid);
                if (signet == null) {
                    //创建新印章
                    signet = new Signet();
                    signet.setUuid(uuid);
                    signet.setCount(0);
                    signetService.add(signet);

                    signet.setName("印章(新" + signet.getId() + ")");
                    signetService.update(signet);
                }

                //创建新配置信息
                config.setId(null);
                config.setUuid(uuid);
                service.insert(config);
            }

            Map<String, Object> configMap = new HashMap<>();
            configMap.put("id", config.getId());
            configMap.put("uuid", config.getUuid());
            configMap.put("type", config.getType());
            configMap.put("status", config.getStatus());
            configMap.put("qssPin", config.getQssPin());
            configMap.put("qssQkud", config.getQssQkud());
            configMap.put("qssQssc", config.getQssQssc());
            configMap.put("wifiSsid", config.getWifiSsid());
            configMap.put("wifiPwd", config.getWifiPwd());
            configMap.put("configIp", config.getConfigIp());
            configMap.put("svrHost", config.getSvrHost());
            configMap.put("svrIp", config.getSvrIp());
            configMap.put("version", config.getVersion());
            configMap.put("apkName", config.getApkName());
            configMap.put("versionUrl", config.getVersionUrl());

            //给印章拼接好 `获取配置服务器接口`，`更新APK接口`，`上传错误日志接口` 3个接口
            configMap.put("deviceConfigIp", config.getConfigIp() + "/device/config/getConfigByUUID");
            configMap.put("updateApkIp", config.getConfigIp() + "/device/config/updateAPK");
            configMap.put("errorLogIp", config.getConfigIp() + "/device/config/addConfigError");
            log.info("---------------->印章获取配置信息{{}}",config.getConfigIp());
            return ResultVO.OK(configMap);
        }
        return ResultVO.FAIL("UUID不能为空");
    }
    /**
     * 设备日志上传
     */
    @RequestMapping("/addConfigError")
    public ResultVO addConfigError(MultipartFile fileupload) {
        HttpServletRequest request = SpringContextUtils.getRequest();
        Map<String, String[]> params = request.getParameterMap();
        String uuid = null;
        try {
            uuid = params.get("uuid")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("【开机后】设备日志上传{{}}",uuid);

//        ConfigError configError = new ConfigError();
//        //保存日志
//        if (fileupload != null) {
//            FileEntity entity = FileUtil.update(fileupload, "configLogs");
//            if (entity != null) {
//                configError.setFileName(entity.getFileName());
//                configError.setAbsolutePath(entity.getAbsolutePath());
//                configError.setRelativePath(entity.getRelativePath());
//                configError.setError("日志上传成功");
//            } else {
//                configError.setError("保存日志文件失败");
//            }
//        } else {
//            configError.setError("无日志文件");
//        }
//
//        configError.setUuid(uuid);
//        configErrorService.add(configError);

        return ResultVO.OK();
    }

    /**
     * 更新APK(设备端专用)
     */
    @RequestMapping("/updateAPK")
    public UpdateAPK updateAPK(@RequestParam(value = "uuid") String uuid) {
        log.info("【开机后】{{}}：更新apk",uuid);
        UpdateAPK apk = new UpdateAPK();
//        Config config = service.getByUUID(uuid);
//        if (config == null) {
//            //uuid不存在,或不存在该uuid的配置,则返回全局默认配置
//            config = service.getDefaultConfig();
//        }
//
//        String version = config.getVersion();
//        String versionUrl = config.getVersionUrl();
//        if (StringUtil.isNoneBlank(version, versionUrl)) {
//            try {
//                apk.setVersion(Float.parseFloat(config.getVersion()));
//            } catch (NumberFormatException e) {
//            }
//            apk.setUrl(config.getVersionUrl());
//        }
//
//        log.info("设备UUID:{{}} 更新APK:{{}}", uuid, apk);
        return apk;
    }

    @RequestMapping(value = "/updateConfigByUUID",method = RequestMethod.POST)
    public ResultVO updateConfig(Config config,String uuid){
        if(config==null || StringUtils.isBlank(uuid)){
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        //TODO 修改全局配置
        String configIp = config.getConfigIp();
        String svrHost = config.getSvrHost();
        String svrIp = config.getSvrIp();
        String wifiSsid = config.getWifiSsid();
        String wifiPwd = config.getWifiPwd();

        if(StringUtils.isAnyBlank(configIp,svrHost,svrIp)){
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }

        Config byUUID = service.getByUUID(uuid);
        if(byUUID==null){
            log.info("{{}}对应的配置不存在",uuid);
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        byUUID.setConfigIp(configIp);
        byUUID.setSvrIp(svrIp);
        byUUID.setSvrHost(svrHost);
        byUUID.setWifiPwd(wifiPwd);
        byUUID.setWifiSsid(wifiSsid);
        service.update(byUUID);
        return ResultVO.OK();
    }
}
