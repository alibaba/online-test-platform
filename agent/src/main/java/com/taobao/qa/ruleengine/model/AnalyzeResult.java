package com.taobao.qa.ruleengine.model;

import com.taobao.qa.ruleengine.rules.Rule;

import java.util.List;

public class AnalyzeResult {
    String result;
    List<Rule> ruleList;

    public AnalyzeResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }
}
