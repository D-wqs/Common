package com.stamper.yx.common.mapper.sqlite;

import com.stamper.yx.common.entity.OrderLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLogMapper {
    Integer insert(OrderLog orderLog);
    Integer update(OrderLog orderLog);
    List<OrderLog> getAll();
    OrderLog get(Integer id);
    List<OrderLog> getByCmd(Integer cmd);
    List<OrderLog> getByName(String name);
}
