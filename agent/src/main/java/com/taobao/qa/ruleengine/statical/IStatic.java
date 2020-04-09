package com.taobao.qa.ruleengine.statical;

import com.taobao.qa.ruleengine.core.RedisConfig;
import redis.clients.jedis.Jedis;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public interface IStatic {
    // 更新redis数据
    //public void updateRedis(Jedis jedis, String prefix_key,LinkedList<LinkedHashMap<String,String>> adlist);
    public void updateRedis(RedisConfig redisConfig, String prefix_key, Object obj, String url);
}
