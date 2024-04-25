package com.hws.manager;


import com.hws.logs.annotation.EnableLogAspect;
import com.hws.manager.properties.MinioProperties;
import com.hws.manager.properties.UserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//扫描外部的包
@ComponentScan(basePackages = {"com.hws"})
@EnableScheduling    //TODO 定时任务
@EnableLogAspect
//添加配置类
@EnableConfigurationProperties(value = {UserProperties.class, MinioProperties.class})
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }
}
