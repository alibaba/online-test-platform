package com.taobao.qa.ruleengine.core;

import com.taobao.qa.ruleengine.utils.PropertyUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.InetAddress;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceTransactionManagerAutoConfiguration.class, DataSourceAutoConfiguration.class })
@ImportResource(locations={"classpath:applicationContext.xml"})
@ComponentScan(basePackages = {
        "com.taobao.qa.ruleengine.controllers",
        "com.taobao.qa.ruleengine.rules",
        "com.taobao.qa.ruleengine.mapper",
        "com.taobao.qa.ruleengine.biz",
        "com.taobao.qa.ruleengine.utils",
        "com.taobao.qa.ruleengine.statical"})
@MapperScan("com.taobao.qa.ruleengine.mapper")
public class Application {
    final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(new ApplicationStartupListener());
        application.run(args);
        logger.info("Rule Engine Started...");
        logger.info("Rule Loader: {}", PropertyUtil.getProperty("rule.loader"));
    }

}
