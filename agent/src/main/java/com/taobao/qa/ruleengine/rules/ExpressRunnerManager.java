package com.taobao.qa.ruleengine.rules;
import com.taobao.qa.ruleengine.core.Context;
import org.springframework.stereotype.Component;

@Component
public class ExpressRunnerManager {
    private static RuleRunner runner;

    public static RuleRunner getRunner() throws Exception {
        if(runner == null) {
            runner = new RuleRunner();
            runner.init();
        }
        return runner;
    }

}
