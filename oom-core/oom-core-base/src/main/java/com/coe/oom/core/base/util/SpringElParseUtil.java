package com.coe.oom.core.base.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 使用spring类库 解析key
 * 
 * @author lqg
 *
 */
public class SpringElParseUtil {

	public static Object parseKey(Method method, Object[] args, String key) {
		Map<String, Object> methodParam = getMethodParam(method, args);
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(methodParam);
		// 使用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		return parser.parseExpression(key).getValue(context);
	}

	public static Map<String, Object> getMethodParam(Method method, Object[] args) {
		Map<String, Object> methodParam = new HashMap<>();
		String[] paraNameArr = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			methodParam.put(paraNameArr[i], args[i]);
		}
		return methodParam;
	}

	public static Object parseKey(JSONObject jsonObject, String key) {
		// 使用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法参数放入SPEL上下文中
		Set<String> keySet = jsonObject.keySet();
		for (String keySingle : keySet) {
			context.setVariable(keySingle, jsonObject.get(keySingle));
		}
		return parser.parseExpression(key).getValue(context);
	}

}
