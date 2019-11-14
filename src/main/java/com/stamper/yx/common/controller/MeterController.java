package com.stamper.yx.common.controller;

import com.stamper.yx.common.entity.Meter;
import com.stamper.yx.common.entity.SMBindInfo;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.service.MeterService;
import com.stamper.yx.common.service.SignetMeterService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author D-wqs
 * @data 2019/11/11 16:29
 */
@Slf4j
@RestController
@RequestMapping("/meter/relevance")
public class MeterController {
    @Autowired
    private MeterService meterService;
    @Autowired
    private SignetMeterService smService;
    @Autowired
    private SignetService signetService;

    @RequestMapping(value = "reg",method = RequestMethod.POST)
    public ResultVO reg(Meter meter) {
        if (meter != null) {
            log.info("获取到的参数：{{}}", meter);
            meterService.save(meter);
        }
        return ResultVO.OK();

    }

    /**
     * 获取所有注册来的高拍仪
     *
     * @return
     */
    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    public ResultVO getAll() {
        List<Meter> all = meterService.getAll();
        if (all != null && all.size() > 0) {
            return ResultVO.OK(all);
        }
        return ResultVO.FAIL(Code.ERROR500);
    }

    @RequestMapping(value = "bind",method = RequestMethod.POST)
    public ResultVO bind(Integer signetId, Integer meterId) {
        if (signetId != null && meterId != null) {
            //绑定高拍仪和印章
            Meter meter = meterService.getById(meterId);
            if (meter == null) {
                log.error("当前高拍仪不存在");
                return ResultVO.FAIL(Code.ERROR500);
            }
            Signet signet = signetService.getById(signetId);
            if (signet == null) {
                log.error("当前印章设备不存在");
                return ResultVO.FAIL(Code.ERROR500);
            }
            smService.insert(signetId, meterId);
            return ResultVO.OK("关联成功");
        }
        return ResultVO.FAIL(Code.ERROR500);
    }

    @RequestMapping(value = "/checkBindInfo",method = RequestMethod.GET)
    public ResultVO checkBindInfo(){
        List<SMBindInfo> allBindInfo = meterService.getAllBindInfo();
        if(allBindInfo!=null&&allBindInfo.size()>0){
            return ResultVO.OK("数据来自于sqlite数据源",allBindInfo);
        }
        return ResultVO.FAIL("尚未绑定过设备");
    }

}

