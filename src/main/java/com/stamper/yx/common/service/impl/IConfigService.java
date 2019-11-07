package com.stamper.yx.common.service.impl;

import com.stamper.yx.common.entity.Config;
import com.stamper.yx.common.mapper.sqlite.ConfigMapper;
import com.stamper.yx.common.service.ConfigService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.error.PrintException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IConfigService implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;
    @Override
    @Transactional
    public void insert(Config config) {
        if(config!=null){
            Integer insert = configMapper.insert(config);
            if(insert!=1){
                throw new PrintException("添加配置信息失败");
            }
        }
    }

    @Override
    @Transactional
    public void update(Config config) {
        if(config!=null){
            Integer update = configMapper.update(config);
            if(update!=1){
                throw new PrintException("更新配置信息失败");
            }
        }
    }

    @Override
    public Config getByUUID(String uuid) {
        if(StringUtils.isNotBlank(uuid)){
            Config byUUID = configMapper.getByUUID(uuid);
            if(byUUID!=null){
                return byUUID;
            }
        }
        return null;
    }

    @Override
    public Config getById(Integer id) {
        if(id!=null){
            Config byId = configMapper.getById(id);
            return byId;
        }
        return null;
    }

    @Override
    public Config getDefaultConfig() {
        //获取默认的配置信息
        Config byUUID = configMapper.getByUUID(AppConstant.defaultUUID);
        if(byUUID!=null){
            return byUUID;
        }
        return null;
    }
}
