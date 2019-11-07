package com.stamper.yx.common.service.mysql;

import com.stamper.yx.common.entity.FileInfo;

/**
 * @author D-wqs
 * @data 2019/11/2 17:48
 */
public interface MysqlFileInfoService {
    int insert(FileInfo fileInfo);
    FileInfo get(Integer id);
}
