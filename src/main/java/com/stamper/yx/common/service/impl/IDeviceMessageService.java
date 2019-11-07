package com.stamper.yx.common.service.impl;

import com.stamper.yx.common.entity.DeviceMessage;
import com.stamper.yx.common.mapper.sqlite.DeviceMessageMapper;
import com.stamper.yx.common.service.DeviceMessageService;
import com.stamper.yx.common.sys.error.PrintException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IDeviceMessageService implements DeviceMessageService {
    @Autowired
    private DeviceMessageMapper mapper;

    @Override
    @Transactional
    public void add(DeviceMessage deviceMessage) {
        if (deviceMessage != null) {
            Integer add = mapper.add(deviceMessage);
            if (add != 1) {
                throw new PrintException("添加离线消息时异常");
            }
        }
    }

    /**
     * 添加或者更新离线消息（用于只能推送1次的离线消息）
     * @param deviceMessage
     */
    @Override
    @Transactional
    public void addOrUpdate(DeviceMessage deviceMessage) {
        if(deviceMessage!=null){
            String title = deviceMessage.getTitle();
            Integer recipientId = deviceMessage.getRecipientId();
            DeviceMessage _dm = getByNameAndSignetAndStatus(title, recipientId, 1);
            if(_dm!=null){
                deviceMessage.setId(_dm.getId());
                update(deviceMessage);
            }else {
                add(deviceMessage);
            }
        }
    }

    @Override
    @Transactional
    public void del(DeviceMessage deviceMessage) {
        if (deviceMessage != null && deviceMessage.getId() != null) {
            DeviceMessage deviceMessage_bak = mapper.get(deviceMessage.getId());
            if (deviceMessage_bak != null) {
                Integer update = mapper.update(deviceMessage_bak);
                if (update != 1) {
                    throw new PrintException("更新离线消息发生异常");
                }
            }
        }
    }

    @Override
    public List<DeviceMessage> getBySignet(Integer deviceId) {
        if (deviceId != null) {
            List<DeviceMessage> deviceMessages = mapper.getBySignet(deviceId);
            if (deviceMessages != null && deviceMessages.size() > 0) {
                return deviceMessages;
            }
        }
        return null;
    }

    @Override
    public void update(DeviceMessage deviceMessage) {
        if (deviceMessage != null && deviceMessage.getId() != null) {
            Integer update = mapper.update(deviceMessage);
            if(update!=1){
                throw new PrintException(String.format("离线消息更新失败{}",deviceMessage.toString()));
            }
        }
    }

    @Override
    public DeviceMessage getByNameAndSignetAndStatus(String title, Integer signetId, Integer status) {
        if (StringUtils.isNotBlank(title) && signetId != null && status != null) {
            return mapper.selectLastOneByTitleAndSignetAndStatus(title, signetId, status);
        }
        return null;
    }
}
