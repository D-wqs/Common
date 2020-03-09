package com.stamper.yx.common.service.impl;

import com.stamper.yx.common.entity.AddrInfo;
import com.stamper.yx.common.mapper.sqlite.AddrMapper;
import com.stamper.yx.common.service.AddInfoService;
import com.stamper.yx.common.sys.error.PrintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author D-wqs
 * @date 2020/3/9 14:29
 */
@Service
public class IAddInfoService implements AddInfoService {

    @Autowired
    private AddrMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(AddrInfo addrInfo) {
        if(addrInfo!=null){
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            addrInfo.setCreateDate(dateString);
            addrInfo.setUpdateDate(dateString);
            Integer insert = mapper.insert(addrInfo);
            if(insert!=1){
                throw new PrintException("添加地址信息异常");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(AddrInfo addrInfo) {
        if(addrInfo!=null&&addrInfo.getId().intValue()!=0){
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            addrInfo.setUpdateDate(dateString);
            Integer update = mapper.update(addrInfo);
            if(update!=1){
                throw new PrintException("更新设备地址异常");
            }
        }
    }

    @Override
    public List<AddrInfo> getAllBySignet(Integer deviceId) {
        if(deviceId!=null&&deviceId.intValue()!=0){
            List<AddrInfo> allBySignet = mapper.getAllBySignet(deviceId);
            return allBySignet;
        }
        return null;
    }
}
