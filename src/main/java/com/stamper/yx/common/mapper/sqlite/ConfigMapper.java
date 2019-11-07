package com.stamper.yx.common.mapper.sqlite;

import com.stamper.yx.common.entity.Config;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigMapper {
    Integer insert(Config config);
    Integer update(Config config);
    Config getByUUID(@Param("uuid") String uuid);
    Config getById(@Param("id") Integer id);
}
