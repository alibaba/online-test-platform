package com.taobao.qa.ruleengine.model;

import com.taobao.qa.ruleengine.utils.CommonUtils;

public class AjaxResult {
    private int code;
    private String message;
    private Object data;

    private static final Integer SUCCESS_CODE = 200;
    private static final Integer FAILURE_CODE = 400;
    private static final Integer EXCEPTION_CODE = 500;

    public AjaxResult(){}

    public AjaxResult(int code, String message, Object data) {
        this.code       = code;
        this.message    = message;
        this.data        = data;
    }

    public void setSuccess(Object data) {
        this.message    = "success";
        this.code       = SUCCESS_CODE;
        this.data        = data;
    }

    public void setSuccess(String msg) {
        this.message    = msg;
        this.code       = SUCCESS_CODE;
    }

    public void setFailure(String message) {
        this.message    = message;
        this.code       = FAILURE_CODE;
    }

    public void setException(Exception e) {
        this.code       = EXCEPTION_CODE;
        this.message    = e.getMessage() + "\n" + CommonUtils.getStackInfoFromException(e);
    }

    // getters and setters
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

    public void setData(Object res) {
        this.data = res;
    }
}
