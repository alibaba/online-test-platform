package com.taobao.qa.ruleengine.core;

import org.dom4j.DocumentException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IContextInitializer {
    public Context initContext(Map<String,Object> req) throws IllegalAccessException, UnsupportedEncodingException, DocumentException;
}
