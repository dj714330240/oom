package com.coe.oom.datasource.annotation;

import com.coe.oom.datasource.DatabaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*** 
 * @Description: 绑定数据源 
 * @Param:  
 * @return:  
 * @Author: 邓太阳 
 * @Date: 2019-05-13 18:14
 */ 
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BindDatasource {
	DatabaseType value();
}