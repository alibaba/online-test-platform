package com.taobao.qa.ruleengine.utils;

import com.google.gson.JsonElement;
import com.google.gson.*;

import java.util.*;

/**
 * Created by liyang on 2019/3/6.
 */

public class JSONReturnFieldParser {
    /**
     * 使用此方法转换json为打平的map kv集合
     * @param json
     * @return
     */
    public static LinkedHashMap<String,List<String>> parser(String json) {

        LinkedHashMap<String,List<String>> lmap = new LinkedHashMap<>(  );
        parseJson2Map( lmap, json , "" );
        return lmap;
    }

    public static void parseJson2Map(LinkedHashMap map, String json, String parentKey){
        JsonElement jsonElement = new JsonParser().parse(json);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            parseJson2Map(map,jsonObject,parentKey);
        }else if (jsonElement.isJsonArray()){
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()){
                parseJson2Map(map,iterator.next().getAsJsonObject(),parentKey);
            }
        }else if (jsonElement.isJsonPrimitive()){

        }else if (jsonElement.isJsonNull()){

        }
    }

    public static void parseJson2Map(LinkedHashMap map,JsonObject jsonObject,String parentKey){
        for (Map.Entry<String, JsonElement> object : jsonObject.entrySet()) {
            String key = object.getKey();
            JsonElement value = object.getValue();
            String fullkey = (null == parentKey || parentKey.trim().equals("")) ? key : parentKey.trim() + "." + key;
            if (value.isJsonNull()){
                putNewval(map,fullkey,null);
                continue;
            }else if (value.isJsonObject()){
                parseJson2Map(map,value.getAsJsonObject(),fullkey);
            }else if (value.isJsonArray()){
                JsonArray jsonArray = value.getAsJsonArray();
                Iterator<JsonElement> iterator = jsonArray.iterator();
                while (iterator.hasNext()) {
                    JsonElement jsonElement1 = iterator.next();
                    if(jsonElement1.isJsonNull()){
                        putNewval(map,fullkey, value.getAsString());
                    } else if(jsonElement1.isJsonObject()){
                        parseJson2Map(map,jsonElement1.getAsJsonObject(),fullkey);
                    }else if(jsonElement1.isJsonPrimitive()){
                        JsonPrimitive jsonPrimitive = jsonElement1.getAsJsonPrimitive();
                        if (jsonPrimitive.isNumber()) {
                            putNewval(map,fullkey,jsonPrimitive.getAsNumber());
                        } else {
                            putNewval(map,fullkey,jsonPrimitive.getAsString());
                        }
                    }
                }
                continue;
            }else if (value.isJsonPrimitive()){
                try {
                    JsonElement element = new JsonParser().parse(value.getAsString());
                    if (element.isJsonNull()){
                        putNewval(map,fullkey,value.getAsString());
                    }else if (element.isJsonObject()) {
                        parseJson2Map(map, element.getAsJsonObject(), fullkey);
                    } else if (element.isJsonPrimitive()) {
                        JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();

                        if (jsonPrimitive.isNumber()) {
                            putNewval(map,fullkey,jsonPrimitive.getAsNumber());
                        } else {
                            putNewval(map,fullkey,jsonPrimitive.getAsString());
                        }
                    } else if (element.isJsonArray()) {
                        JsonArray jsonArray = element.getAsJsonArray();
                        Iterator<JsonElement> iterator = jsonArray.iterator();
                        while (iterator.hasNext()) {
                            parseJson2Map(map, iterator.next().getAsJsonObject(), fullkey);
                        }
                    }
                }catch (Exception e){
                    putNewval(map,fullkey,value.getAsString());
                }
            }
        }
    }

    public static void putNewval(LinkedHashMap<String,List<String>> map,String key,Object val){
        List<String> list = new LinkedList<>();
        if(map.containsKey(key)) {
            list = map.get(key);
            if(!list.contains(val.toString())) {
                list.add(val.toString());
            }
        }else{
            list.add(val.toString());
        }
        map.put(key,list);
    }

}