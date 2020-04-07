package com.taobao.qa.ruleengine.core;

import com.taobao.qa.ruleengine.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Configuration
public class RedisConfig  {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    public static JedisPool jedisPool;
    static {
        String redisHost = PropertyUtil.getProperty("spring.redis.host");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        jedisPool = new JedisPool(config,redisHost);
        logger.info("static jedis pool start");
    }

    public Jedis getJedisObj(){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        }catch (JedisConnectionException e){
            logger.info("Exception:Redisconfig get new jedis failed,value is null");
            e.printStackTrace();
            if(jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        }
        return jedis;
    }

    public void returnResource(Jedis jedis){
        if(jedis != null){
            jedisPool.returnResource(jedis);
        }
    }
}
