package com.stamper.yx.common.service.mysql.impl;

import com.stamper.yx.common.entity.SealRecordInfo;
import com.stamper.yx.common.mapper.mysql.MySealRecordInfoMapper;
import com.stamper.yx.common.service.mysql.MysqlSealRecordInfoService;
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
            //2020年1月14日10:17:54 发现日期值为毫秒值,进行格式转换
            String time = sealRecordInfo.getTime();
            Date date=new Date();
            date.setTime(Long.parseLong(time));
            String strDateFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String format = sdf.format(date);
            sealRecordInfo.setTime(format);
            //查询是否存在该记录,不存在就插入,存在就不操作
            // 申请单id+ count+ time 属性组唯一
            SealRecordInfo byCountAndApplicationIdAndTime = mySealRecordInfoMapper.getByCountAndApplicationIdAndTime(sealRecordInfo.getCount(), sealRecordInfo.getApplicationID(), sealRecordInfo.getTime());
            if(byCountAndApplicationIdAndTime==null){
                // 不存在就插入,存在就不用处理
                count = mySealRecordInfoMapper.insert(sealRecordInfo);
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
    public Integer getMaxCountByDeviceId(Integer deviceId) {
        if(deviceId.intValue()!=0){
            return mySealRecordInfoMapper.getMaxCountByDeviceId(deviceId);
        }
        return null;
    }

    @Override
    public List<SealRecordInfo> getByReal(String uuid, Integer applicationId, Integer count) {
        if (StringUtils.isNotBlank(uuid) && applicationId != null && count != null) {
            return mySealRecordInfoMapper.getByReal(uuid, applicationId, count);
        }
        return null;
    }

    @Override
    public SealRecordInfo getRecordAndAuditIs0(String uuid, Integer applicationId, Integer count) {
        if (StringUtils.isNotBlank(uuid) && applicationId != null && count != null) {
            return mySealRecordInfoMapper.getRecordAndAuditIs0(uuid, applicationId, count);
        }
        return null;
    }
}
