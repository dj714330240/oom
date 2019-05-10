package com.coe.oom.listen;

import com.coe.oom.util.StepExecutorUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @program: oom
 * @description: 启动监听
 * @author: 邓太阳
 * @create: 2019-05-10 11:26
 **/
public class ApplicationStartupListen implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext ac = contextRefreshedEvent.getApplicationContext();
        StepExecutorUtil StepExecutorUtil = ac.getBean(StepExecutorUtil.class);
        Thread thread = new Thread(StepExecutorUtil);
        thread.start();
    }
}
