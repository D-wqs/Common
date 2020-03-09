package com.stamper.yx.common.service;

import com.stamper.yx.common.entity.AddrInfo;

import java.util.List;

public interface AddInfoService {
    void insert(AddrInfo addrInfo);
    void update(AddrInfo addrInfo);
    List<AddrInfo> getAllBySignet(Integer deviceId);
}
