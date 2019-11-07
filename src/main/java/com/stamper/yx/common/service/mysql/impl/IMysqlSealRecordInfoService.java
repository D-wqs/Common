package com.stamper.yx.common.service.mysql.impl;

import com.stamper.yx.common.entity.SealRecordInfo;
import com.stamper.yx.common.mapper.mysql.MySealRecordInfoMapper;
import com.stamper.yx.common.service.mysql.MysqlSealRecordInfoService;
import com.stamper.yx.common.sys.error.PrintException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author D-wqs
 * @data 2019/11/2 17:30
 */
@Service
public class IMysqlSealRecordInfoService implements MysqlSealRecordInfoService {
    @Autowired
    private MySealRecordInfoMapper mySealRecordInfoMapper;

    /**
     * 先查，不存在则插入，存在就更新
     * @param sealRecordInfo
     * @return
     */
    @Override
    @Transactional
    public int addOrUpdate(SealRecordInfo sealRecordInfo) {
        int count=0;
        if(sealRecordInfo!=null){
            if(sealRecordInfo.getApplicationID()==null){
                sealRecordInfo.setApplicationID(0);
            }
            SealRecordInfo byReal = mySealRecordInfoMapper.getByReal(sealRecordInfo.getUuid(), sealRecordInfo.getApplicationID(), sealRecordInfo.getCount());
            if(byReal==null){
                count = mySealRecordInfoMapper.insert(sealRecordInfo);

            }else{
                //TODO 判断是否是审计，因为有先后顺序，以及审计可以多次。
                if(sealRecordInfo.getIsAudit().intValue()!=0||byReal.getIsAudit().intValue()==1) {
                    //只要是审计记录，就插入
                    count = mySealRecordInfoMapper.insert(sealRecordInfo);
                }else{
                    //假如查出的是盖章记录，就更新，不是盖章记录，就插入
                    count = mySealRecordInfoMapper.update(sealRecordInfo);
                }
            }
        }
        if(count!=1){
            throw new PrintException("记录上传异常");
        }
        return count;
    }

    @Override
    @Transactional
    public int  insert(SealRecordInfo sealRecordInfo) {
        int addCount = 0;
        if (sealRecordInfo != null) {
            addCount = mySealRecordInfoMapper.insert(sealRecordInfo);
        }
        if (addCount != 1) {
            throw new PrintException("记录插入失败");
        }
        return addCount;
    }

    @Override
    @Transactional
    public int update(SealRecordInfo sealRecordInfo) {
        int updateCount = 0;
        if (sealRecordInfo != null) {
            updateCount = mySealRecordInfoMapper.update(sealRecordInfo);
        }
        if (updateCount != 1) {
            throw new PrintException("记录更新失败");
        }
        return updateCount;
    }

    @Override
    @Transactional
    public int delete(SealRecordInfo sealRecordInfo) {
        int delCount = 0;
        if (sealRecordInfo != null) {
            delCount = mySealRecordInfoMapper.delete(sealRecordInfo);
        }
        if (delCount != 1) {
            throw new PrintException("记录删除失败");
        }
        return delCount;
    }

    @Override
    public List<SealRecordInfo> getBydeviceId(Integer deviceId) {
        if (deviceId != null || deviceId.intValue() != 0) {
            List<SealRecordInfo> bydeviceId = mySealRecordInfoMapper.getBydeviceId(deviceId);
            if (bydeviceId != null && bydeviceId.size() > 0) {
                return bydeviceId;
            }
        }
        return null;
    }

    @Override
    public SealRecordInfo getById(Integer id) {
        if (id != null && id.intValue() != 0) {
            return mySealRecordInfoMapper.getById(id);
        }
        return null;
    }

    @Override
    public SealRecordInfo getByReal(String uuid, Integer applicationId, Integer count) {
        if (StringUtils.isNotBlank(uuid) && applicationId != null && count != null) {
            return mySealRecordInfoMapper.getByReal(uuid, applicationId, count);
        }
        return null;
    }
}
