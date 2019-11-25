package com.stamper.yx.common.service.mysql;

import com.stamper.yx.common.entity.FileInfo;
import com.stamper.yx.common.entity.SealRecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author D-wqs
 * @data 2019/11/2 17:28
 */
public interface MysqlSealRecordInfoService {
    int addOrUpdate(SealRecordInfo sealRecordInfo);
    int insert(SealRecordInfo sealRecordInfo);
    int update(SealRecordInfo sealRecordInfo);
    int delete(SealRecordInfo sealRecordInfo);

    //deviceId获取所有使用记录【并没有组织概念的区分】
    List<SealRecordInfo> getBydeviceId(@Param("deviceId") Integer deviceId);

    //更具记录id获取记录详情
    SealRecordInfo getById(Integer id);

    //【组织概念】使用当前唯一标识使用记录的属性组，获取唯一记录（uuid、applicationId、count）
    /**
     * 注意：
     *  1.重置使用记录后，会出现记录覆盖问题
     *  2.指纹模式时无申请单id
     *
     */
    List<SealRecordInfo> getByReal(@Param("uuid") String uuid,@Param("applicationId") Integer applicationId,@Param("count") Integer count);


}
