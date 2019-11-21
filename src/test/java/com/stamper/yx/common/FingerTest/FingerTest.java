package com.stamper.yx.common.FingerTest;

import com.stamper.yx.common.CommonApplication;
import com.stamper.yx.common.service.mysql.MysqlFingerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}
