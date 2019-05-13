package com.coe.oom;

import com.coe.oom.base.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

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

	@RequestMapping("user/get")
	public Map<String,Object> get(Long id) {
		return userService.getUser(id);
	}

}
