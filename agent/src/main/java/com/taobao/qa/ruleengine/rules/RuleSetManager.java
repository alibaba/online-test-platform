package com.taobao.qa.ruleengine.rules;

import com.taobao.qa.ruleengine.core.Context;
import com.taobao.qa.ruleengine.utils.CommonUtils;
import com.taobao.qa.ruleengine.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.taobao.qa.ruleengine.utils.CommonConstants;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class RuleSetManager {
    private static Logger logger = LoggerFactory.getLogger(RuleSetManager.class);

    private static IRuleLoader ruleLoader;
    private static List<Rule> ruleSet;
    private static String rule_table;

    static {
        String ruleLoaderType = PropertyUtil.getProperty("rule.loader");
        if(ruleLoaderType.equals("db")) {
            ruleLoader = new CommonDBRuleLoader();
        }
        else if(ruleLoaderType.equals("file")) {
            ruleLoader = new CommonFileRuleLoader("*.ql","../xuanwu-op/agent/rules/enabled");
        }
        else {
            throw new IllegalArgumentException("unknown rule loader type: "+(ruleLoaderType == null?"null":ruleLoaderType));
        }

        try {
            if(ruleLoaderType.equals("db")) {
                ruleSet = ruleLoader.loadRules("rule");
                logger.info( "load rules from database." );
            }
            else if(ruleLoaderType.equals("file")) {
                ruleSet = ruleLoader.loadRules(rule_table);
            }

        } catch (IOException e) {
            throw new IllegalStateException("Static init RuleSetManager failed.");
        }
    }


    /**
     * 用Tags过滤Rule Set全集
     * @param tags
     * @return
     */
    public static List<Rule> getRuleSetByTags(HashSet<String> tags) throws IOException {
        List<Rule> output = new Vector<Rule>();

        // FIXME: 测试每次请求重新loadRule对性能的影响
        //List<Rule> ruleSet = ruleLoader.loadRules();

        for(Rule rule:ruleSet) {
            HashSet<String> ruleTags = rule.getTags();

            // ruleTags为Null则直接filter掉
            if(null == ruleTags) {
                continue;
            }
            HashSet<String> intersection = CommonUtils.getIntersection(ruleTags,tags);

            if(intersection.isEmpty() == false) {
                output.add(rule);
            }
        }
        return output;
    }

    /**
     * 用type过滤Rule Set全集
     * @param type
     * @return
     * @throws IOException
     */
    public static List<Rule> getRuleSetByType(String type) throws IOException {
        List<Rule> output = new Vector<Rule>();
        int sizes = ruleSet.size();
        for (Rule rule:ruleSet) {
            int id = rule.getId();
            String smoketype = rule.getSmoketype();
            logger.info("RuleSetManager id=" + id + ",smoketype=" + smoketype);
        }
        for(Rule rule:ruleSet){
            String ruletype = rule.getSmoketype();
            if(ruletype.equals(type)){
                output.add(rule);
            }
        }
        return output;
    }

    /**
     *  刷新一遍ruleSet 全集
     */
    public static void refreshRuleSet() throws IOException {
        System.out.println("reload rule");
        ruleSet = ruleLoader.loadRules("rule");
    }
}
