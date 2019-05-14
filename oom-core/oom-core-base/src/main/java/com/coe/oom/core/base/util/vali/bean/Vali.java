package com.coe.oom.core.base.util.vali.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Vali {
	
	String regular() default ".*\\S+.*"; //正则  默认非空
    
	String[] value() default "" ;//此值不为空时  根据传递的值进行校验  如果匹配得上就校验
	
	String msg() default "不能为空"; //提示
	
	int minLength() default -1;//最小长度
	
	int maxLength() default -1;//最大长度
	
	boolean allowNull() default false;//是否允许为空
	
	
}
