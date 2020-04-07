package com.taobao.qa.ruleengine.biz;

import com.taobao.qa.ruleengine.mapper.RuleMapper;
import com.taobao.qa.ruleengine.model.RuleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService {

    @Autowired
    private RuleMapper ruleMapper;

    public int addRule(RuleEntity ruleEntity) {
        return ruleMapper.addRule(ruleEntity);
    }

    public RuleEntity getRuleEntityById(String table,int id) {
        return ruleMapper.getRuleEntitiesById(table,id);
    }

    public List<RuleEntity> getAllRulesByTable(String table) {
        return ruleMapper.getAllRulesByTable(table);
    }

    public List<RuleEntity> getAllRulesByProduct(String table, String product) {
        return ruleMapper.getAllRulesByProduct(table,product);
    }

    public List<RuleEntity> getAllRulesBySubproduct(String table, String smoketype) {
        return ruleMapper.getAllRulesBySubproduct(table,smoketype);
    }

    public int update(RuleEntity ruleEntity) {
        return ruleMapper.update(ruleEntity);
    }

    public int deleteRuleById(String table,int id) {
        return ruleMapper.deleteRuleById(table,id);
    }

    public int enableRuleById(String table, int id){
        return ruleMapper.enableRuleById(table,id);
    }

    public int disableRuleById(String table, int id){
        return ruleMapper.disableRuleById(table,id);
    }

    public int enableRulesByIds(List<Integer> ids) {
        return ruleMapper.enableRulesByIds(ids);
    }

    public int disableRulesByIds(List<Integer> ids) {
        return ruleMapper.disableRulesByIds(ids);
    }
}
