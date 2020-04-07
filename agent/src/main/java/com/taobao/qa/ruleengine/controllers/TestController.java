package com.taobao.qa.ruleengine.controllers;

import com.alibaba.fastjson.JSON;
import com.taobao.qa.ruleengine.core.*;
import com.taobao.qa.ruleengine.model.KgbAnalyzeResult;
import com.taobao.qa.ruleengine.utils.CommonUtils;
import com.taobao.qa.ruleengine.utils.PropertyUtil;
import com.taobao.qa.ruleengine.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.taobao.qa.ruleengine.core.Context;

@RestController
@RequestMapping("/testruleengine")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value={"/smoke"},method= RequestMethod.POST)
    public KgbAnalyzeResult testRuleEngine(@RequestBody Map<String,Object> data) {
        logger.info( "Row data is {}" , JSON.toJSONString(data) );
        long starttime = System.currentTimeMillis();
        try {
            IContextInitializer contextInitializer = null;
            String type = (String) data.get("type");
            Map<String,Object> reqData = (Map<String, Object>) data.get("data");
            String taskid = (String) reqData.get("taskid");
            reqData.put("type",type);

            logger.info("application is {}, run DefaultContextInitializer", type);
            contextInitializer = new DefaultContextInitializer();

            Analyzer analyzer = new Analyzer(contextInitializer);
            analyzer.init(reqData);

            KgbAnalyzeResult result = analyzer.analyze();

            Context context = analyzer.getContext();
            HashMap<String, Object> debugMap = context.getDebugMap();
            result.setDebugMap(debugMap);

            long endtime = System.currentTimeMillis();
            long costtime = endtime - starttime;
            logger.info("Taskid:" + taskid + ",costtime:" + String.valueOf(costtime));
            return result;
        }
        catch (Exception e) {
            String msg = String.format("Exception during rule Analysis. \n%s\n%s",
                    e.getMessage(), CommonUtils.getStackInfoFromException(e));
            logger.error(msg);
            logger.error("TestController deal error,exception message is :");
            e.printStackTrace();
            logger.error("request data is " + data.toString());
            int maxMsgLength = Integer.parseInt(PropertyUtil.getProperty("max.message.length"));
            if(msg.length() > maxMsgLength) {
                msg = StringUtil.truncateString(msg,maxMsgLength);
            }

            KgbAnalyzeResult result = new KgbAnalyzeResult(400,msg);

            Map<String,Object> reqData = (Map<String, Object>) data.get("data");
            String taskid = (String) reqData.get("taskid");
            long endtime = System.currentTimeMillis();
            long costtime = endtime - starttime;

            logger.info("taskid:" + taskid + ",cost time:" + String.valueOf(costtime));

            return result;
        }
    }

}
