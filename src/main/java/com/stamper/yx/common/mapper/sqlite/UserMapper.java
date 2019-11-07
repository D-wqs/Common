package com.stamper.yx.common.mapper.sqlite;

import com.stamper.yx.common.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    User getUserByName(String name);
    Integer add(User user);
    void del(User user);
    User getUserById(Integer userId);
    Integer update(User user);

}
