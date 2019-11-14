package com.stamper.yx.common.mapper.sqlite;

import com.stamper.yx.common.entity.Meter;
import com.stamper.yx.common.entity.SMBindInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author D-wqs
 * @data 2019/11/11 15:21
 */
@Component
public interface MeterMapper {
    int insert(Meter meter);
    int update(Meter meter);
    Meter getByName(String name);
    List<Meter> getAll();
    Meter getById(Integer meterId);
    List<SMBindInfo> getAllBindInfo();
}
