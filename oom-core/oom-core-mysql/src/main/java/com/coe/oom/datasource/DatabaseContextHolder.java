package com.coe.oom.datasource;
/*** 
 * @Description: 用于设置数据源(选择读还是写) 
 * @Param:  
 * @return:  
 * @Author: 邓太阳 
 * @Date: 2019-05-13 18:13
 */ 
public class DatabaseContextHolder {

	private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<>();

	public static void setDatabaseType(DatabaseType type) {
		contextHolder.set(type);
	}

	public static DatabaseType getDatabaseType() {
		return contextHolder.get();
	}
}
