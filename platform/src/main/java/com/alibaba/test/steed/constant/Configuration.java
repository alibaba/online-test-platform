package com.alibaba.test.steed.constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by liyang on 2019/8/30.
 */
@Component
public class Configuration {

    @Value("${rule.address}")
    public String address;

    @Value( "${query.interval}" )
    public Integer queryInterval;

    @Value( "${query.sample.count}" )
    public Integer querySampleCount;

    @Value( "${task.thread.count}" )
    public Integer queryPoolSize;

    @Value( "${rule.thread.count}" )
    public Integer rulePoolSize;

    @Value( "${task.rule.minimum}" )
    public Integer minimumRule;

    @Value( "${query.path}" )
    public String queryPath;

}
