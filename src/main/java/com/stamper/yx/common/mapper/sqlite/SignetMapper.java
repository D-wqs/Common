package com.stamper.yx.common.mapper.sqlite;

import com.stamper.yx.common.entity.Signet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignetMapper {
    Integer insert(Signet signet);

    Integer update(Signet signet);

    Signet getByUUID(String uuid);

    Signet get(Integer id);

    Signet getByName(String signetName);

    List<Signet> getAll();
}