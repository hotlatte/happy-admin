package com.happy.service.impl;

import com.happy.mapper.MilitaryMapper;
import com.happy.model.Military;
import com.happy.service.IMilitaryService;
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
public class MilitaryService implements IMilitaryService {

    @Resource
    private MilitaryMapper militaryMapper;

    @Override
    public int save(Military military) {
        return militaryMapper.save(military);
    }

    @Override
    public int delete(int id) {
        return 0;
    }

    @Override
    public int update(Military military) {
        return militaryMapper.update(military);
    }

    //    @Override
//    public List<Military> findAll(Map params) {
//        return null;
//    }
    @Override
    public List<Military> findAll() {
        return militaryMapper.findAll();
    }


    @Override
    public Military findById(int id) {
        return militaryMapper.findById(id);
    }

    @Override
    public int findMaxNumber() {
        return militaryMapper.findMaxNumber();
    }
}
