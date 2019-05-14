package com.coe.oom.core.base.util;

/**
 * 枚举uutil
 * @author lqg
 *
 */
public class EnumUtil {
	public static boolean isEquals(Enum enum1,Enum...enums){
		if(enum1==null){
			return false;
		}
		for (Enum enum2 : enums) {
			if(enum1==enum2){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEqual(Enum enum1,Enum enum2){
		if(enum1==null || enum2 ==null){
			return false;
		}
		return enum1==enum2;
	}
	
	
}
