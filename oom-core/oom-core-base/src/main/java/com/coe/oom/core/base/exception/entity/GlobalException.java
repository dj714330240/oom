package com.coe.oom.core.base.exception.entity;


import com.coe.oom.core.base.exception.enums.GirlExceptionLevelEnum;
import com.coe.oom.core.base.util.EeceptionUtil;

/**
 * 系统中所有异常均使用此方法
 * @author lqg
 *
 */
public class GlobalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//异常级别
	private GirlExceptionLevelEnum girlExceptionLevel;

	private String message;
	
	private String messageId;
	
	public GirlExceptionLevelEnum getGirlExceptionLevel() {
		return girlExceptionLevel;
	}

	public void setGirlExceptionLevel(GirlExceptionLevelEnum girlExceptionLevel) {
		this.girlExceptionLevel = girlExceptionLevel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public GlobalException(GirlExceptionLevelEnum girlExceptionLevel, String message) {
		super();
		this.girlExceptionLevel = girlExceptionLevel;
		this.message = message;
	}
	
	public GlobalException(GirlExceptionLevelEnum girlExceptionLevel,Exception exception) {
		super();
		this.girlExceptionLevel = girlExceptionLevel;
		this.message=getMessage(exception);
	}
	
	public GlobalException(String message,Exception exception) {
		super();
		this.message=message;
		this.message=getMessage(exception);
	}
	
	public GlobalException(String msg,GirlExceptionLevelEnum girlExceptionLevel,Exception exception) {
		super();
		this.girlExceptionLevel = girlExceptionLevel;
		this.message=msg+":"+getMessage(exception);
	}
	
	public GlobalException(String msg,GirlExceptionLevelEnum girlExceptionLevel) {
		super();
		this.girlExceptionLevel = girlExceptionLevel;
		this.message=msg;
	}
	
	public GlobalException(String msg) {
		super();
		this.girlExceptionLevel = GirlExceptionLevelEnum.ORDINARY;
		this.message=msg;
	}
	
	public GlobalException() {
		super();
	}
	
	
	private String getMessage(Exception e)  {
		return EeceptionUtil.getMessage(e);
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
}
