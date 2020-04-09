package com.taobao.qa.ruleengine.core;

import com.taobao.qa.ruleengine.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.io.IOException;

public class ApplicationStartupListener implements ApplicationListener<ApplicationStartedEvent> {
    private static Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            PropertyUtil.loadAllProperties();
            logger.info("load all properties...done.");
        } catch (IOException e) {
            logger.error( "load properties error {}", e );
        }
    }

}
