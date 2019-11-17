package com.stamper.yx.common.service.mysql.impl;

import com.stamper.yx.common.entity.Finger;
import com.stamper.yx.common.mapper.mysql.MyFingerMapper;
import com.stamper.yx.common.service.mysql.MysqlFingerService;
import com.stamper.yx.common.sys.error.PrintException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author D-wqs
 * @data 2019/11/17 21:06
 */
@Slf4j
@Service
public class IMysqlFingerService implements MysqlFingerService {
    @Autowired
    private MyFingerMapper mapper;

    @Transactional
    @Override
    public int insert(Finger finger) {
        int addCount = 0;
        if (finger != null) {
            addCount = mapper.insert(finger);
            if (addCount != 1) {
                log.error("添加指纹信息失败");
                throw new PrintException("添加指纹信息失败");
            }
        }
        return addCount;
    }

    @Transactional
    @Override
    public int update(Finger finger) {
        int updateCount = 0;
        if (finger != null) {
            finger.setUpdateDate(new Date());
            updateCount = mapper.update(finger);
        }
        return updateCount;
    }

    @Transactional
    @Override
    public int del(Finger finger) {
        int delCount = 0;
        if (finger != null) {
            finger.setDeleteDate(new Date());
            delCount = mapper.update(finger);
        }
        return delCount;
    }

    @Override
    public void delAllBydeviceId(Integer deviceId) {
        if(deviceId!=null){
            mapper.delAllByDeviceId(deviceId);
        }
    }

    @Override
    public void delByFingerAddr(Integer deviceId, Integer fingerAddr,Integer userId) {
        if(deviceId!=null&&fingerAddr!=null){
            mapper.delByFingerAddr(deviceId,fingerAddr,userId);
        }
    }

    @Transactional
    @Override
    public void save(Finger finger) {
        if (finger != null) {
            Finger byUser = mapper.getByUser(finger.getDeviceId(), finger.getUserId());
            if (byUser == null) {
                mapper.insert(finger);
            } else {
                finger.setId(byUser.getId());
                finger.setUpdateDate(new Date());
                mapper.update(finger);
            }
        }
    }

    @Override
    public List<Finger> getFingerByDevice(Integer deviceId) {
        List<Finger> fingerByDevice = mapper.getFingerByDevice(deviceId);
        return fingerByDevice;
    }

    @Override
    public Integer getFineFingerAddr(Integer deviceId) {
        Integer maxFingerAddr = mapper.getMaxFingerAddr(deviceId);
        if (maxFingerAddr != null) {
            //返回合适的指纹地址
            List<Finger> fingers = mapper.getFingerByDevice(deviceId);
            int i = 1;
            for (Finger f : fingers) {
                if (i != f.getFingerAddr().intValue()) {
                    return i;
                }
                i++;
            }
        }
        return maxFingerAddr + 1;
    }

    @Override
    public Finger getByUserAndDevice(Integer userId, Integer deviceId) {
        if (userId != null && deviceId != null) {
            Finger byUser = mapper.getByUser(deviceId, userId);
            return byUser;
        }
        return null;
    }
}
