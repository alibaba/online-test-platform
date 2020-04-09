package com.alibaba.test.steed.constant;

/**
 * Created by liyang on 2019/8/30.
 */
public interface RuleEndPoint {

    String EXECUTE = "/testruleengine/smoke";

    String REFRESH = "/rules/refreshRuleSet";

    String GET_RULE_SET = "/rules/getRuleSetByTags?type=&tags=";

}
