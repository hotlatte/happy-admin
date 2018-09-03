package com.happy.service;

import com.happy.mapper.CarMapper;

public interface ICarService extends CarMapper {
    int deleteByCountry(String country);
}
