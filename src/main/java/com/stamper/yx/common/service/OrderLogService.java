package com.stamper.yx.common.service;

import com.stamper.yx.common.entity.OrderLog;

import java.util.List;

public interface OrderLogService {
    void insert(OrderLog orderLog);
    void update(OrderLog orderLog);
    OrderLog get(Integer id);
    List<OrderLog> getByCmd(Integer cmd);
    List<OrderLog> getByName(String name);

}
