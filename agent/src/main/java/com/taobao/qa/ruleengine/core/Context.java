package com.taobao.qa.ruleengine.core;

import com.alibaba.fastjson.JSONArray;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.IExpressContext;
import com.taobao.qa.ruleengine.rules.Rule;
import com.taobao.qa.ruleengine.utils.PropertyUtil;
import com.taobao.qa.ruleengine.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Context {
    private IExpressContext<String,Object> expressContext;
    private HashMap<String,Object> debugMap = new HashMap<>();

    public Context() {
        this.expressContext = new DefaultContext<String, Object>();
    }

    public Object put(String key, Object value){
        debugMap.put(key,value);
        return expressContext.put(key,value);
    }

    public Object get(String key) {
        return expressContext.get(key);
    }

    public IExpressContext<String, Object> getExpressContext() {
        return expressContext;
    }

    public void trackExecutedRules(List<Rule> rules) {
        String key = "executedRules";
        List<Rule> ruleList = null;
        if(expressContext.get(key) == null) {
            ruleList = new Vector<Rule>();
        }
        else{
            ruleList = (List<Rule>) expressContext.get(key);
        }

        ruleList.addAll(rules);
        expressContext.put(key,ruleList);
    }

    /**
     *  将本次分析执行过的RuleSet返回(为了减少传输，将执行结果为Pass的RuleSet内容置空)
     * @return
     */
    public List<Rule> getExecutedRuleSet() {
        List<Rule> executedRuleSet = (List<Rule>) expressContext.get("executedRules");
        if(null == executedRuleSet) {
            return null;
        }

        for(Rule rule:executedRuleSet) {
            // 如果message过长则截断
            int maxMsgLength = Integer.parseInt(PropertyUtil.getProperty("max.message.length"));
            String msg = rule.getMessage();
            if(msg.length() > maxMsgLength) {
                rule.setMessage(StringUtil.truncateString(msg,maxMsgLength));
            }

            // 如果Rule为Pass状态则将expression置空
            if(!rule.isException() && rule.getState().equals("Pass")) {
                rule.setExpressionsToNull();
            }
        }

        return executedRuleSet;
    }

    public HashMap<String, Object> getDebugMap() {
        return debugMap;
    }
}
