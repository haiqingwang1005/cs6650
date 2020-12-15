package com.haiqing.redis;

import java.time.Duration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {

  private final JedisPool jedisPool;

  private RedisConnection() {
    final JedisPoolConfig poolConfig = buildPoolConfig();
    jedisPool = new JedisPool(poolConfig, "cs6650cache.0918jk.ng.0001.usw2.cache.amazonaws.com");
  }
  private static RedisConnection redisConnection;

  public static RedisConnection getInstance() {
    if (redisConnection == null) {
      redisConnection = new RedisConnection();
    }
    return redisConnection;
  }
  private JedisPoolConfig buildPoolConfig() {
    final JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(128);
    poolConfig.setMaxIdle(128);
    poolConfig.setMinIdle(16);
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    poolConfig.setTestWhileIdle(true);
    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
    poolConfig.setNumTestsPerEvictionRun(3);
    poolConfig.setBlockWhenExhausted(true);
    return poolConfig;
  }

  public JedisPool getJedisPool() {
    return this.jedisPool;
  }

}
