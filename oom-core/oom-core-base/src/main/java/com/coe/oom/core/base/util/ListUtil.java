package com.coe.oom.core.base.util;

import java.util.List;

/**
 * 常用辅助util
 * @author lqg
 *
 */
public class ListUtil {

	/**
	 * 获取集合中的第一个元素
	 * @param list
	 * @return
	 */
	public static <T> T listGetFirst(List<T> list) {
		return (list != null && !list.isEmpty()) ? list.get(0) : null;
	}
	
	/**
	 * 是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List list){
		return ! (list != null && !list.isEmpty());
	}
}
