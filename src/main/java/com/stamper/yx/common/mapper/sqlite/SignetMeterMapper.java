package com.stamper.yx.common.mapper.sqlite;

import com.stamper.yx.common.entity.SignetMeter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author D-wqs
 * @data 2019/11/11 18:08
 */
@Component
public interface SignetMeterMapper {
    int insert(@Param("signetId") Integer signetId, @Param("meterId") Integer meterId);
    int del (@Param("signetId") Integer signetId, @Param("meterId") Integer meterId);
    SignetMeter get(@Param("signetId") Integer signetId, @Param("meterId") Integer meterId);
    //设备--高拍仪   多对一：所以一个signet只能得到一个meter
    SignetMeter getBySignetId(@Param("signetId") Integer signetId);
}
