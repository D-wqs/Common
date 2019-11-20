package com.stamper.yx.common.service.async;

import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.okhttpUtil.OkHttpCli;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知高拍仪拍照
 * @author D-wqs
 * @data 2019/11/11 20:16
 */
@Slf4j
@Component
public class TalkMeterAsynvService {
    @Autowired
    private OkHttpCli okHttpCli;

    /**
     * 通过设备id,记录id，高拍仪地址，=====>请求图片传输
     */
    @Async
    public void postMeterUploadImage(Integer sealRecordId,String clientAddr) {
        String meterUploadAddr = AppConstant.METER_UPLOAD_ADDR;
        String address = "http://"+clientAddr + meterUploadAddr;
        log.info("高拍仪上传地址{{}}",address);
        Map<String,String> requestBody=new HashMap<>();
        requestBody.put("sealRecordId",sealRecordId+"");

        try {
            String s = okHttpCli.doPost(address,requestBody);
            log.info("通知高拍仪上传res：{{}}",s);
        } catch (Exception e) {
            log.error("通知高拍仪上传失败{{}}",e.getMessage());
            e.printStackTrace();
        }
    }
}
