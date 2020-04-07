package com.taobao.qa.ruleengine.model;

import java.io.Serializable;

public class RuleEntity implements Serializable {
    private int id;
    private String name;
    private String tags;
    private String category;
    private Integer level;
    private String r_when;
    private String r_verify;
    private String r_then;
    private int enable;
    private int retry;
    private String logic_id;
    private String author;
    private String time;
    private String application;
    private String debugtype;
    private String product;
    private int isautogen;

    public RuleEntity(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getWhen() {
        return r_when;
    }

    public void setWhen(String when) {
        this.r_when = when;
    }

    public String getVerify() {
        return r_verify;
    }

    public void setVerify(String verify) {
        this.r_verify = verify;
    }

    public String getThen() {
        return r_then;
    }

    public void setThen(String then) {
        this.r_then = then;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public String getLogic_id() {
        return logic_id;
    }

    public void setLogic_id(String logic_id) {
        this.logic_id = logic_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getDebugtype() {
        return debugtype;
    }

    public void setDebugtype(String debugtype) {
        this.debugtype = debugtype;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setIsautogen(int isautogen) {
        this.isautogen = isautogen;
    }

    public int isIsautogen() {
        return isautogen;
    }

}
