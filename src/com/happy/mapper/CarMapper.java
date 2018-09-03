package com.happy.mapper;

import com.happy.model.Car;

import java.util.List;
import java.util.Map;

public interface CarMapper {
    int save(Car car);

    int delete(long id);

    int update(Car car);

    List<Car> findAll(Map params);

    Car findById(long id);
}
