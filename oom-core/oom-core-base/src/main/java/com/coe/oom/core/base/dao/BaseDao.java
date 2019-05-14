package com.coe.oom.core.base.dao;

import org.mybatis.plugin.model.Pager;

public interface BaseDao<T> {

	 int deleteByPrimaryKey(Long id)  throws Exception;

	 int insertSelective(T record)  throws Exception;
	 
	 T selectByPrimaryKey(Long id)  throws Exception;

	 int updateByPrimaryKeySelective(T record) throws Exception;
	 
	 Pager<T> list(int page, int limit) throws Exception;
}
