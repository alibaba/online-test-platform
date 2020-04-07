package com.alibaba.test.steed.domain;

import com.alibaba.test.steed.model.Rule;

import java.util.ArrayList;

/**
 * Created by liyang on 2019/8/29.
 */
public class RuleResponseData {

    public ArrayList<RuleVo> executedRuleSet = new ArrayList<>();

    public ArrayList<RuleVo> sikppedRuleSet = new ArrayList<>();

    public ArrayList<RuleVo> ruleSet = new ArrayList<>();

}
