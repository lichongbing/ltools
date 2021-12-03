package com.lichongbing.ltools.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.spring.SpringUtil;

import com.lichongbing.ltools.pool.FTPClientFactory;
import com.lichongbing.ltools.pool.FtpClientDataSource;
import com.lichongbing.ltools.pool.FtpClientPool;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ftp连接配置信息
 */
@Configuration
@ConditionalOnProperty(name = "ftp.enable",havingValue = "true",matchIfMissing = true)
public class FtpConfiguration {




    //初始化ftp 连接配置
    @Bean
    public FtpConfig ftpConfig(){
        return new FtpConfig();
    }


    //初始化ftp 数据源配置
    @Bean
    public FtpPoolConfig ftpPoolConfig(){
        return new FtpPoolConfig();
    }


    /**
     * 连接池配置
     * @param ftpPoolConfig
     * @return
     */
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig(FtpPoolConfig ftpPoolConfig){
        GenericObjectPoolConfig genericObjectPoolConfig=new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(ftpPoolConfig.getMaxIdle());
        genericObjectPoolConfig.setMaxTotal(ftpPoolConfig.getMaxTotal());
        genericObjectPoolConfig.setMinIdle(ftpPoolConfig.getMinIdle());
        genericObjectPoolConfig.setJmxEnabled(false);
        genericObjectPoolConfig.setTestOnBorrow(true);
        genericObjectPoolConfig.setTestOnReturn(true);
        return genericObjectPoolConfig;
    }

    //连接池对象工厂
    @Bean
    public FTPClientFactory ftpClientFactory(FtpConfig ftpConfig){
        return  new FTPClientFactory(ftpConfig);
    }


    //连接池对象管理器
    @Bean
    public FtpClientPool ftpClientPool(FTPClientFactory factory, GenericObjectPoolConfig config){
        return new FtpClientPool(factory,config);
    }

    /**
     * 连接池配置
     */
    @Data
    @ConfigurationProperties(prefix = "ftp.pool")
    public static class FtpPoolConfig{
        //连接池中最大连接数
        private int maxTotal = 8;
        //连接池中最大空闲的连接数,默认为8
        private int maxIdle = 8;
        //连接池中最少空闲的连接数,默认为0
        private int minIdle = 0;
    }


    /**
     * ftp配置
     */
    @Data
    @ConfigurationProperties(prefix = "ftp.client")
    public static class FtpConfig{

        //连接地址
        private String ip="127.0.0.1";

        //端口
        private Integer port=21;

        //用户名
        private String username="anonymous";

        //密码
        private String password="";

        //ftp连接模式，主动模式和被动模式, 0主动，1被动
        private Integer model=0;

        //编码格式
        private String charset= CharsetUtil.UTF_8;

        //连接超时时长，单位毫秒
        private Integer connectionTimeout=3000;

        /**
         * Socket连接超时时长，单位毫秒
         */
        private long soTimeout=3000;

    }



    @Bean
    public SpringUtil mySpringUtil(){
        return new SpringUtil();
    }


    /**
     * 通过
     * @param ftpClientPool
     * @return
     */
    @Bean
    public FtpClientDataSource ftpClientDataSource(FtpClientPool ftpClientPool){
        return new FtpClientDataSource(ftpClientPool);
    }










}
