package com.stamper.yx.common.mapper.mysql;

import com.stamper.yx.common.entity.Finger;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 保存指纹信息
 * @author D-wqs
 * @data 2019/10/31 9:47
 */
@Repository
public interface MyFingerMapper {
    int insert(Finger finger);
    int update(Finger finger);
    List<Finger> getFingerByDevice(Integer deviceId);
    //查询该设备指纹地址最大的一个
    Integer getMaxFingerAddr(Integer deviceId);
    //获取某设备的指纹
    Finger getByUser(Integer deviceId,Integer userId);
    void delAllByDeviceId(Integer deviceId);
    void delByFingerAddr(Integer deviceId,Integer fingerAddr,Integer userId);

}
