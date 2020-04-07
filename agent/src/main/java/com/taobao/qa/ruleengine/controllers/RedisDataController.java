package com.taobao.qa.ruleengine.controllers;

import com.taobao.qa.ruleengine.core.RedisConfig;
import com.taobao.qa.ruleengine.model.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/testruleengine")
public class RedisDataController {
    private static final Logger logger = LoggerFactory.getLogger(RedisDataController.class);
    private RedisConfig redisConfig = new RedisConfig();

    /**
     * Delete expire redis data
     * @return
     */
    @RequestMapping(value="/deleteExpireRedisdata")
    public AjaxResult deleteExpireRedisdata(@RequestParam("prefix_key") String prefix_key){
        Set<String> keySet = new HashSet<>();
        Jedis jedis = redisConfig.getJedisObj();
        Pipeline pipeline = jedis.pipelined();
        if(pipeline != null){
            keySet = jedis.keys(prefix_key + "*");
            for (String key:keySet) {
                pipeline.del(key);
            }
            pipeline.sync();
        }
        redisConfig.returnResource(jedis);
        AjaxResult result = new AjaxResult();
        result.setSuccess(keySet);
        return result;
    }
}
