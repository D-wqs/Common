package com.stamper.yx.common.mapper.sqlite;

import com.stamper.yx.common.entity.AddrInfo;
import com.stamper.yx.common.entity.Config;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddrMapper {
    Integer insert(AddrInfo addrInfo);
    Integer update(AddrInfo AddrInfo);
    List<AddrInfo> getAllBySignet(Integer deviceId);
    AddrInfo getById(Integer id);
}
