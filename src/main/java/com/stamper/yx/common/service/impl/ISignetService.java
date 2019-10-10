package com.stamper.yx.common.service.impl;

import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.mapper.SignetMapper;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.sys.error.PrintException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ISignetService implements SignetService {
    @Autowired
    private SignetMapper signetMapper;

    @Override
    @Transactional
    public void add(Signet signet) {
        if (signet != null) {
            Integer insert = signetMapper.insert(signet);
            if (insert != 1) {
                log.error("设备注册异常");
                throw new PrintException("设备注册异常");
            }
        }
    }

    @Override
    @Transactional
    public void del(Signet signet) {

    }

    @Override
    @Transactional
    public void update(Signet signet) {
        if (signet != null && StringUtils.isNotBlank(signet.getUuid())) {
            Integer update = signetMapper.update(signet);
            if (update != 1) {
                log.error("更新设备时异常uuid-->{{}}", signet.getUuid());
                throw new PrintException("更新设备时异常");
            }
        }
    }

    @Override
    public Signet getById(Integer signetId) {
        if(signetId!=null){
            Signet signet = signetMapper.get(signetId);
            if(signet!=null){
                return signet;
            }
        }
        return null;
    }

    @Override
    public Signet getByName(String signetName) {
        if(StringUtils.isNotBlank(signetName)){
            Signet byName = signetMapper.getByName(signetName);
            if(byName!=null){
                return byName;
            }
        }
        return null;
    }

    @Override
    public Signet getByUUID(String uuid) {
        if (StringUtils.isNotBlank(uuid)) {
            Signet byUUID = signetMapper.getByUUID(uuid);
            if (byUUID != null) {
                return byUUID;
            }
            log.info("uuid--》{{}}，没有查到设备信息", uuid);
        }
        return null;
    }

    @Override
    public Signet get(Integer id) {
        if (id != null && id != 0) {
            Signet signet = signetMapper.get(id);
            if (signet != null) {
                return signet;
            }
            log.error("当前设备不存在 id-->{{}}", id);
        }
        return null;
    }

    @Override
    public List<Signet> getAll() {
        List<Signet> all = signetMapper.getAll();
        if(all!=null&&all.size()>0){
            return all;
        }
        return null;
    }

}
