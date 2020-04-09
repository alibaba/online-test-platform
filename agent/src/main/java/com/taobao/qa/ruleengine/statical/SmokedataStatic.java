package com.taobao.qa.ruleengine.statical;

import com.alibaba.fastjson.JSONArray;
import com.taobao.qa.ruleengine.core.Context;
import com.taobao.qa.ruleengine.core.RedisConfig;
import com.taobao.qa.ruleengine.utils.JSONReturnFieldParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.*;

public class SmokedataStatic implements IStatic {
    private static final Logger logger = LoggerFactory.getLogger(SmokedataStatic.class);

    /**
     * 读取redis中对应的统计数据
     * @param redisConfig
     * @param keyPattern kgb::taskid::*
     * @return
     */
    public Map<String,Integer> readRedisSmokeData(RedisConfig redisConfig, String keyPattern){
        Map<String, Integer> resMap = new HashMap<>();

        Jedis jedis = redisConfig.getJedisObj();
        Pipeline pipeline = jedis.pipelined();
        if(pipeline != null) {
            Map<String, Response<String>> responseMap = new HashMap<>();
            Response<String> response = null;
            Set<String> keySet = jedis.keys(keyPattern);
            for (String key : keySet) {
                response = pipeline.get(key);
                responseMap.put(key, response);
            }
            pipeline.sync();
            for (String key : responseMap.keySet()) {
                String val = responseMap.get(key).get();
                String[] keyArr = key.split("::");
                resMap.put(keyArr[4], Integer.valueOf(val));
            }
        }
        redisConfig.returnResource(jedis);
        return resMap;
    }

    /**
     * 统计类数据，包括默认统计信息
     * @param pipeline
     * @param prefix_key
     * @param resultslist
     */
    public void updateResultlistRedis(Pipeline pipeline,String prefix_key, LinkedHashMap<String,List<String>> resultslist) {
        for (String key:resultslist.keySet()) {
            // 默认统计信息,包括字段出现总次数，以及取值为空的次数
            pipeline.incr(prefix_key + "::total_"+key);
            if(resultslist.get(key).isEmpty()) {
                pipeline.incr(prefix_key+"empty_"+key);
            }
        }
    }

    public void updateRedis(RedisConfig redisConfig, String prefix_key, Object obj, String url){
        //logger.info("********* start updateRedis time " + System.currentTimeMillis());
        Jedis jedis = redisConfig.getJedisObj();
        Pipeline pipeline = jedis.pipelined();

        try {
            LinkedHashMap<String,List<String>> resultslist = new LinkedHashMap<>();
            if (pipeline != null) {
                if (obj instanceof Context) {
                    Context context = (Context) obj;
                    try {
                        JSONArray resArr = (JSONArray) context.get("返回结果jsonArr");
                        resultslist = JSONReturnFieldParser.parser(resArr.toJSONString());
                        if (resultslist != null) {
                            updateResultlistRedis(pipeline, prefix_key, resultslist);
                        }
                    } catch (Exception e) {
                        logger.info("context not has 返回结果jsonArr");
                    }

                    pipeline.incr(prefix_key + "::total_query");
                    if (context.get("parseexcept_return") != null) {
                        pipeline.incr(prefix_key + "::parseexcept_return");
                    }
                    // 解析异常导致的结果为空时，不统计在返回空结果
                    if (resultslist != null && resultslist.size() == 0) {
                        if (context.get("parseexcept_return") == null) {
                            pipeline.incr(prefix_key + "::empty_return");
                        }
                    }
                }
                pipeline.sync();
            }
        }catch (Exception e) {
            logger.info(prefix_key + ": updateRedis catch Exception,content is :"+e.getMessage());
        }
        redisConfig.returnResource(jedis);
    }
}
