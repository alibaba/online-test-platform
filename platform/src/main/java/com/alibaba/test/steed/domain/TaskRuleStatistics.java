package com.alibaba.test.steed.domain;

import java.util.ArrayList;

/**
 * Created by liyang on 2019/8/29.
 */
public class TaskRuleStatistics {

    public ArrayList<String> passed = new ArrayList<String>(  );

    public ArrayList<String> failed = new ArrayList<String>(  );

    public ArrayList<String> notrun = new ArrayList<String>(  );

    public int total = 0;

    public int  passNum = 0 ;

    public int retryQueryCount = 0;

    public ArrayList<String> error = new ArrayList<String>(  );

    public String finalStatus;

    public String passRatio;

    public String ruleCategoryId;

    public String ruleCategoryName;

    public String ruleLevel;

    public String ruleId;

    public String ruleName;

    public String debugtype;

}
