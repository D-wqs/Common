package com.stamper.yx.common.service.mysql.impl;

import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.mapper.mysql.MySignetMapper;
import com.stamper.yx.common.service.mysql.MysqlSignetService;
import com.stamper.yx.common.sys.error.PrintException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author D-wqs
 * @data 2019/10/29 13:31
 */
@Service
public class IMysqlSignetService implements MysqlSignetService {
    @Autowired
    private MySignetMapper mapper;
    @Override
    @Transactional
    public int insert(Signet signet) {
        int addCount=0;
        if(signet!=null){
            addCount = mapper.insert(signet);
        }
        if(addCount!=1){
            throw new PrintException("设备添加失败");
        }
        return addCount;
    }

    @Override
    @Transactional
    public int delete(Signet signet) {
        int delCount=0;
        if(signet!=null){
            delCount = mapper.delete(signet);
        }
        if (delCount!=1){
            throw new PrintException("删除设置失败");
        }
        return delCount;
    }

    @Override
    @Transactional
    public int update(Signet signet) {
        int updateCount=0;
        if(signet!=null){
            updateCount = mapper.update(signet);
        }
        if(updateCount!=1){
            throw new PrintException("设备更新失败");
        }
        return updateCount;
    }

    @Override
    public Signet get(Integer deviceId, String corpId) {
        if(deviceId!=null&& StringUtils.isNotBlank(corpId)){
            Signet signet = mapper.get(deviceId, corpId);
            if(signet!=null){
                return signet;
            }
        }
        return null;
    }

    @Override
    public Signet getByUUID(String uuid) {
        if(StringUtils.isNotBlank(uuid)){
            Signet byUUID = mapper.getByUUID(uuid);
            if(byUUID!=null){
                return byUUID;
            }
        }
        return null;
    }

    @Override
    public int add(Signet signet) {
        if(signet!=null){
            String uuid = signet.getUuid();
            if(StringUtils.isNotBlank(uuid)){

                //已存在就更新,不存在就添加
                Signet byUUID = mapper.getByUUID(uuid);
                if(byUUID==null){
                    int insert = mapper.insert(signet);
                    if(insert!=1){
                        throw new PrintException("mysql数据源添加设备失败");
                    }
                    return 1;
                }else{
                    int update = mapper.update(byUUID);
                    if(update!=1){
                        throw new PrintException("mysql数据库更新设备失败");
                    }
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public List<Signet> getAllByCorpId(String corpId) {
        if(StringUtils.isNotBlank(corpId)){
            List<Signet> all = mapper.getAllByCorpId(corpId);
            if(all!=null&&all.size()>0){
                return all;
            }
        }
        return null;
    }

    @Override
    public List<Signet> getAll() {
        List<Signet> all = mapper.getAll();
        if(all!=null&&all.size()>0){
            return all;
        }
        return null;
    }

    @Override
    public Signet getByLike(Signet signet) {
        if(signet!=null){
            Signet byLike = mapper.getByLike(signet);
            if(byLike!=null){
                return byLike;
            }
        }
        return null;
    }

    @Override
    public Signet getById(Integer id) {
        if(id!=null){
            Signet byId = mapper.getById(id);
            if(byId!=null){
                return byId;
            }
        }
        return null;
    }
}
