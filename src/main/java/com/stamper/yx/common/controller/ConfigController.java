package com.stamper.yx.common.controller;

import com.stamper.yx.common.entity.Config;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.entity.deviceModel.UpdateAPK;
import com.stamper.yx.common.service.ConfigService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.context.SpringContextUtils;
import com.stamper.yx.common.sys.dir.DirFileUtils;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
    public ResultVO addConfigError(MultipartFile fileupload,String uuid) {
        if (fileupload.isEmpty()){
            log.error("【日志文件】上传失败");
            return ResultVO.FAIL("上传失败");
        }
        if(StringUtils.isBlank(uuid)){
            log.error("【日志上传】uuid位空");
            return ResultVO.FAIL("uuid为空");
        }
        Signet byUUID = signetService.getByUUID(uuid);
        if(byUUID==null){
            log.error("【日志上传】通过uuid没有找到设备");
            return ResultVO.FAIL("当前uuid对应的设备不存在");
        }
        String filePath = AppConstant.FILE_PATH;
        String filePathV2 = DirFileUtils.getFilePathV2(uuid);//路径地址：uuid/年/月/日
        String realpath =  "upload" + filePathV2;//拼接文件最终所在磁盘地址
        //此处处理盖章记录文件
        String absoultPath = filePath + File.separator +realpath;
        File path = new File(absoultPath);
        //创建父目录文件夹
        if (!path.exists()) {
            //文件不存在？创建文件目录
            path.mkdirs();
        }
        //文件名：
        String originalFilename = fileupload.getOriginalFilename();
        String name = StringUtils.isNotBlank(originalFilename) ? originalFilename : System.currentTimeMillis() + "";
        log.info("【开机后】设备日志上传:,deviceId:{{}},filename{{}},size:{}",byUUID.getId(),name,fileupload.getSize());
        File dest = new File(absoultPath + name);
        try {
            fileupload.transferTo(dest);
            log.info("【开机后】设备日志上传:,deviceId:{{}},filename{{}},size:{}",byUUID.getId(),name,fileupload.getSize());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【日志上传】失败",e);
        }
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
