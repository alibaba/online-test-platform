package com.taobao.qa.ruleengine.rules;

import com.taobao.qa.ruleengine.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class CommonRulePicker implements IRulePicker {
    private static final Logger logger = LoggerFactory.getLogger(CommonRulePicker.class);

    private List<Rule> ruleSet;

    public CommonRulePicker(Context context) throws IOException {
        HashSet<String> contextTags = (HashSet<String>) context.get("Tags");
        //RuleSetManager.getAllRules(context);
        this.ruleSet = RuleSetManager.getRuleSetByTags(contextTags);
    }

    public List<Rule> pickupRules(Context context,RuleRunner runner) throws Exception {
        List<Rule> result = new Vector<Rule>();

        if(isEnd(context)) {
            return result;
        }

        for (Rule rule : ruleSet) {
            //TODO: 每次clone一个新实例，测试对性能影响
            Rule ruleToRun = rule.clone();
            if(runner.when(ruleToRun,context)) {
                result.add(ruleToRun);
            }
        }

        return result;
    }

    private boolean isEnd(Context context) {
        List<Rule> executedRules = context.getExecutedRuleSet();
        if(executedRules == null) {
            return false;
        }

        // TODO: 由于目前没有定义清楚终止逻辑，暂且hardcode为true， 即只遍历一轮Rule
        //return executedRules.containsAll(ruleSet);
        return true;
    }

    public List<Rule> getRuleSet() {
        return ruleSet;
    }
}
