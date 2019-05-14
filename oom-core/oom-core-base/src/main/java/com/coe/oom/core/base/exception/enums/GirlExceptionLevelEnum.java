package com.coe.oom.core.base.exception.enums;

/**
 * 异常级别
 * @author lqg
 *
 */
public enum GirlExceptionLevelEnum {
	
	ORDINARY(1,"普通"),//有些异常用于检查数据的合法性  此类异常属于普通异常  此类异常不记录   开发者自行决定
	SERIOUS(2,"严重"),//此类异常需要记录
	SERIOUSER(3,"最严重");//此类异常需要发送邮件给相关人员 同时记录
	
	private Integer code;
	
	private String name;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private GirlExceptionLevelEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}
	
	
}
