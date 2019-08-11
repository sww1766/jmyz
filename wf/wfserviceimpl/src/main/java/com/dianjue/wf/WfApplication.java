package com.dianjue.wf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.dianjue.wf.dao*")
@EnableDiscoveryClient
@EnableCaching
@EnableFeignClients
@ComponentScan(basePackages = {
    "com.dianjue.wf",
    "org.activiti"})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class WfApplication {
  public static void main(String[] args) {
    SpringApplication.run(WfApplication.class, args);
  }

}