package com.taobao.qa.ruleengine.utils;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommonUtils {
    public static String getStackInfoFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "\n" + sw.toString() + "\n";
        } catch(Exception e2) {
            return "bad getStackInfoFromException()";
        }
    }

    public static String getCauseOfException(Exception e) {
        StringBuffer result = new StringBuffer();
        result.append(ExceptionUtils.getMessage(e)+"\n");
        result.append(ExceptionUtils.getRootCauseMessage(e));
        return result.toString();
    }

    public static Map<String,String> Obj2StrMap(Object obj) throws IllegalAccessException {
        Map<String,String> map = new HashMap<String,String>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields) {
            field.setAccessible(true);
            String key = field.getName();
            Object valueObj = field.get(obj);
            String value = null;
            if(null != valueObj) {
                value = valueObj.toString();
            }
            map.put(key,value);
        }
        return map;
    }

    public static Map<String,String> ObjFromFather2StrMap(Object obj) throws IllegalAccessException {
        Map<String,String> map = new HashMap<String,String>();
        try {
            for (Class<?> clazz = obj.getClass();
                 clazz != Object.class;
                 clazz = clazz.getSuperclass()){

                Field[] fields =  clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String key = field.getName();
                    Object valueObj = field.get(obj);
                    String value = null;
                    if (null != valueObj) {
                        value = valueObj.toString();
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 取两个集合的交集
     *
     * @param set1
     * @param set2
     * @return
     */
    public static <T> HashSet<T> getIntersection(HashSet<T> set1, HashSet<T> set2) {
        HashSet<T> output = new HashSet<T>();
        output.addAll(set1);
        output.retainAll(set2);
        return output;
    }
}
