package com.stamper.yx.common.service;

import com.stamper.yx.common.entity.Config;

public interface ConfigService {
    void insert(Config config);
    void update(Config config);
    Config getByUUID(String uuid);
    Config getById(Integer id);
    Config getDefaultConfig();
}
