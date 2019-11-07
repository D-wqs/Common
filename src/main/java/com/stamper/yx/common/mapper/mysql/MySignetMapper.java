package com.stamper.yx.common.mapper.mysql;

import com.stamper.yx.common.entity.Signet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mysql数据源处理
 * 为了适配钉钉第三方，关联组织信息
 *
 * @author D-wqs
 * @data 2019/10/29 9:04
 */
@Repository
public interface MySignetMapper {
    int insert(Signet signet);

    int delete(Signet signet);

    int update(Signet signet);

    Signet get(@Param("deviceId") Integer deviceId, @Param("corpId") String corpId);

    Signet getByUUID(@Param("uuid") String uuid);

    List<Signet> getAllByCorpId(@Param("corpId") String corpId);

    List<Signet> getAll();

    //加入没有使用组织id，出现同名设备，此处最好limit 1,就算传入CorpID也只会出现一条记录
    Signet getByLike(Signet signet);

    Signet getById(@Param("deviceId")Integer deviceId);
}
