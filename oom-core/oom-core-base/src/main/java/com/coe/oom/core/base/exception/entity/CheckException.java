package com.coe.oom.core.base.exception.entity;


import com.coe.oom.core.base.exception.enums.GirlExceptionLevelEnum;

public class CheckException extends GlobalException {

	public CheckException(Exception exception) {
		super(GirlExceptionLevelEnum.ORDINARY, exception);
	}

	public CheckException(String message) {
		super(GirlExceptionLevelEnum.ORDINARY, message);
	}

	public CheckException(String msg, Exception exception) {
		super(msg, GirlExceptionLevelEnum.ORDINARY, exception);
	}

}
