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
            //区分盖章和审计的需求：盖章时、审计时，记录insert，有is_audit判断，
            //假如记录上传慢，审计先上传，那就先insert，不影响，假如insert多个同时出现，需要加锁，或者索引，
            //审计多个相同insert，那就直接inset，对于天津来说不影响
            //因为审计模式可以多次，这里接收到的不止一个记录，应该是个数组
            //记录只会出现一个？不会更新？还是记录只会

            //记录是推送过来，我保存，假如保存失败，会再次上传。数据库记录不出insert，所以，不需要校验是否已存在该记录，直接插入即可
            //当记录同时插入，我需要加锁
            count = mySealRecordInfoMapper.insert(sealRecordInfo);
//            List<SealRecordInfo> byReal = mySealRecordInfoMapper.getByReal(sealRecordInfo.getUuid(), sealRecordInfo.getApplicationID(), sealRecordInfo.getCount());
//            if(byReal==null){
//                count = mySealRecordInfoMapper.insert(sealRecordInfo);
//
//            }else{
//                //TODO 判断是否是审计，因为有先后顺序，以及审计可以多次。
//                if(sealRecordInfo.getIsAudit().intValue()!=0||byReal.getIsAudit().intValue()==1) {
//                    //只要是审计记录，就插入
//                    count = mySealRecordInfoMapper.insert(sealRecordInfo);
//                }else{
//                    //假如查出的是盖章记录，就更新，不是盖章记录，就插入
//                    sealRecordInfo.setId(byReal.getId());
//                    String strDateFormat = "yyyy-MM-dd HH:mm:ss";
//                    SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
//                    sealRecordInfo.setUpdateDate(new Date());
//                    count = mySealRecordInfoMapper.update(sealRecordInfo);
//                    if(count==0){
//                        count=1;
//                    }
//                }
//            }

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
