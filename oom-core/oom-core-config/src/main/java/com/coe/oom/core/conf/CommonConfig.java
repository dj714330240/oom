package com.coe.oom.core.conf;


import com.coe.oom.core.base.entity.BasicConfig;
import com.coe.oom.core.base.util.FileOperationUtil;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * 基础配置
 * @author lqg
 *
 */
@Configuration
public class CommonConfig {

	@Resource
	private Environment env;
	
	/**
	 * 配置 basicConfig
	 * @return
	 */
//	@Bean
//	public BasicConfig basicConfig(){
//        BasicConfig basicConfig = Binder.get(env).bind("basic.config",Bindable.of(BasicConfig.class)).get();
//        //配置文件服务器
//        FileOperationUtil.setFileUri(basicConfig.getFileUri());
//        return basicConfig;
//	}
	 
}
