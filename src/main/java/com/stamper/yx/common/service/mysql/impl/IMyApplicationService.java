package com.stamper.yx.common.service.mysql.impl;

import com.stamper.yx.common.entity.Applications;
import com.stamper.yx.common.mapper.mysql.MyApplicationMapper;
import com.stamper.yx.common.service.mysql.MyApplicationService;
import com.stamper.yx.common.sys.error.PrintException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author D-wqs
 * @data 2019/11/21 16:36
 */
@Slf4j
@Service
public class IMyApplicationService implements MyApplicationService {
    @Autowired
    private MyApplicationMapper mapper;
    @Transactional
    @Override
    public int insert(Applications applications) {
        int insertCount=0;
        if(applications!=null){
            insertCount = mapper.insert(applications);
            if(insertCount!=1){
                log.error("【第二数据源】添加申请单失败");
                throw new PrintException("添加申请单失败");
            }
        }
        return insertCount;
    }

    @Transactional
    @Override
    public int update(Applications applications) {
        int updateCount=0;
        if(applications!=null){
            updateCount = mapper.update(applications);
            if(updateCount!=1){
                log.error("【第二数据源】更新申请单失败");
                throw  new PrintException("更新申请单失败");
            }
        }
        return updateCount;
    }

    @Transactional
    @Override
    public void save(Applications applications) {
        if(applications!=null){
            Applications byApplicationId = mapper.getByApplicationId(applications.getApplicationId());
            if(byApplicationId==null){
                mapper.insert(applications);
            }else{
                applications.setId(byApplicationId.getId());
                applications.setUpdateDate(new Date());
                mapper.update(applications);
            }
        }
    }

    @Override
    public Applications getByApplicationId(Integer applicationId) {
        if(applicationId!=null){
            return mapper.getByApplicationId(applicationId);
        }
        return null;
    }
}
