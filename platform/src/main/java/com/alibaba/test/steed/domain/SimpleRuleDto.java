package com.alibaba.test.steed.domain;

import com.alibaba.test.steed.model.RuleWithBLOBs;

import java.util.Date;

/**
 * Created by liyang on 2019/8/29.
 */
public class SimpleRuleDto extends RuleWithBLOBs {
    private String state;
    private String message;


    public SimpleRuleDto(){
        super();
    }

    public SimpleRuleDto(Integer id, String name, String tags, String category, Integer level, Byte enable, Byte retry, String owner, Date modify, String application, Byte debug, String rWhen, String rVerify, String rThen, String state, String message) {
        super( id, name, tags, category, level, enable, retry, owner, modify, application, debug, rWhen, rVerify, rThen );
        this.state = state;
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
