package com.coe.oom.core.base.util;

import org.springframework.stereotype.Component;

/**
 * @program: oom
 * @description: 执行操作
 * @author: 邓太阳
 * @create: 2019-05-10 11:23
 **/
@Component
public class StepExecutorUtil implements Runnable {

    //跳转路径
    private String url;

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
                Runtime.getRuntime().exec("cmd /c start "+url);
            }else if (os.toLowerCase().startsWith("mac")){
                Runtime.getRuntime().exec("open "+url);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
