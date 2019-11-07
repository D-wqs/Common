package com.stamper.yx.common.mapper.sqlite;

import com.stamper.yx.common.entity.DeviceMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMessageMapper {
    Integer add(DeviceMessage deviceMessage);

//    Integer del(DeviceMessage deviceMessage);

    List<DeviceMessage> getBySignet(Integer deviceId);

    DeviceMessage get(Integer id);

    Integer update(DeviceMessage deviceMessage);

    DeviceMessage selectLastOneByTitleAndSignetAndStatus(String title, Integer signetId, Integer status);
}
