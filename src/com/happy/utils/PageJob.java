package com.happy.utils;

import org.apache.ibatis.plugin.Invocation;

/**
 * Created by sylar on 2017-01-17.
 */
public interface PageJob {
    Object intercept(Invocation invocation, PageHelper helper, Page<?> page);
}
