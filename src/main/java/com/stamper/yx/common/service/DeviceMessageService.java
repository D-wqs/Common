package com.stamper.yx.common.service;

import com.stamper.yx.common.entity.DeviceMessage;

import java.util.List;

//离线message消息
public interface DeviceMessageService {
    void add(DeviceMessage deviceMessage);

    void addOrUpdate(DeviceMessage deviceMessage);

    void del(DeviceMessage deviceMessage);

    //查询印章离线信息列表
    List<DeviceMessage> getBySignet(Integer deviceId);

    void update(DeviceMessage deviceMessage);
    //查询最后一个未推送成功的指令(指定title)
    DeviceMessage getByNameAndSignetAndStatus(String title, Integer signetId, Integer status);

}
