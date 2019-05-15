package com.coe.oom.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/*** 
 * @Description: 配置中心 
 * @Param:  
 * @return:  
 * @Author: 邓太阳 
 * @Date: 2019-05-15 10:07
 */ 
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class OomCoreConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(OomCoreConfigApplication.class, args);
	}

}
