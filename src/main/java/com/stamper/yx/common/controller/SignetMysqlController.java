package com.stamper.yx.common.controller;

import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.service.mysql.MysqlSignetService;
import com.stamper.yx.common.sys.AppConstant;
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
 * @data 2019/10/30 10:28
 */
@Slf4j
@RestController
@RequestMapping(value = "signets",method = RequestMethod.POST)
public class SignetMysqlController {
    @Autowired
    private MysqlSignetService mysqlSignetService;

    @RequestMapping(value = "getAll")
    public ResultVO getAlls() {
        //todo mysql 数据源异常
        boolean b = checkDatasource();
        if (b == false) {
            return ResultVO.FAIL(Code.ERROR500);
        }
        List<Signet> all = mysqlSignetService.getAll();
        if (all != null && all.size() > 0) {
            return ResultVO.OK(all);
        }
        return ResultVO.FAIL(Code.ERROR500);
    }

    @RequestMapping("get")
    public ResultVO get(Signet signet) {
        if (signet == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        //todo mysql 数据源异常
        boolean b = checkDatasource();
        if (b == false) {
            return ResultVO.FAIL(Code.ERROR500);
        }
        Signet byLike = mysqlSignetService.getByLike(signet);
        if (byLike != null) {
            return ResultVO.OK(byLike);
        }
        return ResultVO.FAIL(Code.ERROR500);
    }

    @RequestMapping("update")
    public ResultVO update(Signet signet) {
        if (signet == null|| signet.getId()==null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        //todo mysql 数据源异常
        boolean b = checkDatasource();
        if (b == false) {
            return ResultVO.FAIL(Code.ERROR500);
        }
        Signet byLike = mysqlSignetService.getById(signet.getId());
        //当前参数没有查找到对应设备，无法更新
        if (byLike == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        int update = mysqlSignetService.update(signet);
        if(update!=1){
            return ResultVO.FAIL("更新失败");
        }
        return ResultVO.OK("更新成功");
    }

    /**
     * 校验mysql数据源
     * @return
     */
    public boolean checkDatasource() {
        //mysql 数据源同步数据
        String openMysql = AppConstant.OPEN_MYSQL;
        if (openMysql.equalsIgnoreCase("false")) {
            mysqlSignetService = null;
            return false;
        }
        return true;
    }
}
