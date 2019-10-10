package com.stamper.yx.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.entity.SealRecordInfo;
import com.stamper.yx.common.sys.okhttpUtil.OkHttpCli;
import com.stamper.yx.common.sys.response.ResultVO;
import com.stamper.yx.common.sys.security.AES.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = {"/device/sealRecordInfo"}, method = RequestMethod.POST)
public class SealRecordInfoController {
    @Autowired
    private OkHttpCli okHttpCli;
    /**
     * 指纹模式上传
     * 0:成功  1:失败  2:图片重复 3:出错
     *
     * @param sealRecordInfo
     * @return
     */
    @RequestMapping(value = "addEasyInfo")
    public String addEasyInfo(SealRecordInfo sealRecordInfo) {
        String s = JSONObject.toJSONString(sealRecordInfo);
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
            //此处处理盖章记录文件

        } catch (Exception e) {
            e.printStackTrace();
        }
        //回调成功：服务端返回给设备：【0:成功】、【1:失败】、【2:图片重复】、【3:出错】
        //加入返回1和3，设备会认为记录未同步，继续上传记录信息到此接口
        log.info("收到的记录值：{{}}", s);
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
        String s = null;
        try {
            s = JSONObject.toJSONString(sealRecordInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("转换异常:{{}}", e.getMessage());
        }
        log.info("收到的记录值：{{}}", s);
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
        String s = JSONObject.toJSONString(sealRecordInfo);
        log.info("收到的记录值：{{}}", s);
        return "0";
    }

    //获取aesKey
    public String getAesKey(Integer deviceId){
        //TODO 获取ticket,每次运行会打印在控制台，除非重启否则不会改变
        //请求参数：name，password
        //请求地址：url:http://ip:port/user/getTicket【无需校验请求头】
        Map<String,String > ticket =new HashMap<>();
        ticket.put("name", "admin");
        ticket.put("password","admin_123456");
        String s2 = okHttpCli.doPost("http://210.45.123.111:8888/user/getTicket", ticket);

        //TODO 获取token，获取密钥
        //请求参数：ticket
        //请求地址：url:http://ip:port/device/getAccessToken【无需校验请求头】
        Map<String, String> getToken = new HashMap<>();
        getToken.put("ticket", s2);
        String s1 = okHttpCli.doPost("http://210.45.123.111:8888/device/getAccessToken", getToken);
        ResultVO resultVO = JSONObject.parseObject(s1, ResultVO.class);
        Object token = resultVO.getData();
        //TODO 获取对称密钥aesKey
        //请求参数：deviceId
        //请求地址：url:http://ip:port/device/getAesKey
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", deviceId+"");
        String s = okHttpCli.doPostWithHeader("http://210.45.123.111:8888/device/getAesKey", map, token.toString());//添加请求头：Authorization
        ResultVO aesData = JSONObject.parseObject(s, ResultVO.class);
        Object aesKey = aesData.getData();
        log.info("获取到的返回值{{}}", aesKey.toString());
        return aesKey.toString();
    }
}
