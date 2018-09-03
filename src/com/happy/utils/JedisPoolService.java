package com.happy.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolService {
    private static JedisPoolService jedisPoolService;
    private JedisPoolConfig jedisPoolConfig;
    private String host = "localhost";
    private int port = 6379;

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private JedisPool pool;

    /**
     * 建立连接池 真实环境，一般把配置参数缺抽取出来。
     */
    private void createJedisPool() {
        pool = new JedisPool(jedisPoolConfig, host, port);
    }

    /**
     * 在多线程环境同步初始化
     */
    public synchronized void poolInit() {
        if (pool == null)
            createJedisPool();
        JedisPoolService.jedisPoolService = this;
    }

    public static JedisPoolService currentJedisPoolService() {
        return jedisPoolService;
    }

    /**
     * 获取一个jedis 对象
     *
     * @return
     */
    public Jedis getJedis() {
        if (pool == null)
            poolInit();
        return pool.getResource();
    }
}
