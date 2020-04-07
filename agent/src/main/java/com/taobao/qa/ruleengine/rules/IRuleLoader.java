package com.taobao.qa.ruleengine.rules;

import java.io.IOException;
import java.util.List;

public interface IRuleLoader {
    public List<Rule> loadRules(String type) throws IOException;
}
