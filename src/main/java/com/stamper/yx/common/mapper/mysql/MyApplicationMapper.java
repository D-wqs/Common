package com.stamper.yx.common.mapper.mysql;

import com.stamper.yx.common.entity.Applications;
import com.stamper.yx.common.entity.Finger;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 保存指纹信息
 * @author D-wqs
 * @data 2019/10/31 9:47
 */
@Repository
public interface MyApplicationMapper {
    int insert(Applications applications);
    int update(Applications applications);
    Applications getByApplicationId(Integer applicationId);
}
