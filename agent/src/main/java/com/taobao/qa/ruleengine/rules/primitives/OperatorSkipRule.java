package com.taobao.qa.ruleengine.rules.primitives;

import com.ql.util.express.ArraySwap;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.op.OperatorBase;
import com.taobao.qa.ruleengine.rules.Rule;

public class OperatorSkipRule extends OperatorBase {

    @Override
    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
        Rule currentRule = (Rule) parent.get("current_rule");
        currentRule.setSkipped(true);
        return null;
    }
}