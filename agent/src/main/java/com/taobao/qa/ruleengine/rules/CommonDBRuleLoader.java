package com.taobao.qa.ruleengine.rules;

import com.taobao.qa.ruleengine.biz.RuleService;
import com.taobao.qa.ruleengine.model.RuleEntity;
import com.taobao.qa.ruleengine.utils.CommonConstants;
import com.taobao.qa.ruleengine.utils.SpringUtil;
import com.taobao.qa.ruleengine.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class CommonDBRuleLoader implements IRuleLoader {
    private static final Logger logger = LoggerFactory.getLogger(CommonDBRuleLoader.class);

    public CommonDBRuleLoader(){}

    @Override
    public List<Rule> loadRules(String rule_table) throws IOException {
        List<Rule> output = new Vector<Rule>();
        RuleService ruleService = SpringUtil.getBean(RuleService.class);
        List<RuleEntity> ruleEntityList = ruleService.getAllRulesByTable(rule_table);

        for (RuleEntity ruleEntity : ruleEntityList) {

            // 如果Rule是disabled状态则直接跳过
            if(ruleEntity.getEnable() == 0) {
                continue;
            }

            // 获取rule的基本属性
            int id              = ruleEntity.getId();
            String name         = ruleEntity.getName();
            String whenExpr     = ruleEntity.getWhen();
            String verifyExpr   = ruleEntity.getVerify();
            String thenExpr     = ruleEntity.getThen();
            Integer level       = ruleEntity.getLevel();
            String category     = ruleEntity.getCategory().trim();
            Integer categoryId  = CommonConstants.RULE_CATEGORY.get(category);
            String application    = ruleEntity.getApplication();
            String debugtype    = ruleEntity.getDebugtype();
            String product      = ruleEntity.getProduct();
            Integer isautogen   = ruleEntity.isIsautogen();


            // Sanity Check: Category必须在预定义的集合中
            if(null == categoryId) {
                categoryId = 0;
            }

            // 获取 Rule 的 Tags
            HashSet<String> tags = null;
            String tagsStr = ruleEntity.getTags();
            if( null  == tagsStr ){
                tagsStr = ruleEntity.getApplication();
            }
            tagsStr = tagsStr.trim().replace("\n", "");
            tags = StringUtil.csvStringToHashSet(tagsStr);


            // whenExpr and verifyExpr是必须的
            if (name == null || whenExpr == null || verifyExpr == null) {
                String errMsg = String.format("invalid rule load from DB, id=%d", id);
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }

            // rule的retry属性
            boolean retry = ruleEntity.getRetry() == 1;
            output.add(new Rule(id,name, whenExpr, verifyExpr, thenExpr, tags, category, categoryId, level,retry,application,debugtype,product,isautogen));
        }
        return output;
    }
}