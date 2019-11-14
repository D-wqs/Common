package com.stamper.yx.common.mapper.mysql;

import com.stamper.yx.common.entity.FileInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface MysqlFileInfoMapper {
    int insert(FileInfo fileInfo);
    int update(FileInfo fileInfo);
    FileInfo get(Integer id);
    FileInfo getByFileName(String fileName);
}