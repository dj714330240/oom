package com.coe.oom;

import com.coe.oom.listen.ApplicationStartupListen;
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
        SpringApplication springApplication = new SpringApplication(OomRegisterApplication .class);
        /*************启动跳转浏览器---监听---开发环境使用---不用可以注释掉*****************/
        springApplication.addListeners(new ApplicationStartupListen());
        /******************************************************************************/
        springApplication.run(args);
    }

}
