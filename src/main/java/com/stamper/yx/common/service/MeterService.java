package com.stamper.yx.common.service;

import com.stamper.yx.common.entity.Meter;
import com.stamper.yx.common.entity.SMBindInfo;

import java.util.List;

/**
 * @author D-wqs
 * @data 2019/11/11 15:27
 */
public interface MeterService {
    int insert(Meter meter);
    int update(Meter meter);
    void save(Meter meter);
    List<Meter> getAll();
    Meter getById(Integer meterId);
    List<SMBindInfo> getAllBindInfo();
}
