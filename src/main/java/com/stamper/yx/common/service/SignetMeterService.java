package com.stamper.yx.common.service;

import com.stamper.yx.common.entity.SignetMeter;

/**
 * @author D-wqs
 * @data 2019/11/11 18:33
 */
public interface SignetMeterService {
    int insert(Integer signetId,Integer meterId);
    int del(Integer signetId,Integer meterId);
    SignetMeter get(Integer signetId,Integer meterId);
    SignetMeter getBySignetId(Integer signetId);
}
