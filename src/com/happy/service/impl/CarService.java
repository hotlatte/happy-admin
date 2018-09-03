package com.happy.service.impl;

import com.happy.mapper.CarMapper;
import com.happy.model.Car;
import com.happy.service.ICarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author kyle
 * @version 1.0
 * @className
 * @descripsion TODO
 * @date 2018/8/28 13:56
 */
@Service
public class CarService implements ICarService {

    @Resource
    private CarMapper carMapper;

    @Override
    public int save(Car car) {
        return carMapper.save(car);
    }

    @Override
    public int delete(long id) {
        return carMapper.delete(id);
    }

    @Override
    public int update(Car car) {
        return carMapper.update(car);
    }

    @Override
    public List<Car> findAll(Map params) {
        return carMapper.findAll(params);
    }

    @Override
    public Car findById(long id) {
        return carMapper.findById(id);
    }

    @Override
    public int deleteByCountry(String country) {
        return 0;
    }
}
