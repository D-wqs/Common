package com.stamper.yx.common.mapper.mysql;

import com.stamper.yx.common.entity.AddrInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface MyAddrInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AddrInfo record);

    int insertSelective(AddrInfo record);

    AddrInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AddrInfo record);

    int updateByPrimaryKey(AddrInfo record);
}