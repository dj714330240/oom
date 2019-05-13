package com.coe.oom.datasource;
/**
 * 用于设置数据源(选择读还是写)
 * @author lqg
 *
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
