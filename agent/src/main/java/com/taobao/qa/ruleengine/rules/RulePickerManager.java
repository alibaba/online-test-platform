package com.taobao.qa.ruleengine.rules;

import com.taobao.qa.ruleengine.core.Context;
import com.taobao.qa.ruleengine.utils.CommonUtils;
import org.slf4j.Logger;

public class RulePickerManager {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(RulePickerManager.class);

    public static IRulePicker createRulePicker(Context context) {
        try {
            IRulePicker rulePicker = new CommonRulePicker(context);
            return rulePicker;
        }
        catch(Exception e) {
            String errmsg = String.format("Failed to create a rulePicker, errmsg:\n%s\n%s",
                    e.getMessage(), CommonUtils.getStackInfoFromException(e));
            logger.error(errmsg);
            return null;
        }
    }
}
