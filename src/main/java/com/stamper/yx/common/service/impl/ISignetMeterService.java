package com.stamper.yx.common.service.impl;

import com.stamper.yx.common.entity.SignetMeter;
import com.stamper.yx.common.mapper.sqlite.SignetMeterMapper;
import com.stamper.yx.common.service.SignetMeterService;
import com.stamper.yx.common.sys.error.PrintException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author D-wqs
 * @data 2019/11/11 18:34
 */
@Slf4j
@Service
public class ISignetMeterService implements SignetMeterService {
    @Autowired
    private SignetMeterMapper mapper;

    @Override
    public int insert(Integer signetId, Integer meterId) {
        int insertCount = 0;
        if (signetId != null && meterId != null) {
            insertCount = mapper.insert(signetId, meterId);
            if (insertCount != 1) {
                log.error("关联印章和高拍仪异常");
                throw new PrintException("关联印章和高拍仪异常");
            }
        }
        return insertCount;
    }

    @Override
    public int del(Integer signetId, Integer meterId) {
        int delCount = 0;
        if (signetId != null && meterId != null) {
            delCount = mapper.del(signetId, meterId);
            if (delCount != 1) {
                log.error("删除印章和高拍仪的关联异常");
                throw new PrintException("删除印章和高拍仪的关联异常");
            }
        }
        return delCount;
    }

    @Override
    public SignetMeter get(Integer signetId, Integer meterId) {
        if (signetId != null && meterId != null) {
            return mapper.get(signetId,meterId);
        }
        return null;
    }

    @Override
    public SignetMeter getBySignetId(Integer signetId) {
        if(signetId!=null){
            return mapper.getBySignetId(signetId);
        }
        return null;
    }
}
