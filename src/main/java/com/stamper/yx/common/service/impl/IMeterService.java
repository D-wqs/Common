package com.stamper.yx.common.service.impl;

import com.stamper.yx.common.entity.Meter;
import com.stamper.yx.common.entity.SMBindInfo;
import com.stamper.yx.common.mapper.sqlite.MeterMapper;
import com.stamper.yx.common.service.MeterService;
import com.stamper.yx.common.sys.error.PrintException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author D-wqs
 * @data 2019/11/11 15:28
 */
@Slf4j
@Service
public class IMeterService implements MeterService {
    @Autowired
    private MeterMapper mapper;

    @Transactional
    @Override
    public int insert(Meter meter) {
        int insertCount = 0;
        if (meter != null) {
            insertCount = mapper.insert(meter);
        }
        if (insertCount != 1) {
            log.error("高拍仪insert异常");
            throw new PrintException("高拍仪insert异常");
        }
        return insertCount;
    }

    @Transactional
    @Override
    public int update(Meter meter) {
        int updateCount = 0;
        if (meter != null) {
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            meter.setUpdateDate(sdf.format(new Date()));
            updateCount = mapper.update(meter);
        }
        return updateCount;
    }

    @Transactional
    @Override
    public void save(Meter meter) {
        if (meter != null) {
            Meter byName = mapper.getByName(meter.getName());
            if (byName == null) {
                mapper.insert(meter);
            } else {
                meter.setId(byName.getId());
                String strDateFormat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                meter.setUpdateDate(sdf.format(new Date()));
                mapper.update(meter);
            }
        }
    }

    @Override
    public List<Meter> getAll() {
        List<Meter> all = mapper.getAll();
        return all;
    }

    @Override
    public Meter getById(Integer meterId) {
        if (meterId != null) {
            return mapper.getById(meterId);
        }
        return null;
    }

    /**
     * 获取所有绑定信息
     * @return
     */
    @Override
    public List<SMBindInfo> getAllBindInfo() {
        List<SMBindInfo> allBindInfo = mapper.getAllBindInfo();

        return allBindInfo;
    }
}
