package com.coe.oom.core.base.service;

import org.mybatis.plugin.model.Pager;

/**
 * 目前只对于mogodb有用  mysql 自动生成  没必要再去继承
 * @author lqg
 *
 * @param <T>
 */
public interface BaseService<T> {

	T add(T record) throws Exception;

	boolean delete(Long id) throws Exception;

	T update(T record) throws Exception;

	T get(Long id) throws Exception;

	Pager<T> list(int page, int limit) throws Exception;

}
