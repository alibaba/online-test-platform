package com.alibaba.test.steed.service;

import com.alibaba.test.steed.model.RuleWithBLOBs;

import java.util.List;

/**
 * Created by liyang on 2019/8/29.
 */
public interface IRuleService {

    List<RuleWithBLOBs> selectAll();

    int addRule(RuleWithBLOBs ruleWithBLOBs);

    int updateRule(RuleWithBLOBs ruleWithBLOBs);

    int deleteRule(Integer ruleId);

    String getRuleSetByType(String ruleType);

    String queryRuleEngine(String query);

    String refreshRuleSets();

}
