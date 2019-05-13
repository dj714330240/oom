package com.coe.oom.datasource.aop;


import com.coe.oom.datasource.DatabaseContextHolder;
import com.coe.oom.datasource.DatabaseType;
import com.coe.oom.datasource.annotation.BindDatasource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切换数据源切面
 * @author lqg
 *
 */
@Aspect
@Component
public class DataSourceAdvice {
	private Logger logger=LoggerFactory.getLogger(DataSourceAdvice.class);
	@Around(value="@annotation(bindDatasource)")
	public Object doInterceptor(ProceedingJoinPoint pjp, BindDatasource bindDatasource) throws Throwable {
		DatabaseType databaseType = bindDatasource.value();
		try {
			logger.info("切换数据源:"+databaseType.toString());
			//根据注解值设置数据源
			DatabaseContextHolder.setDatabaseType(databaseType);
			return pjp.proceed();
		} finally {
			//设置回默认数据源
			DatabaseContextHolder.setDatabaseType(DatabaseType.write);
		}
	}

}
