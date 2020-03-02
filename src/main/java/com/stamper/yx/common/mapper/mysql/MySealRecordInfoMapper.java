package com.stamper.yx.common.mapper.mysql;

import com.stamper.yx.common.entity.SealRecordInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 使用记录存入mysql数据源
 * @author D-wqs
 * @data 2019/10/31 10:06
 */
@Repository
public interface MySealRecordInfoMapper {
    int insert(SealRecordInfo sealRecordInfo);
    int update(SealRecordInfo sealRecordInfo);
    int delete(SealRecordInfo sealRecordInfo);

    //deviceId获取所有使用记录【并没有组织概念的区分】
    List<SealRecordInfo> getBydeviceId(@Param("deviceID") Integer deviceId);

    //根据记录id获取记录详情
    SealRecordInfo getById(Integer id);

    //获取记录中的设备最大使用次数
    Integer getMaxCountByDeviceId(Integer deviceId);

    //【组织概念】使用当前唯一标识使用记录的属性组，获取唯一记录（uuid、applicationId、count）
    /**
     * 注意：
     *  1.重置使用记录后，会出现记录覆盖问题
     *  2.指纹模式时无申请单id
     *
     */
    List<SealRecordInfo> getByReal(@Param("uuid") String uuid,@Param("applicationID") Integer applicationId,@Param("count") Integer count);

    SealRecordInfo getRecordAndAuditIs0(@Param("uuid") String uuid,@Param("applicationID") Integer applicationId,@Param("count") Integer count);
}
