package com.coe.oom.core.base.util;


import com.coe.oom.core.base.exception.entity.CheckException;

/**
 * 检查非空util
 * @author lqg
 *
 */
public class CheckExceptionUtil {

	public static void checkNotNull(String fieldName,Object value) throws CheckException{
		if(value==null || value.toString().trim().length()==0){
			throw new CheckException(fieldName+":不允许为空");
		}
	}
	
	public static void checkObjectNotNull(String remark,Object value) throws CheckException{
		if(value==null || value.toString().trim().length()==0){
			throw new CheckException(remark);
		}
	}
}
