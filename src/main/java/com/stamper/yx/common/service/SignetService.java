package com.stamper.yx.common.service;

import com.stamper.yx.common.entity.Signet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SignetService {
    void add(Signet signet);

    void del(Signet signet);

    void update(Signet signet);

    Signet getById(Integer signetId);

    Signet getByName(String signetName);

    Signet getByUUID(@Param("uuid") String uuid);

    Signet get(@Param("id") Integer id);

    List<Signet> getAll();

    void save(Signet signet);
}
