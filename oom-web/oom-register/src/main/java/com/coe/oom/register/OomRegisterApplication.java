package com.coe.oom.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/***
 * @Description:  注册中心
 * @Param:
 * @return:
 * @Author: 邓太阳
 * @Date: 2019-05-09 17:49
 */
@EnableEurekaServer
@SpringBootApplication
public class OomRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OomRegisterApplication.class, args);
    }

}
