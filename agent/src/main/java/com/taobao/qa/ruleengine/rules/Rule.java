package com.taobao.qa.ruleengine.rules;

import java.util.HashSet;
import java.util.List;

public class Rule {

    private int id;
    private String name;
    private String whenExpression;
    private String verifyExpression;
    private String thenExpression;
    private String state;
    private String message;
    private HashSet<String> tags;
    private String category;
    private int categoryId;
    private int level;
    private boolean retry;
    private boolean skipped;
    private String smoketype;
    private String debugtype;
    private String application;
    private int isautogen;


    public Rule(int id, String name, String whenExp, String verifyExp, String thenExp,
                HashSet<String> tags, String category,int categoryId, int level, boolean retry ,String smoketype,String debugtype,String application,int isautogen) {

        this.id                 = id;
        this.name               = name;
        this.whenExpression     = whenExp;
        this.verifyExpression   = verifyExp;
        this.thenExpression     = thenExp;
        this.state              = "NotExecuted";
        this.message            = "";
        this.tags               = tags;
        this.level              = level;
        this.category           = category;
        this.categoryId         = categoryId;
        this.retry              = retry;
        this.skipped            = false;
        this.smoketype          = smoketype;
        this.debugtype          = debugtype;
        this.application            = application;
        this.isautogen          = isautogen;

    }

    /**
     * 以当前Rule对象为模板，复制出一个新的Rule对象
     * @return
     */
    public Rule clone() {
        Rule result = new Rule(this.id,this.name,this.whenExpression,this.verifyExpression,this.thenExpression,
                (HashSet<String>) this.tags.clone(),this.category,this.categoryId,this.level,this.retry,this.smoketype,this.debugtype,this.application,this.isautogen);
        return result;
    }

    public void setExpressionsToNull() {
        setWhenExpression("");
        setVerifyExpression("");
        setThenExpression("");
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void appendMessage(String msg) {
        this.message += msg+"\n";
    }

    public boolean isException() {
        return this.state.equals("Exception");
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public void setSmoketype(String smoketype) {
        this.smoketype = smoketype;
    }

    public String getSmoketype() {
        return smoketype;
    }

    public void setDebugtype(String debugtype) {
        this.debugtype = debugtype;
    }

    public String getDebugtype() {
        return debugtype;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public int getIsautogen() {
        return isautogen;
    }

    public void setIsautogen(int isautogen) {
        this.isautogen = isautogen;
    }

}