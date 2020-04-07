package com.taobao.qa.ruleengine.rules;

import com.taobao.qa.ruleengine.core.Context;

import java.util.List;

public interface IRulePicker {
    public List<Rule> pickupRules(Context context, RuleRunner runner) throws Exception;
    public List<Rule> getRuleSet();
}
