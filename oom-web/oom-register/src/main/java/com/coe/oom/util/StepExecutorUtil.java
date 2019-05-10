package com.coe.oom.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: oom
 * @description: 执行操作
 * @author: 邓太阳
 * @create: 2019-05-10 11:23
 **/
@Component
public class StepExecutorUtil implements Runnable {

    //配置的域名
    @Value("${eureka.instance.hostname}")
    private String host;

    //配置的端口
    @Value("${server.port}")
    private String port;

    @Override
    public void run() {
        startStreamTask();
    }
    /**
     * 项目启动后浏览器自动跳转
     */
    public void startStreamTask() {
        try {
            String os = System.getProperty("os.name");
            if(os.toLowerCase().startsWith("win")){
                Runtime.getRuntime().exec("cmd /c start http://"+host+":"+port);
            }else if (os.toLowerCase().startsWith("mac")){
                Runtime.getRuntime().exec("open http://"+host+":"+port);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
