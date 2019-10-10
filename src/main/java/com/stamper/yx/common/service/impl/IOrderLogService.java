package com.stamper.yx.common.service.impl;

import com.stamper.yx.common.entity.OrderLog;
import com.stamper.yx.common.mapper.OrderLogMapper;
import com.stamper.yx.common.service.OrderLogService;
import com.stamper.yx.common.sys.error.PrintException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IOrderLogService implements OrderLogService {
    @Autowired
    private OrderLogMapper orderLogMapper;
    @Override
    @Transactional
    public void insert(OrderLog orderLog) {
        if(orderLog!=null){
            Integer insert = orderLogMapper.insert(orderLog);
            if(insert!=1){
                throw new PrintException("添加日志记录异常");
            }
        }
    }

    @Override
    @Transactional
    public void update(OrderLog orderLog) {
        if(orderLog!=null){
            Integer update = orderLogMapper.update(orderLog);
            if(update!=1){
                throw new PrintException("更新日志记录异常");
            }
        }
    }

    @Override
    public OrderLog get(Integer id) {
        if(id!=null&&id.intValue()!=0){
            OrderLog orderLog = orderLogMapper.get(id);
            return orderLog;
        }
        return null;
    }

    @Override
    public List<OrderLog> getByCmd(Integer cmd) {
        if(cmd!=null&&cmd.intValue()!=0){
            List<OrderLog> byCmd = orderLogMapper.getByCmd(cmd);
            if(byCmd!=null&&byCmd.size()>0){
                return byCmd;
            }
        }
        return null;
    }

    @Override
    public List<OrderLog> getByName(String name) {
        if(StringUtils.isNotBlank(name)){
            List<OrderLog> byName = orderLogMapper.getByName(name);
            if(byName!=null&&byName.size()>0){
                return byName;
            }
        }
        return null;
    }
}
