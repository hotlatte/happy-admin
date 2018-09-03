package com.happy.mapper;

import com.happy.model.Military;

import java.util.List;
import java.util.Map;

public interface MilitaryMapper {
    int save(Military military);

    int delete(int id);

    int update(Military military);

    //List<Military> findAll(Map params);
    List<Military> findAll();

    Military findById(int id);

    int findMaxNumber();
}
