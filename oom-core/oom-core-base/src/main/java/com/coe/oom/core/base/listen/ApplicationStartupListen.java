package com.coe.oom.core.base.listen;

import com.coe.oom.core.base.util.StepExecutorUtil;
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

    private String url;

    public  ApplicationStartupListen(String url){
        this.url=url;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext ac = contextRefreshedEvent.getApplicationContext();
        StepExecutorUtil stepExecutorUtil = ac.getBean(StepExecutorUtil.class);
        stepExecutorUtil.setUrl(url);
        Thread thread = new Thread(stepExecutorUtil);
        thread.start();
    }


}
