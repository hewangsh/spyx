package com.hws.order;

import com.hws.service.anno.EnableUserLoginAuthInterceptor;
import com.hws.service.anno.EnableUserTokenFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.hws.feign.cart","com.hws.feign.product","com.hws.feign.user"})   //TODO 远程调用
@EnableUserTokenFeignInterceptor  //TODO 远程调用传递请求头
@EnableUserLoginAuthInterceptor
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class , args) ;
    }
}