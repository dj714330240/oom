package com.coe.oom.core.base.util.vali.bean;


import com.coe.oom.core.base.util.StringUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 校验工具类
 * @author lqg
 *
 */
public class ValiUtil {
	
	/**
	 * 校验通过  返回null  不通过 返回相应字符串
	 * @param object  校验对象
	 * @param valueList  针对性校验  可选  匹配注解中的 value值进行校验
	 * @return
	 */
	public static String vali(Object object, String... valueList){

		StringBuffer errorStr=new StringBuffer();
		
		List<String> asList = null;
		if(valueList!=null && valueList.length>0){
			asList=Arrays.asList(valueList);
		}
		return vali(object, asList, errorStr,"");
	}
	
	/**
	public static String outOrderVali(Object object,String valiType, String... valueList){
		StringBuffer errorStr=new StringBuffer();
		
		List<String> asList = null;
		if(valueList!=null && valueList.length>0){
			asList=Arrays.asList(valueList);
		}
		return outOrderVali(object,valiType, asList, errorStr,"");
	}
	**/
	
	private static String vali(Object object, List<String> valueList,StringBuffer errorStr,String prefix) {
		try {
			Class cls = object.getClass();
			Field[] declaredFields = cls.getDeclaredFields();
            //循环字段
			for (Field field : declaredFields) {
				field.setAccessible(true);
				//判断字段上面是否有注解
				Vali annotation = field.getAnnotation(Vali.class);
				if (annotation == null) {
					continue;
				}
				int minLength = annotation.minLength();
				int maxLength = annotation.maxLength();
				//获取注解上面的value值
				String[] valiValueList = annotation.value();
				//如果value值不为空 则判断value值 跟 入参valueList 是否有交集  有交集则进行校验  否则不校验
				if(valiValueList!=null && valiValueList.length>0 && StringUtil.isNotNull(valiValueList[0])){
					if(valueList==null||valueList.size()<1){
						continue;
					}
					boolean isContains=false;
					for (String valiValue : valiValueList) {
						boolean contains = valueList.contains(valiValue);
						if(contains){
							isContains=true;
							break;
						}
					}
					if(! isContains){
						continue;
					}
				}
				//获取正则表达式
				String regular = annotation.regular();
				boolean allowNull=annotation.allowNull();
				//获取字段值
				Object value = field.get(object);
				//如果为null 校验不通过
				if (value == null) {
					//如果不允许为空  则提示
					if(!allowNull){
						errorStr.append(prefix+field.getName() + ":" + annotation.msg() + "  ");	
					}
					continue;
				}
				if(StringUtil.isNull(value.toString()) && allowNull){
					continue;
				}
				//如果是集合 如果集合为空集合  不通过
				if (List.class == field.getType()) {
					List list = (List) value;
					if (list.size() == 0) {
						errorStr.append(prefix+field.getName() + ":" + annotation.msg());
					}
					int listIndex=1;
					
					for (Object object2 : list) {
						//如果集合明细数据为对象  递归校验
						if((!(value instanceof Number)) && (!(value instanceof String))  && (!(value instanceof BigDecimal)))
						    vali(object2, valueList,errorStr,"记录"+(listIndex++)+":");
						
					}
				} else if ((!(value instanceof Number)) && (!(value instanceof String))  && (!(value instanceof BigDecimal))) {
					vali(value, valueList,errorStr,"");
				} else {
					boolean matches = value.toString().matches(regular);
					if(matches && ( minLength !=-1 || maxLength !=-1 )  ){
						//如果最小长度存在  并且最小长度  大于value
						if(minLength !=-1 && minLength > value.toString().length()){
							errorStr.append(prefix+field.getName() + ":长度太低,最低长度为:"+minLength+" ");
							continue;
						}
						
						//如果最大长度存在  并且最大长度  小于value
						if(maxLength !=-1 && maxLength < value.toString().length()){
							errorStr.append(prefix+field.getName() + ":长度太高,最大长度为:"+maxLength+" ");
							continue;
						}
					}
					
					if (matches == false)
						errorStr.append(prefix+field.getName() + ":" + annotation.msg() + "  ");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (errorStr.length() != 0) {
			return errorStr.toString();
		}
		return null;
	}
	/**
	private static String outOrderVali(Object object,String common, List<String> valueList,StringBuffer errorStr,String prefix) {
		try {
			Class cls = object.getClass();
			Field[] declaredFields = cls.getDeclaredFields();
			//循环字段
			for (Field field : declaredFields) {
				//
				Integer minLength = null;
				Integer maxLength = null;
				//
				//获取注解上面的value值
				String[] valiValueList = null;
				
				//获取正则表达式
				String regular = null;
				Boolean allowNull= null;
				//
				String msg = null;
				
				field.setAccessible(true);
				//判断字段上面是否有注解
				if(StringUtil.isEqual(common, COMMON)) {
					CommonVali anno = field.getAnnotation(CommonVali.class);
					if(null==anno) 
						continue;
					Class<? extends CommonVali> class1 = anno.getClass();
					Method minLengthMethod = class1.getDeclaredMethod("minLength", null);
					Method maxLengthMethod = class1.getDeclaredMethod("maxLength", null);
					Method valueMethod = class1.getDeclaredMethod("value", null);
					Method regularMethod = class1.getDeclaredMethod("regular", null);
					Method allowNullMethod = class1.getDeclaredMethod("allowNull", null);
					Method msgMethod = class1.getDeclaredMethod("msg", null);
					minLength = (Integer)minLengthMethod.invoke(anno, null);
					maxLength = (Integer)maxLengthMethod.invoke(anno, null);
					valiValueList = (String[])valueMethod.invoke(anno, null);
					regular = (String)regularMethod.invoke(anno, null);
					allowNull = (Boolean)allowNullMethod.invoke(anno, null);
					msg = (String)msgMethod.invoke(anno, null);
					
				}
				else if(StringUtil.isEqual(common, PE)) {
					PeVali anno = field.getAnnotation(PeVali.class);
					if(null==anno) 
						continue;
					Class<? extends PeVali> class1 = anno.getClass();
					Method minLengthMethod = class1.getDeclaredMethod("minLength", null);
					Method maxLengthMethod = class1.getDeclaredMethod("maxLength", null);
					Method valueMethod = class1.getDeclaredMethod("value", null);
					Method regularMethod = class1.getDeclaredMethod("regular", null);
					Method allowNullMethod = class1.getDeclaredMethod("allowNull", null);
					Method msgMethod = class1.getDeclaredMethod("msg", null);
					minLength = (Integer)minLengthMethod.invoke(anno, null);
					maxLength = (Integer)maxLengthMethod.invoke(anno, null);
					valiValueList = (String[])valueMethod.invoke(anno, null);
					regular = (String)regularMethod.invoke(anno, null);
					allowNull = (Boolean)allowNullMethod.invoke(anno, null);
					msg = (String)msgMethod.invoke(anno, null);
				}
				else if(StringUtil.isEqual(common, BC)) {
					BcVali anno= field.getAnnotation(BcVali.class);	
					if(null==anno) 
						continue;
					Class<? extends BcVali> class1 = anno.getClass();
					Method minLengthMethod = class1.getDeclaredMethod("minLength", null);
					Method maxLengthMethod = class1.getDeclaredMethod("maxLength", null);
					Method valueMethod = class1.getDeclaredMethod("value", null);
					Method regularMethod = class1.getDeclaredMethod("regular", null);
					Method allowNullMethod = class1.getDeclaredMethod("allowNull", null);
					Method msgMethod = class1.getDeclaredMethod("msg", null);
					minLength = (Integer)minLengthMethod.invoke(anno, null);
					maxLength = (Integer)maxLengthMethod.invoke(anno, null);
					valiValueList = (String[])valueMethod.invoke(anno, null);
					regular = (String)regularMethod.invoke(anno, null);
					allowNull = (Boolean)allowNullMethod.invoke(anno, null);
					msg = (String)msgMethod.invoke(anno, null);
				}
				else if(StringUtil.isEqual(common, ETK)) {
					EtkVali anno = field.getAnnotation(EtkVali.class);
					if(null==anno) 
						continue;
					Class<? extends EtkVali> class1 = anno.getClass();
					Method minLengthMethod = class1.getDeclaredMethod("minLength", null);
					Method maxLengthMethod = class1.getDeclaredMethod("maxLength", null);
					Method valueMethod = class1.getDeclaredMethod("value", null);
					Method regularMethod = class1.getDeclaredMethod("regular", null);
					Method allowNullMethod = class1.getDeclaredMethod("allowNull", null);
					Method msgMethod = class1.getDeclaredMethod("msg", null);
					minLength = (Integer)minLengthMethod.invoke(anno, null);
					maxLength = (Integer)maxLengthMethod.invoke(anno, null);
					valiValueList = (String[])valueMethod.invoke(anno, null);
					regular = (String)regularMethod.invoke(anno, null);
					allowNull = (Boolean)allowNullMethod.invoke(anno, null);
					msg = (String)msgMethod.invoke(anno, null);
				}
				//如果value值不为空 则判断value值 跟 入参valueList 是否有交集  有交集则进行校验  否则不校验
				if(valiValueList!=null && valiValueList.length>0 && StringUtil.isNotNull(valiValueList[0])){
					if(valueList==null||valueList.size()<1){
						continue;
					}
					boolean isContains=false;
					for (String valiValue : valiValueList) {
						boolean contains = valueList.contains(valiValue);
						if(contains){
							isContains=true;
							break;
						}
					}
					if(! isContains){
						continue;
					}
				}
				//获取字段值
				Object value = field.get(object);
				//如果为null 校验不通过
				if (value == null) {
					//如果不允许为空  则提示
					if(!allowNull){
						errorStr.append(prefix+field.getName() + ":" + msg + "  ");	
					}
					continue;
				}
				if(StringUtil.isNull(value.toString()) && allowNull){
					continue;
				}
				//如果是集合 如果集合为空集合  不通过
				if (List.class == field.getType()) {
					List list = (List) value;
					if (list.size() == 0) {
						errorStr.append(prefix+field.getName() + ":" + msg);
					}
					int listIndex=1;
					
					for (Object object2 : list) {
						//如果集合明细数据为对象  递归校验
						if((!(value instanceof Number)) && (!(value instanceof String))  && (!(value instanceof BigDecimal)))
							vali(object2, valueList,errorStr,"记录"+(listIndex++)+":");
						
					}
				} else if ((!(value instanceof Number)) && (!(value instanceof String))  && (!(value instanceof BigDecimal))) {
					vali(value, valueList,errorStr,"");
				} else {
					boolean matches = value.toString().matches(regular);
					if(matches && ( minLength !=-1 || maxLength !=-1 )  ){
						//如果最小长度存在  并且最小长度  大于value
						if(minLength !=-1 && minLength > value.toString().length()){
							errorStr.append(prefix+field.getName() + ":长度太低,最低长度为:"+minLength+" ");
							continue;
						}
						
						//如果最大长度存在  并且最大长度  小于value
						if(maxLength !=-1 && maxLength < value.toString().length()){
							errorStr.append(prefix+field.getName() + ":长度太高,最大长度为:"+maxLength+" ");
							continue;
						}
					}
					
					if (matches == false)
						errorStr.append(prefix+field.getName() + ":" + msg + "  ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (errorStr.toString().length() != 0) {
			return errorStr.toString();
		}
		return null;
	}
	
	**/
}
