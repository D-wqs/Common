package com.stamper.yx.common.FingerTest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.CommonApplication;
import com.stamper.yx.common.entity.HistoryApplicationInfo;
import com.stamper.yx.common.entity.deviceModel.LoginApplication;
import com.stamper.yx.common.service.mysql.MysqlFingerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

/**
 * @author D-wqs
 * @data 2019/11/21 14:06
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonApplication.class)
public class FingerTest {
    @Autowired
    private MysqlFingerService service;
    @Test
    public void test1(){
        Integer fineFingerAddr = service.getFineFingerAddr(1001);
        System.out.println("传出可用的指纹地址："+fineFingerAddr);
    }

    @Test
    public void test2(){
        Integer i=null;
        int i1 = i + 1;
        System.out.println(i1);
    }
    @Test
    public void test3(){
        String decrypt="{\"Body\":[{\"applicationId\":19,\"useCount\":3}],\"Head\":{\"Cmd\":13,\"Magic\":-46510,\"SerialNum\":0,\"Version\":1}}";
        JSONObject jsonObject = JSONObject.parseObject(decrypt);
        String body = jsonObject.getString("Body");
        List<HistoryApplicationInfo> historyApplicationInfos = JSONArray.parseArray(body, HistoryApplicationInfo.class);
        HistoryApplicationInfo historyApplicationInfo = historyApplicationInfos.get(0);
        System.out.println(historyApplicationInfo);

    }

}
