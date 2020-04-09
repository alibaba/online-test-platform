package com.alibaba.test.steed;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by liyang on 2019/8/27.
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.alibaba.test.steed.dao"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run( Application.class, args);
    }
}
