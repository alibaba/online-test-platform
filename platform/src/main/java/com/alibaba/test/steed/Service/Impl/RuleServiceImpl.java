package com.alibaba.test.steed.service.impl;

import com.alibaba.test.steed.service.IRuleService;
import com.alibaba.test.steed.constant.Configuration;
import com.alibaba.test.steed.constant.RuleEndPoint;
import com.alibaba.test.steed.dao.RuleMapper;
import com.alibaba.test.steed.model.RuleExample;
import com.alibaba.test.steed.model.RuleWithBLOBs;
import com.alibaba.test.steed.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liyang on 2019/8/29.
 */
@Service
public class RuleServiceImpl implements IRuleService {

    @Autowired
    public RuleMapper ruleMapper;

    @Autowired
    Configuration configuration;

    @Override
    public List<RuleWithBLOBs> selectAll() {
        RuleExample ruleExample = new RuleExample();
        List<RuleWithBLOBs> rule = ruleMapper.selectByExampleWithBLOBs( ruleExample );
        return rule;
    }

    @Override
    public int addRule(RuleWithBLOBs ruleWithBLOBs) {
        int ret = ruleMapper.insert( ruleWithBLOBs );
        return ret;
    }

    @Override
    public int updateRule(RuleWithBLOBs ruleWithBLOBs) {
        int ret = ruleMapper.updateByPrimaryKeySelective( ruleWithBLOBs );
        return ret;
    }

    @Override
    public int deleteRule(Integer ruleId) {
        int ret = ruleMapper.deleteByPrimaryKey( ruleId );
        return ret;
    }

    @Override
    public String queryRuleEngine(String query) {
        String ruleExecute = configuration.address + RuleEndPoint.EXECUTE;
        String ret = HttpClient.post( ruleExecute, query );
        return ret;
    }

    @Override
    public String refreshRuleSets() {
        String refreshRule = configuration.address + RuleEndPoint.REFRESH;
        return HttpClient.get( refreshRule );
    }

    @Override
    public String getRuleSetByType(String ruleType) {
        return HttpClient.get( configuration.address + RuleEndPoint.GET_RULE_SET + ruleType );
    }
}
