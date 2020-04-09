package com.alibaba.test.steed.controller;

import com.alibaba.test.steed.domain.AjaxResult;
import com.alibaba.test.steed.service.IRuleService;
import com.alibaba.test.steed.model.RuleWithBLOBs;
import com.alibaba.test.steed.utils.HttpClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by liyang on 2019/8/28.
 */
@RestController
@RequestMapping("/api/rule")
public class RuleController {

    private static final Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
    public IRuleService ruleService;

    @RequestMapping("/selectAllByPage")
    public PageInfo<List<RuleWithBLOBs>> selectAllByPage(HttpServletRequest request,
                                                            @RequestParam(value = "pageNum") Integer pageNum,
                                                                @RequestParam(value = "pageSize") Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<RuleWithBLOBs> persons =    ruleService.selectAll();
        PageInfo page = new PageInfo(persons);
        return page;
    }

    @RequestMapping("/addRule")
    public AjaxResult addRule(@RequestBody RuleWithBLOBs ruleWithBLOBs){
        int ret =  ruleService.addRule( ruleWithBLOBs );
        ruleService.refreshRuleSets();
        AjaxResult result = new AjaxResult();
        result.setSuccess( ruleWithBLOBs.getId() );
        return result;
    }

    @RequestMapping("/editRule")
    public AjaxResult editRule(@RequestBody RuleWithBLOBs ruleWithBLOBs){
        int ret = ruleService.updateRule( ruleWithBLOBs );
        ruleService.refreshRuleSets();
        AjaxResult result = new AjaxResult();
        result.setSuccess( ret );
        return result;
    }

    @RequestMapping("/deleteRule")
    public AjaxResult editRule(@RequestParam(value = "ruleId") Integer ruleId){
        int ret = ruleService.deleteRule( ruleId );
        ruleService.refreshRuleSets();
        AjaxResult result = new AjaxResult();
        result.setSuccess( ret );
        return result;
    }

    @RequestMapping("/refreshRuleSets")
    public AjaxResult refreshRuleSets(){
        ruleService.refreshRuleSets();
        AjaxResult result = new AjaxResult();
        result.setSuccess( "Rule sets refreshed." );
        return result;
    }

    @RequestMapping("/sendRequest")
    public AjaxResult sendRequest(@RequestBody Map<String, Object> params){
        String url = (String) params.get("url");
        String res = HttpClient.get(url);
        AjaxResult result = new AjaxResult();
        result.setData(res);
        return result;
    }

    @RequestMapping("/sendRuleRequest")
    public AjaxResult sendRuleRequest(@RequestBody Map<String, Object> params){
        String url = (String) params.get("url");
        String data = (String) params.get("data");
        String res = HttpClient.post(url, data);
        AjaxResult result = new AjaxResult();
        result.setData(res);
        return result;
    }

}
