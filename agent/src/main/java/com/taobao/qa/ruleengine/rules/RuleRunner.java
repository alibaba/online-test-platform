package com.taobao.qa.ruleengine.rules;

import com.alibaba.fastjson.JSONArray;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.InstructionSet;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.taobao.qa.ruleengine.core.Context;
import com.taobao.qa.ruleengine.rules.primitives.*;
import com.taobao.qa.ruleengine.utils.CommonUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class RuleRunner {
    private static final Logger logger = LoggerFactory.getLogger(RuleRunner.class);

    private ExpressRunner runner;

    public RuleRunner() {
        this.runner = new ExpressRunner();
    }

    public void init() throws Exception {

 		runner.addOperatorWithAlias("小于","<","$1 < $2 不符合");
		runner.addOperatorWithAlias("大于",">","$1 > $2 不符合");

        runner.addFunctionOfClassMethod("最小值约束", ConstraintsClassMethods.class.getName(),
                "min", new String[]{String.class.getName(),Double.class.getName()}, null);

        runner.addFunctionOfClassMethod("最大值约束", ConstraintsClassMethods.class.getName(),
                "max", new String[]{String.class.getName(),Double.class.getName()}, null);

        runner.addFunctionOfClassMethod("取值范围约束", ConstraintsClassMethods.class.getName(),
                "inRange", new String[]{String.class.getName(),Double.class.getName(),Double.class.getName()}, null);

        runner.addFunctionOfClassMethod("取值集合约束", ConstraintsClassMethods.class.getName(),
                "inSet", new Class[]{String.class,String[].class}, null);

        runner.addFunctionOfClassMethod("isInteger", ConstraintsClassMethods.class.getName(),
                "isInteger", new String[]{String.class.getName()}, null);

        runner.addFunctionOfClassMethod("为非负整数", ConstraintsClassMethods.class.getName(),
                "isNonNegativeInteger", new String[]{String.class.getName()}, null);

        runner.addFunctionOfClassMethod("isFloat", ConstraintsClassMethods.class.getName(),
                "isFloat", new String[]{String.class.getName()}, null);

        runner.addFunctionOfClassMethod("isNumeric", ConstraintsClassMethods.class.getName(),
                "isNumeric", new String[]{String.class.getName()}, null);

        runner.addFunctionOfClassMethod("matchPattern", ConstraintsClassMethods.class.getName(),
                "matchPattern", new String[]{String.class.getName(),String.class.getName()}, null);

        runner.addFunctionOfClassMethod("为空", ConstraintsClassMethods.class.getName(),
                "isEmpty", new String[]{String.class.getName()}, null);

        runner.addFunctionOfClassMethod("format", String.class.getName(),
                "format", new String[]{String.class.getName(),Object[].class.getName()}, null);

         runner.addFunctionOfClassMethod("字符串比较", CommonClassMethods.class.getName(),
                "strCompare", new String[]{String.class.getName(), String.class.getName()}, null);

        runner.addFunctionOfClassMethod("字符串切分", CommonClassMethods.class.getName(),
                "strSplit", new String[]{String.class.getName(), String.class.getName()}, null);

         runner.addFunctionOfClassMethod("整数转为字符串",CommonClassMethods.class.getName(),
                 "intToString",new String[]{Integer.class.getName()},null);

         runner.addFunctionOfClassMethod("getByIndex",CommonClassMethods.class.getName(),
                 "getByIndex",new String[]{String[].class.getName(),Integer.class.getName()},null);

        runner.addFunctionOfClassMethod("整除", ConstraintsClassMethods.class.getName(),
                "diver", new String[]{String.class.getName(), String.class.getName()}, null);

        runner.addFunctionOfClassMethod("相乘", ConstraintsClassMethods.class.getName(),
                "multiply", new String[]{String.class.getName(), String.class.getName()}, null);

        runner.addFunctionOfClassMethod("字符串无序是否相等比较",ConstraintsClassMethods.class.getName(),
                "strArrEqualNoOrder",new String[]{String[].class.getName(),String[].class.getName()},null);

        runner.addFunctionOfClassMethod("第一个字符数组中存在第二个字符数组中不存在的字段",ConstraintsClassMethods.class.getName(),
                "strArrsubtract",new String[]{String[].class.getName(),String[].class.getName()},null);

        runner.addFunctionOfClassMethod("ltrim",CommonClassMethods.class.getName(),
                "ltrim",new String[]{String.class.getName(),String.class.getName()},null);

        runner.addFunctionOfClassMethod("rtrim",CommonClassMethods.class.getName(),
                "rtrim",new String[]{String.class.getName(),String.class.getName()},null);

        runner.addFunctionOfClassMethod("urldecode",CommonClassMethods.class.getName(),
                "urldecode",new String[]{String.class.getName(),String.class.getName()},null);

        runner.addFunctionOfClassMethod("urlencode",CommonClassMethods.class.getName(),
                "urlencode",new String[]{String.class.getName(),String.class.getName()},null);

        runner.addFunctionOfClassMethod("json串key获取",CommonClassMethods.class.getName(),
                "executeJson",new String[]{String.class.getName(),String.class.getName()},null);

        runner.addFunctionOfClassMethod("数组A是否包含元素B",CommonClassMethods.class.getName(),
                "contains",new String[]{String[].class.getName(),String.class.getName()},null);

        runner.addFunctionOfClassMethod("数组A是否不包含数组B的任何元素",CommonClassMethods.class.getName(),
                "containsNon",new String[]{String[].class.getName(),String[].class.getName()},null);

        runner.addFunctionOfClassMethod("数组A和B是否有交集",CommonClassMethods.class.getName(),
                "containsAny",new String[]{String[].class.getName(),String[].class.getName()},null);

        runner.addFunctionOfClassMethod("数组B是否是数组A的子集",CommonClassMethods.class.getName(),
                "containsAll",new String[]{String[].class.getName(),String[].class.getName()},null);

        runner.addFunctionOfClassMethod("获取json对应指定key值",CommonClassMethods.class.getName(),
                "getMultiFieldFromJson",new String[]{JSONArray.class.getName(),String.class.getName()},null);

        runner.addFunctionOfClassMethod("json数组中是否存在指定值",CommonClassMethods.class.getName(),
                "containsValue",new String[]{JSONArray.class.getName(),String.class.getName()},null);

        runner.addFunctionOfClassMethod("整数字符串比较",CommonClassMethods.class.getName(),
                "stringIntCompare",new String[]{String.class.getName(), String.class.getName()},null);

        runner.addFunctionOfClassMethod("字符串是否为正整数",CommonClassMethods.class.getName(),
                "isNumeric",new String[]{String.class.getName()},null);


        runner.addFunction("记录错误",new OperatorMessageRecorder());

        runner.addFunction("跳过当前规则",new OperatorSkipRule());

        logger.debug("[Exit]init()");
    }

    public boolean when(Rule rule, Context context) throws Exception {
        logger.debug("[Enter]when() for rule {}",rule.getName());
        try {
            Boolean result = (Boolean)runner.execute(rule.getWhenExpression(),context.getExpressContext(),null,true,false);
            return result;
        } catch(Exception e) {
            rule.setState("Exception");
            String msg = String.format("规则解析异常(When part), 规则名[%s],(id=%s) 异常信息: \n%s",
                    rule.getName(),rule.getId(),CommonUtils.getCauseOfException(e));
            logger.error(msg);
            rule.appendMessage(msg);
            return true;
        }
    }

    public boolean verify(Rule rule, Context context) throws Exception {
        logger.debug("[Enter]verify() for rule {}",rule.getName());

        // Record current rule
        context.put("current_rule",rule);

        // 跳过Exception状态的Rule, 直接返回False
        if(rule.isException()) {
            logger.debug("Rule \"%s(id=%s)\" is in Exception state, thus return false.",rule.getName(),rule.getId());
            return false;
        }

        // 执行Rule的Verify Part
        try {
            ArrayList<String> errList = new ArrayList<String>();
            InstructionSet instructionSet = null;
            try {
                instructionSet = runner.parseInstructionSet(rule.getVerifyExpression());
            } catch(Exception e) {
                String msg = String.format("规则解析异常(Verify part), 规则名[%s],(id=%s) 异常信息: \n%s",
                        rule.getName(),rule.getId(),CommonUtils.getCauseOfException(e));
                logger.error(msg);
                rule.appendMessage(msg);
                return false;
            }

            Boolean result = (Boolean) runner.execute(instructionSet, context.getExpressContext(), errList,false,false,null);
            if(rule.isSkipped()) {
                rule.setState("SKIP");
            }
            else if(result) {
                rule.setState("PASS");
            }
            else {
                rule.setState("FAIL");
            }
            return result;
        } catch (Exception e) {
            rule.setState("Exception");
            String msg = String.format("规则执行异常(Verify part), 规则名[%s],(id=%s) 异常信息:%s",
                    rule.getName(),rule.getId(),CommonUtils.getCauseOfException(e));
            logger.error(msg);

            rule.appendMessage(msg);
            return false;
        }
    }

    public void then(Rule rule, Context context) throws Exception {
        logger.debug("[Enter]then() for rule {}",rule.getName());

        // 跳过Exception Rule的执行
        if(rule.isException()) {
            logger.debug("Rule \"%s(id=%s)\" is in Exception state, thus return false.",rule.getName(),rule.getId());
            return;
        }

        String expression = rule.getThenExpression();
        // Then Part是可选的，有可能为null
        if(expression == null) {
            return;
        }
        try {
            runner.execute(expression,context.getExpressContext(),null,true,false);
        } catch(Exception e) {
            rule.setState("Exception");
            String msg = String.format("Exception during running the Then() part for rule: %s (id=%s)%s",
                    rule.getName(),rule.getId(),CommonUtils.getStackInfoFromException(e));
            logger.error(msg);
            rule.appendMessage(msg);
            return;
        }
    }
}
