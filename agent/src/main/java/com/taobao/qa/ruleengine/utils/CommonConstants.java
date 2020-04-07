package com.taobao.qa.ruleengine.utils;

import java.util.HashMap;
import java.util.Map;

public class CommonConstants {

    public static final Map<String,Integer> RULE_CATEGORY = new HashMap<String,Integer>();
    static {
        RULE_CATEGORY.put("字段取值验证",100);
        RULE_CATEGORY.put("功能逻辑验证",101);
        RULE_CATEGORY.put("异常验证",102);
        RULE_CATEGORY.put("其他验证",103);
    }
}
