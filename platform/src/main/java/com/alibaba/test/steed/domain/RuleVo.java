package com.alibaba.test.steed.domain;

/**
 * Created by liyang on 2019/8/30.
 */
public class RuleVo {
    public String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWhenExpression() {
        return whenExpression;
    }

    public void setWhenExpression(String whenExpression) {
        this.whenExpression = whenExpression;
    }

    public String getVerifyExpression() {
        return verifyExpression;
    }

    public void setVerifyExpression(String verifyExpression) {
        this.verifyExpression = verifyExpression;
    }

    public String getThenExpression() {
        return thenExpression;
    }

    public void setThenExpression(String thenExpression) {
        this.thenExpression = thenExpression;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRetry() {
        return retry;
    }

    public void setRetry(String retry) {
        this.retry = retry;
    }

    public String getDebugtype() {
        return debugtype;
    }

    public void setDebugtype(String debugtype) {
        this.debugtype = debugtype;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String name;

    public String state;

    public String whenExpression;

    public String verifyExpression;

    public String thenExpression;

    public String message;

    public String category;

    public String categoryId;

    public String level;

    public String retry;

    public String debugtype;
}
