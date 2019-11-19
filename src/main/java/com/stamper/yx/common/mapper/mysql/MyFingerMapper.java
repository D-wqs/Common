package com.stamper.yx.common.mapper.mysql;

import com.stamper.yx.common.entity.Finger;
import org.apache.ibatis.annotations.Param;
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
    List<Finger> getFingerByDevice(@Param("deviceId") Integer deviceId);
    //查询该设备指纹地址最大的一个
    Integer getMaxFingerAddr(@Param("deviceId") Integer deviceId);
    //获取某设备的指纹
    Finger getByUser(@Param("deviceId") Integer deviceId, @Param("userId") Integer userId);
    void delAllByDeviceId(@Param("deviceId")Integer deviceId);
    void delByFingerAddr(@Param("deviceId")Integer deviceId,@Param("fingerAddr")Integer fingerAddr,@Param("userId") Integer userId);
    Finger getFinger(Finger finger);

}
