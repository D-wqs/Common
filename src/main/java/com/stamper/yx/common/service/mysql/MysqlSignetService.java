package com.stamper.yx.common.service.mysql;

import com.stamper.yx.common.entity.Signet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author D-wqs
 * @data 2019/10/29 13:31
 */
public interface MysqlSignetService {
    int insert(Signet signet);

    int delete(Signet signet);

    int update(Signet signet);

    Signet get(@Param("deviceId") Integer deviceId, @Param("corpId") String corpId);

    Signet getByUUID(@Param("uuid") String uuid);

    //传入的signet不存在就添加，存在就更新
    int add(Signet signet);

    List<Signet> getAllByCorpId(@Param("corpId") String corpId);

    List<Signet> getAll();

    Signet getByLike(Signet signet);

    Signet getById(Integer id);
}
