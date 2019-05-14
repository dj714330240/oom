package com.coe.oom;

import com.coe.oom.ent.user.UserEnt;
import com.coe.oom.user.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/*** 
 * @Description: 订单作业系统管理端-主程序
 * @Param:  
 * @return:  
 * @Author: 邓太阳 
 * @Date: 2019-05-09 18:15
 */
@EnableFeignClients
@RestController
@EnableEurekaClient
@SpringBootApplication
public class OomAdminApplication {

	@Resource
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(OomAdminApplication.class, args);
	}

	@RequestMapping("/user/getUserByCode")
	public UserEnt getUserByCode(String userCode) {
		return userService.getUserByUserCode(userCode);
	}

}
