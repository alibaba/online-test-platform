package com.taobao.qa.ruleengine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private static Map<String,String> propertiesMap;

    private static void processProperties(Properties props) throws UnsupportedEncodingException {

        propertiesMap = new HashMap<String,String>();
        for(Object key: props.keySet()) {
            String keyStr = key.toString();
            propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8"));
        }
    }

    public static void loadAllProperties() throws IOException {
        Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
        processProperties(properties);
    }

    public static String getProperty(String name) {
        return propertiesMap.get(name);
    }
}
