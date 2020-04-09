package com.taobao.qa.ruleengine.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class JsonUtils {
    private static Logger logger = Logger.getLogger(JsonUtils.class);

    public static JSONObject parseJson(String jsonString) {
        JSONObject jo = new JSONObject(jsonString);
        return jo;
    }

    public static String map2JsonStr(Map map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String result = objectMapper.writeValueAsString(map);
            return result;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T map2Object(Map map, Class<T> objType) {
        ObjectMapper objectMapper = new ObjectMapper();
        T obj = objectMapper.convertValue(map,objType);
        return obj;
    }

    public static Map<String,Object> JsonStr2Map(String jsonStr) {
        if(jsonStr == null || jsonStr.trim().isEmpty()) {
            logger.error("Failed to convert JSON String to Map. Input String is null or empty");
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> result = null;
        try {
            result = objectMapper.readValue(jsonStr,Map.class);
        } catch (IOException e) {
            logger.error(String.format("Failed to convert JSON String to Map. Input String: %s, \nError Message: %s",
                    jsonStr, e.getMessage()));
            e.printStackTrace();
            return null;
        }
        return result;
    }

    // allow unrecognized properties
    public static <T> T JsonStr2Obj(String jsonStr,Class<T> valueType) {
        if(jsonStr == null || jsonStr.trim().isEmpty()) {
            logger.error("Failed to convert JSON String to Obj. Input String is null or empty");
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        // not fail on unknown properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        T result = null;
        try {
            result = objectMapper.readValue(jsonStr,valueType);
        } catch (IOException e) {
            logger.error(String.format("Failed to convert JSON String to Map. Input String: %s, \nError Message: %s",
                    jsonStr, e.getMessage()));
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static String obj2JsonStr(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(obj);
        return result;
    }
}

