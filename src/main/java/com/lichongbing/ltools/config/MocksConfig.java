package com.lichongbing.ltools.config;

import com.github.javafaker.Faker;
import com.github.jsonzou.jmockdata.MockConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/12/5 12:24 下午
 * @description: TODO
 */
@Configuration
public class MocksConfig {


    @Bean
    public Faker getFaker() {
        return new Faker(Locale.CHINA);
    }

    @Bean
    public MockConfig getMockConfig() {
        return  new MockConfig()
                .subConfig("age")
                // 设置 int 的范围
                .intRange(1, 100)
                .subConfig("email")
                // 设置生成邮箱正则
                .stringRegex("[a-z0-9]{5,15}\\@\\w{3,5}\\.[a-z]{2,3}")
                .globalConfig();
    }




}
