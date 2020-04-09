package com.alibaba.test.steed.domain;

/**
 * Created by liyang on 2019/8/29.
 */
public class RuleRequestDto {

    public String type;

    public RuleRequestData data = new RuleRequestData();

    public class RuleRequestData{

        public String request;

        public String response;

        public String taskid;

    }
}
