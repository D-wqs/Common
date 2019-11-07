package com.stamper.yx.common.service.mysql.impl;

import com.stamper.yx.common.entity.FileInfo;
import com.stamper.yx.common.mapper.mysql.MysqlFileInfoMapper;
import com.stamper.yx.common.service.mysql.MysqlFileInfoService;
import com.stamper.yx.common.sys.error.PrintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author D-wqs
 * @data 2019/11/2 17:48
 */
@Service
public class IMysqlFileInfoService implements MysqlFileInfoService {
    @Autowired
    private MysqlFileInfoMapper mapper;
    @Override
    @Transactional
    public int insert(FileInfo fileInfo) {
        int addCount=0;
        if(fileInfo!=null){
            addCount=mapper.insert(fileInfo);
        }
        if(addCount!=1){
            throw new PrintException("文件插入失败");
        }
        return addCount;
    }

    @Override
    public FileInfo get(Integer id) {
        if(id!=null&&id.intValue()!=0){
            return mapper.get(id);
        }
        return null;
    }
}
