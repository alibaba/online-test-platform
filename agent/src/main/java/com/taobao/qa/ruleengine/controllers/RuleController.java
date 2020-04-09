package com.taobao.qa.ruleengine.controllers;

import com.taobao.qa.ruleengine.biz.RuleService;
import com.taobao.qa.ruleengine.model.AjaxResult;
import com.taobao.qa.ruleengine.model.KgbAnalyzeResult;
import com.taobao.qa.ruleengine.model.RuleEntity;
import com.taobao.qa.ruleengine.rules.Rule;
import com.taobao.qa.ruleengine.rules.RuleSetManager;
import com.taobao.qa.ruleengine.rules.primitives.CommonClassMethods;
import com.taobao.qa.ruleengine.utils.CommonConstants;
import com.taobao.qa.ruleengine.utils.CommonUtils;
import com.taobao.qa.ruleengine.utils.IpUtil;
import com.taobao.qa.ruleengine.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/rules")
public class RuleController {
    private static final Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
    private RuleService ruleService;

    @RequestMapping(value="/getRuleSetByTags", method= RequestMethod.GET)
    KgbAnalyzeResult getRuleSetByTags(@RequestParam("type") String type, @RequestParam("tags") String tags) {
        Map<String, Object> resultData = new HashMap<String, Object>();
        logger.info("getRuleSetByTags type="+type+",tags="+tags);

        List<Rule> ruleSet = new Vector<Rule>();
        try {
            logger.info("getRuleSetByTags type="+type+",tags="+tags);
            // tags存在则用tags做过滤
            if(tags !=null && !tags.isEmpty()) {
                HashSet<String> tagSet = StringUtil.csvStringToHashSet(tags);
                ruleSet = RuleSetManager.getRuleSetByTags(tagSet);
                logger.info("getRuleSetByTags size="+ruleSet.size());
                resultData.put("tags", tagSet);
            }else{
                ruleSet = RuleSetManager.getRuleSetByType(type);
                logger.info("getRuleSetByType size="+ruleSet.size());
                resultData.put("tags",tags);
            }

            resultData.put("ruleSet", ruleSet);

            KgbAnalyzeResult result = new KgbAnalyzeResult(200, "");
            result.setData(resultData);
            return result;
        }
        catch(Exception e) {
            // 返回 Exception
            String msg = String.format("Exception: %s\n%s",
                    e.getMessage(), CommonUtils.getStackInfoFromException(e));
            logger.error(msg);
            KgbAnalyzeResult result = new KgbAnalyzeResult(400,msg);
            return result;
        }
    }

    @RequestMapping(value="/getAllRulesByProduct")
    public AjaxResult getAllRulesByProduct(@RequestParam("table")String table,@RequestParam("product")String prod) {
        logger.info("getAllRulesBySubproduct start table"+table+",product="+prod);
        AjaxResult result = new AjaxResult();
        try {
            List<RuleEntity> ruleEntityList = ruleService.getAllRulesByProduct(table, prod);
            logger.info("getAllRulesByProduct ruleEntityList size is "+ ruleEntityList.size());
            result.setSuccess(ruleEntityList);
            logger.info("getAllRulesByProduct success");
        }catch (Exception e) {
            result.setFailure(e.getMessage());
            logger.info("getAllRulesByProduct exception");
            e.printStackTrace();
        }
        logger.info("getAllRulesBySubproduct end");
        return result;
    }

    @RequestMapping(value="/getAllRulesBySubproduct")
    public AjaxResult getAllRulesBySubproduct(@RequestParam("table")String table,@RequestParam("subproduct")String subpro) {
        AjaxResult result = new AjaxResult();
        try {
            List<RuleEntity> ruleEntityList = ruleService.getAllRulesBySubproduct(table, subpro);
            logger.info("getAllRulesBySubproduct ruleEntityList size is "+ruleEntityList.size());
            result.setSuccess(ruleEntityList);
        }catch (Exception e) {
            result.setFailure(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/getRuleById",method = RequestMethod.GET)
    public AjaxResult getRuleEntityById(@RequestParam("table") String table,@RequestParam("id") int id) {
        logger.info("getRuleEntityById table="+table+",id="+id);
        AjaxResult result = new AjaxResult();
        try {
            RuleEntity res = ruleService.getRuleEntityById(table, id);
            logger.info("getRuleEntityById table="+table+",res is "+res.toString());
            result.setSuccess(res);
        }catch (Exception e) {
            result.setFailure(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/updateRule")
    public AjaxResult updateRule(@RequestParam("table") String table,@RequestBody RuleEntity RuleEntity) {
        AjaxResult result = new AjaxResult();
        try {
            if (RuleEntity.getTime() == null || RuleEntity.getTime().isEmpty()) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                RuleEntity.setTime(df.format(new Date()));
            }
            ruleService.update(RuleEntity);
            result.setSuccess("Success");
        }catch (Exception e) {
            result.setFailure(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/getRuleCategories", method = RequestMethod.GET)
    public AjaxResult getRuleCategories() {
        AjaxResult result = new AjaxResult();
        try {
            Set<String> res = CommonConstants.RULE_CATEGORY.keySet();
            result.setSuccess(res);
        }catch (Exception e) {
            result.setFailure(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/enableRule")
    public AjaxResult enableRule(@RequestParam("table") String table,@RequestParam("id") int id){
        AjaxResult result = new AjaxResult();
        try {
            ruleService.enableRuleById(table, id);
            result.setSuccess("enable table " + table + " id=" + id + " success");
        }catch (Exception e) {
            result.setFailure(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/disableRule")
    public AjaxResult disableRules(@RequestParam("table") String table,@RequestParam("id") int id){
        AjaxResult result = new AjaxResult();
        ruleService.disableRuleById(table, id);
        result.setSuccess("disable table " + table + " id=" + id + " success");
        return result;
    }

    @RequestMapping(value="/refreshRuleSet")
    public AjaxResult refreshRuleSet() {
        System.out.println("refresh ruleset");
        AjaxResult result = new AjaxResult();
        String ip = IpUtil.getLocalIp();
        try {
            RuleSetManager.refreshRuleSet();
            result.setSuccess("IP:" +ip + " refreshRuleSet success. ");
        } catch (Exception e) {
            String errMsg = "IP:" + ip + " refreshRuleSet failed. " + CommonUtils.getStackInfoFromException(e);
            result.setFailure(errMsg);
        }
        return result;
    }

    @RequestMapping(value="/getAllRulesByTable")
    public AjaxResult getAllRulesByTable(@RequestParam("table")String table) {
        AjaxResult result = new AjaxResult();
        List<RuleEntity> entityList = ruleService.getAllRulesByTable(table);
        logger.info("getAllRulesByTable size is " + entityList.size());
        result.setSuccess(entityList);
        return result;
    }
}
