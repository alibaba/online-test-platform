package com.taobao.qa.ruleengine.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.qa.ruleengine.statical.SmokedataStatic;
import com.taobao.qa.ruleengine.utils.DateTimeUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;

public class DefaultContextInitializer implements IContextInitializer {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DefaultContextInitializer.class);

    public static SmokedataStatic smokedataStatic = new SmokedataStatic();

    public static RedisConfig redisConfig = new RedisConfig();

    public Context initContext(Map<String,Object> data)
            throws IllegalAccessException, UnsupportedEncodingException, DocumentException {
        Context context = new Context();

        String taskid = (String) data.get("taskid");

        // 原始Request String (做unescape处理)
        String reqUrl = (String)data.get("request");
        reqUrl = StringEscapeUtils.unescapeJava(reqUrl);

        // 原始Response String (做unescape处理)
        String respStr = (String)data.get("response");
        //respStr = StringEscapeUtils.unescapeJava(respStr);

        String type = (String) data.get("type");
        String date = DateTimeUtils.getCurrentDate();
        String prefix_key = type + "::smokedata::" + date + "::" + taskid;

        HashSet<String> tagSet = new HashSet<String>();
        tagSet.add(type);
        // 默认情况下type就是tags
        context.put("Tags", tagSet);

        // 记录必要信息到context
        context.put("接收请求串",1);
        context.put("type",type);
        context.put("原始查询串",reqUrl);
        context.put("原始返回结果",respStr);
        context.put("返回结果jsonArr",new JSONObject());
        context.put("解析异常",0);
        try {
            // 默认按照json或者jsonp的格式解析
            JSONArray resArr= parseRes(respStr);
            context.put("返回结果jsonArr",resArr);
            if(resArr.size() == 0) {
                context.put("解析异常",1);
            }
            smokedataStatic.updateRedis(redisConfig,prefix_key,context,reqUrl);
            context.put("统计结果",smokedataStatic.readRedisSmokeData(redisConfig,prefix_key + "::*"));
        }catch (Exception e) {
            logger.info("DefaultContextInitializer parse result catch exception {}", e);
            e.printStackTrace();
        }
        return context;
    }

    public JSONArray parseRes(String resStr) {
        JSONObject resObj = new JSONObject();
        JSONArray resArr = new JSONArray();
        try {
            int startIndex = resStr.indexOf("{");
            int endIndex = resStr.lastIndexOf("}");
            String json = resStr.substring(startIndex, endIndex+1);
            resObj = JSONObject.parseObject(json);
            resArr = JSONArray.parseArray("["+json+"]");
        }catch (Exception e) {
            logger.error("resStr is {}" , resStr);
        }
        return resArr;
    }


}
