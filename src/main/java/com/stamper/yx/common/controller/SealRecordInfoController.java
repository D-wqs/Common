package com.stamper.yx.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.entity.FileInfo;
import com.stamper.yx.common.entity.SealRecordInfo;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.mapper.mysql.MySealRecordInfoMapper;
import com.stamper.yx.common.mapper.mysql.MysqlFileInfoMapper;
import com.stamper.yx.common.service.mysql.MysqlFileInfoService;
import com.stamper.yx.common.service.mysql.MysqlSealRecordInfoService;
import com.stamper.yx.common.service.mysql.MysqlSignetService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.dir.DirFileUtils;
import com.stamper.yx.common.sys.okhttpUtil.OkHttpCli;
import com.stamper.yx.common.sys.response.ResultVO;
import com.stamper.yx.common.sys.security.AES.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = {"/device/sealRecordInfo"}, method = RequestMethod.POST)
public class SealRecordInfoController {
    @Autowired
    private OkHttpCli okHttpCli;

    @Autowired
    private MysqlSignetService mysqlSignetService;
    @Autowired
    private MysqlSealRecordInfoService mysqlSealRecordInfoService;
    @Autowired
    private MysqlFileInfoService mysqlFileInfoService;

    /**
     * 指纹模式上传
     * 0:成功  1:失败  2:图片重复 3:出错
     *
     * @param sealRecordInfo
     * @return
     */
    @RequestMapping(value = "addEasyInfo")
    public String addEasyInfo(SealRecordInfo sealRecordInfo) {
        log.info("来自指纹模式的记录上传----------【从请求参数中获取fileupload参数，进行对称解密，获取图片详情】");
        //获取图片密文
        String fileupload = sealRecordInfo.getFileupload();
        //通过sealRecordInfo获取设备id
        Integer deviceID = sealRecordInfo.getDeviceID();
        //todo 通过设备id获取AesKey对文件密文解密
        //这里调用获取aesKey的接口
        String aesKey = getAesKey(deviceID);
        //通过对密文解密，将密文使用base64转码，获取文件信息，生成文件
        String decrypt = null;
        try {
            decrypt = AesUtil.decrypt(fileupload, aesKey);
            byte[] bytes = new BASE64Decoder().decodeBuffer(decrypt);
            //todo 生成本地文件
            //将使用记录存入数据库
            if(AppConstant.OPEN_MYSQL.equalsIgnoreCase("false")){
                log.info("数据源已关闭，不能插入使用记录。请开启mysql数据源");
                return "1";
            }
            //todo 检查当前记录是否已存在？
            int insert = mysqlSealRecordInfoService.addOrUpdate(sealRecordInfo);
            if(insert!=1){
                log.info("记录插入失败，返回1，通知设备重新上传");
                return "1";
            }
            FileInfo fileInfo = fileUpload(deviceID, bytes, null, sealRecordInfo.getId());
            if(fileInfo==null){
                log.info("fileInfo信息写入数据库失败，通知设备重新上传");
                return "1";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //回调成功：服务端返回给设备：【0:成功】、【1:失败】、【2:图片重复】、【3:出错】
        //假如返回1和3，设备会认为记录未同步，继续上传记录信息到此接口
        return "0";
    }

    /**
     * 申请单模式上传
     * 0:成功  1:失败  2:图片重复 3:出错
     *
     * @param sealRecordInfo
     * @return
     */
    @RequestMapping(value = "addNormalInfo")
    public String addNormalInfo(SealRecordInfo sealRecordInfo) {
        log.info("来自申请单模式的记录上传----------【从请求参数中获取fileupload参数，进行对称解密，获取图片详情】");
        //获取图片密文
        String fileupload = sealRecordInfo.getFileupload();
        //通过sealRecordInfo获取设备id
        Integer deviceID = sealRecordInfo.getDeviceID();
        //todo 通过设备id获取AesKey对文件密文解密
        //这里调用获取aesKey的接口
        String aesKey = getAesKey(deviceID);
        //通过对密文解密，将密文使用base64转码，获取文件信息，生成文件
        String decrypt = null;
        try {
            decrypt = AesUtil.decrypt(fileupload, aesKey);
            byte[] bytes = new BASE64Decoder().decodeBuffer(decrypt);
            //todo 生成本地文件
            //将使用记录存入数据库
            if(AppConstant.OPEN_MYSQL.equalsIgnoreCase("false")){
                log.info("数据源已关闭，不能插入使用记录。请开启mysql数据源");
                return "1";
            }
            //todo 检查当前记录是否已存在？
            int insert = mysqlSealRecordInfoService.addOrUpdate(sealRecordInfo);
            if(insert!=1){
                log.info("记录插入失败，返回1，通知设备重新上传");
                return "1";
            }
            FileInfo fileInfo = fileUpload(deviceID, bytes, null, sealRecordInfo.getId());
            if(fileInfo==null){
                log.info("fileInfo信息写入数据库失败，通知设备重新上传");
                return "1";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //回调成功：服务端返回给设备：【0:成功】、【1:失败】、【2:图片重复】、【3:出错】
        //假如返回1和3，设备会认为记录未同步，继续上传记录信息到此接口
        return "0";
    }

    /**
     * 审计上传
     *
     * @param sealRecordInfo
     * @return
     */
    @RequestMapping(value = "addAuditInfo")
    public String addAuditInfo(SealRecordInfo sealRecordInfo) {
        log.info("来自审计的记录上传----------【从请求参数中获取fileupload参数，进行对称解密，获取图片详情】");
        //获取图片密文
        String fileupload = sealRecordInfo.getFileupload();
        //通过sealRecordInfo获取设备id
        Integer deviceID = sealRecordInfo.getDeviceID();
        //todo 通过设备id获取AesKey对文件密文解密
        //这里调用获取aesKey的接口
        String aesKey = getAesKey(deviceID);
        //通过对密文解密，将密文使用base64转码，获取文件信息，生成文件
        String decrypt = null;
        try {
            decrypt = AesUtil.decrypt(fileupload, aesKey);
            byte[] bytes = new BASE64Decoder().decodeBuffer(decrypt);
            //todo 生成本地文件
            //将使用记录存入数据库
            if(AppConstant.OPEN_MYSQL.equalsIgnoreCase("false")){
                log.info("数据源已关闭，不能插入使用记录。请开启mysql数据源");
                return "1";
            }
            //todo 检查当前记录是否已存在？
            int insert = mysqlSealRecordInfoService.addOrUpdate(sealRecordInfo);
            if(insert!=1){
                log.info("记录插入失败，返回1，通知设备重新上传");
                return "1";
            }
            FileInfo fileInfo = fileUpload(deviceID, bytes, null, sealRecordInfo.getId());
            if(fileInfo==null){
                log.info("fileInfo信息写入数据库失败，通知设备重新上传");
                return "1";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //回调成功：服务端返回给设备：【0:成功】、【1:失败】、【2:图片重复】、【3:出错】
        //假如返回1和3，设备会认为记录未同步，继续上传记录信息到此接口
        return "0";
    }

    //获取aesKey
    public String getAesKey(Integer deviceId) {
        //TODO 获取ticket,每次运行会打印在控制台，除非重启否则不会改变
        //请求参数：name，password
        //请求地址：url:http://ip:port/user/getTicket【无需校验请求头】
        Map<String, String> ticket = new HashMap<>();
        ticket.put("name", "admin");
        ticket.put("password", "admin_123456");
        String s2 = okHttpCli.doPost("http://localhost:8888/user/getTicket", ticket);

        //TODO 获取token，获取密钥
        //请求参数：ticket
        //请求地址：url:http://ip:port/device/getAccessToken【无需校验请求头】
        Map<String, String> getToken = new HashMap<>();
        getToken.put("ticket", s2);
        String s1 = okHttpCli.doPost("http://localhost:8888/device/getAccessToken", getToken);
        ResultVO resultVO = JSONObject.parseObject(s1, ResultVO.class);
        Object token = resultVO.getData();
        //TODO 获取对称密钥aesKey
        //请求参数：deviceId
        //请求地址：url:http://ip:port/device/getAesKey
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", deviceId + "");
        String s = okHttpCli.doPostWithHeader("http://localhost:8888/device/getAesKey", map, token.toString());//添加请求头：Authorization
        ResultVO aesData = JSONObject.parseObject(s, ResultVO.class);
        Object aesKey = aesData.getData();
        log.info("获取到的返回值{{}}", aesKey.toString());
        return aesKey.toString();
    }

    /**
     * 根据设备id关联的组织id做目录隔离
     *
     * @param deviceId
     * @param fileData
     * @param fileName
     */
    public FileInfo fileUpload(Integer deviceId, byte[] fileData, String fileName,Integer sealInfoId) {
        //用组织id做目录隔离
        String corpId = "";
        //todo 存入mysql
        String openMysql = AppConstant.OPEN_MYSQL;
        if (StringUtils.isBlank(openMysql) || openMysql.equalsIgnoreCase("false")) {
            mysqlSignetService = null;
        }
        if (mysqlSignetService != null) {
            Signet byId = mysqlSignetService.getById(deviceId);
            corpId = byId.getCorpId();
        }
        String filePath = AppConstant.FILE_PATH;
        String filePathV2 = DirFileUtils.getFilePathV2(corpId);//路径地址：CorpId/年/月/日
        String realpath = filePath.replaceAll("/", File.separator) + filePathV2;//拼接文件最终所在磁盘地址
        //此处处理盖章记录文件
        String absoultPath = realpath;//真实地址
        File path = new File(absoultPath);
        //创建父目录文件夹
        if (!path.exists()) {
            //文件不存在？创建文件目录
            path.mkdirs();
        }
        //文件名：
        String name = StringUtils.isNotBlank(fileName) ? fileName : System.currentTimeMillis() + "";

        //把文件字节数组写到本地文件里面
        FileOutputStream fos = null;
        try {
            File file = new File(path, name + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(fileData);
            fos.flush();
            //再次方法内返回fileINFO对象，器她记录结果，字需要关联fileINFO的id即可
            FileInfo fileInfo = new FileInfo();
            fileInfo.setOriginalName(fileName);//原文件名
            fileInfo.setFileName(name+".jpg");//新文件名
            fileInfo.setRelativePath(absoultPath);//绝对路径地址
            fileInfo.setSealRecordInfoId(sealInfoId);
            int insert = mysqlFileInfoService.insert(fileInfo);
            if(insert==1){
                return fileInfo;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}
