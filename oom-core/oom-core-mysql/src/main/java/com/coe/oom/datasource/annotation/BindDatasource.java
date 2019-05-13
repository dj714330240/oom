package com.coe.oom.datasource.annotation;

import com.coe.oom.datasource.DatabaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定数据源
 * @author lqg
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BindDatasource {
	DatabaseType value();
}