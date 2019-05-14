package com.coe.oom.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/*** 
 * @Description: 动态数据源（需要继承AbstractRoutingDataSource） 
 * @Param:  
 * @return:  
 * @Author: 邓太阳 
 * @Date: 2019-05-13 18:14
 */ 
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DatabaseContextHolder.getDatabaseType();
	}

}
