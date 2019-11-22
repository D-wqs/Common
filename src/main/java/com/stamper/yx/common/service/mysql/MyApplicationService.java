package com.stamper.yx.common.service.mysql;

import com.stamper.yx.common.entity.Applications;

/**
 * @author D-wqs
 * @data 2019/11/21 16:35
 */
public interface MyApplicationService {
    int insert(Applications applications);
    int update(Applications applications);
    void save(Applications applications);
    Applications getByApplicationId(Integer applicationId);
}
