package com.coe.oom.core.conf;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * 用于获取applicationContext
 * @author lqg
 *
 */
@Service
public class SpringManager implements ApplicationListener<ContextRefreshedEvent> {

	private static ApplicationContext applicationContext = null;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (applicationContext == null) {
			applicationContext = event.getApplicationContext();
		}
	}

	/*
	 * ApplicationContext context=
	 * ContextLoader.getCurrentWebApplicationContext();//尝试下这个方法
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	 

}
