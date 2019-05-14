package com.coe.oom.core.base.util;

public class PhoneUtil {

	
	/**
	 * 过滤手机电话中的非数字字符
	 * @param phoneOrMobile
	 * @return
	 */
	public static String trimMobileAndPhone(String phoneOrMobile){
		if(StringUtil.isNull(phoneOrMobile)){
			return null;
		}
		return phoneOrMobile.replaceAll("[^\\d]+", "");
	}
}
