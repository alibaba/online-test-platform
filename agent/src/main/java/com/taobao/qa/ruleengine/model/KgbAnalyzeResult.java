package com.taobao.qa.ruleengine.model;

import com.taobao.qa.ruleengine.rules.Rule;

import java.util.List;

public class KgbAnalyzeResult {
    private int code;
    private String message;
    private Object data;
    private Object debugMap;

    public KgbAnalyzeResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setDebugMap(Object debugMap) {
        this.debugMap = debugMap;
    }

    public Object getDebugMap() {
        return debugMap;
    }
}
