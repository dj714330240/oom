package com.coe.oom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/*** 
 * @Description: 订单作业系统管理端-主程序
 * @Param:  
 * @return:  
 * @Author: 邓太阳 
 * @Date: 2019-05-09 18:15
 */
@EnableEurekaClient
@SpringBootApplication
public class OomAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(OomAdminApplication.class, args);
	}

}
