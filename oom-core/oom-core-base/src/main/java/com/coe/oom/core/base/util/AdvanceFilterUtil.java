package com.coe.oom.core.base.util;


import com.coe.oom.core.base.suport.select.AdvanceParam;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class AdvanceFilterUtil {
	
	public enum Option {
//		"op":"contains"  equal notequal greater greaterorequal less lessorequal beginwith endwith
		CONTAINS("contains","Like"), 
		EQUAL("equal","EqualTo"),
		NOTEQUAL("notequal","NotEqualTo"),
		GREATER("greater","GreaterThan"),
		GREATEROREQUAL("greaterorequal","GreaterThanOrEqualTo"), 
		LESS("less","LessThan"),
		LESSOREQUAL("lessorequal","LessThanOrEqualTo"),
		BEGINWITH("beginwith","Beginwith"),
		ENDWITH("endwith","Endwith");
		
		public final String string;
		public final String code;
		
		 Option(String string,String code) {
			this.string = string;
			this.code = code;
		}

		public String getString() {
			return string;
		}

		public String getCode() {
			return code;
		}
		
		 
		private static Map<String,Option> codeMap=new HashMap<String,Option>();
		
		static{
			for(Option c: Option.values()){
				codeMap.put(c.string, c);
			}
		}
		
		public static Option forCode(String string){
			return codeMap.get(string);
		} 
	}
	
	
	public static Map<String,  Map<String, Method>> methodMap = new HashMap<>();
	
//	"join" : "or",
//	 "lb" : "(",
//	 "field" : "customerCode",
//	 "op" : "contains",
//	 "value" : "i",
//	 "rb" : ")"
	

	public static void fillAdvanceClause(Object criteria, AdvanceParam query) {
	
		Class<? extends Object> criteriaClazz = criteria.getClass();
		
		try {
			String methodName = generMethodName(query);
			
			setValue(criteria, criteriaClazz, methodName, query.getValue());
			
			/*
			Method method = getMethod(criteriaClazz,methodName,String.class);
			method.setAccessible(true);
		
			method.invoke(criteria, query.getValue());
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static String generMethodName(AdvanceParam query) {
//		"field":"customerCode"
//		"op":"contains"  
		
		StringBuffer sb = new StringBuffer();
		sb.append("and");
		sb.append(Character.toUpperCase(query.getField().charAt(0)));
		sb.append(query.getField().substring(1,query.getField().length()));
		sb.append(Option.forCode(query.getOp()).getCode());
		
		return sb.toString();
		
}

	public static Method getMethod(Class clazz,String methodName,Class<?>... paramClass){
		try {
			return clazz.getDeclaredMethod(methodName, paramClass);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	
	
	
	public static void setValue(Object object,Class clazz,String methodName,String value){
		try {
			Map<String, Method> map = methodMap.get(clazz.getName());
			
			Method[] declaredMethods = clazz.getDeclaredMethods();
			for (Method method : declaredMethods) {
				if(!StringUtil.isEqual(method.getName(), methodName)){
					continue;
				}
				Class<?>[] parameterTypes = method.getParameterTypes();
				Class<?> classParam=parameterTypes[0];
				
				Constructor<?> constructor = classParam.getConstructor(String.class);
				Object newInstance = constructor.newInstance(value);
				method.invoke(object, newInstance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
