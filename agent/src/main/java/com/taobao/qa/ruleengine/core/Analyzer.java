package com.taobao.qa.ruleengine.core;

import com.ql.util.express.DynamicParamsUtil;
import com.taobao.qa.ruleengine.model.KgbAnalyzeResult;
import com.taobao.qa.ruleengine.rules.*;
import com.taobao.qa.ruleengine.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Analyzer {
    private IContextInitializer contextInitializer;
    private Context context;
    private RuleRunner runner;
    private IRulePicker rulePicker;
    private static final Logger logger = LoggerFactory.getLogger(Analyzer.class);

    public Analyzer(IContextInitializer contextInitializer) {
        this.contextInitializer = contextInitializer;
    }

    public void init(Map<String,Object> data) throws Exception {
        if(contextInitializer == null) {
            throw new Exception("contextInitializer is null");
        }

        DynamicParamsUtil.supportDynamicParams = true;

        // 初始化Context
        context = contextInitializer.initContext(data);
        // 不同业务线，调用各自不同的context类函数

        // 记录开始时间戳
        context.put("timeStart", DateTimeUtils.getCurrentTimeStamp());

        // 根据Context获取RuleRunner
        //runner = ExpressRunnerManager.createRunner(context);
        runner = ExpressRunnerManager.getRunner();

        // 根据Context获取RulePicker
        rulePicker = RulePickerManager.createRulePicker(context);
    }

    public void setContextInitializer(IContextInitializer contextInitializer) {
        this.contextInitializer = contextInitializer;
    }

    public KgbAnalyzeResult analyze() throws Exception {
        List<Rule> rules  = rulePicker.pickupRules(context,runner);
        boolean result = true;
        boolean needRetry = false;
        while(rules.size() > 0) {
            for(Iterator<Rule> it = rules.iterator();it.hasNext();) {
                Rule rule = it.next();
                if(!runner.verify(rule,context)) {
                    result = false;
                    // Rule的retry属性如果为true则整个分析标记为retry
                    if(rule.isRetry()) {
                        needRetry = true;
                    }
                }
                runner.then(rule,context);
                /**
                 * 如果在执行过程中发现本条规则应该被跳过（非Pass也非Fail) 则rule的isState属性会设置true
                 * 这种场景下档条rule应该被结果集所忽略
                 */
                if(rule.isSkipped()) {
                    logger.info("rule is skipped;rule is " + rule.getName());
                    it.remove();
                }
            }

            context.trackExecutedRules(rules);
            rules  = rulePicker.pickupRules(context,runner);
        }

        // 组装分析结果

        // 置结束时间
        context.put("timeEnd", DateTimeUtils.getCurrentTimeStamp());

        // 如果需要retry，用rtCode=201标识，否则rtCode=200
        int rtCode = needRetry?201:200;
        KgbAnalyzeResult analyzeResult = new KgbAnalyzeResult(rtCode,"分析结果： "+result);

        // check executed Rule set, 如果size为0，被认为是失败结果（一条规则都没有命中）
        List<Rule> executedRuleSet = context.getExecutedRuleSet();
        if(null == executedRuleSet || executedRuleSet.size() == 0) {
            analyzeResult = new KgbAnalyzeResult(rtCode,"分析结果： false （未命中任何规则）");
        }

        Map<String,Object> data = new HashMap<String,Object>();

        // RuleSet全集暂时不需要返回
        data.put("executedRuleSet",executedRuleSet);
        data.put("timeStart",context.get("timeStart"));
        data.put("timeEnd",context.get("timeEnd"));
        analyzeResult.setData(data);

        return analyzeResult;
    }

    public Context getContext() {
        return context;
    }
}
