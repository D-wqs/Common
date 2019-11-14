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
    SignetMeter getBySignetId(@Param("signetId") Integer signetId);
}
