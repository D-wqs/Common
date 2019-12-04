package com.stamper.yx.common.service.mysql;

import com.stamper.yx.common.entity.Finger;

import java.util.List;

/**
 * @author D-wqs
 * @data 2019/11/17 20:58
 */
public interface MysqlFingerService {
    int insert(Finger finger);
    int update(Finger finger);
    int del(Finger finger);
    /**
     *清空指纹
     */
    void delAllBydeviceId(Integer deviceId);
    /**
     *删除指定位置的指纹
     */
    void delByFingerAddr(Integer deviceId,Integer fingerAddr,Integer userId);
    void save(Finger finger);
    List<Finger> getFingerByDevice(Integer deviceId);
    /**
     *获取最大指纹地址
     */
    Integer getFineFingerAddr(Integer deviceId);
    Finger getByUserAndDevice(Integer userId,Integer deviceId);
    Finger getFinger(Finger finger);
}
