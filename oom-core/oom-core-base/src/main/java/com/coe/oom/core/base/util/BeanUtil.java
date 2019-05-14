package com.coe.oom.core.base.util;


import org.apache.commons.beanutils.PropertyUtilsBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.apache.commons.beanutils.BeanUtils;


public class BeanUtil {

	
	static List<String>desensitizationName = new ArrayList<>(Arrays.asList("",""));
	
	static enum DesensitizationName{//字段名称首字母须大写
		CHINESE_NAME(new ArrayList<>(Arrays.asList("Name","ReceiverName","SenderName","OrderPersonName")),String.class),
		ID_CARD(new ArrayList<>(Arrays.asList("IdCardNumber","ReceiverIdCardNumber","OrderPersonCardNo")),String.class),
		FIXED_PHONE(new ArrayList<>(Arrays.asList("PhoneNumbe","PhoneNumber","ReceiverPhoneNumbe","SenderPhoneNumbe","OrderPersonPhoneNumber")),String.class),
		MOBILE_PHONE(new ArrayList<>(Arrays.asList("MobileNumber","ReceiverMobileNumber","SenderMobileNumber","OrderPersonMobileNumber")),String.class),
		ADDRESS(new ArrayList<>(Arrays.asList("Address","ReceiverAddress","SenderAddress","OrderPersonAddress")),String.class),
		EMAIL(new ArrayList<>(Arrays.asList("Email","ReceiverEmail","SenderEmail")),String.class);
		
		
		
		private List<String> codeList;
		
		private Class typeClass;

		private DesensitizationName(List<String> codeList, Class typeClass) {
			this.codeList = codeList;
			this.typeClass = typeClass;
		}
		public List<String> getCodeList() {
			return codeList;
		}

		public void setCodeList(List<String> codeList) {
			this.codeList = codeList;
		}
		public Class getTypeClass() {
			return typeClass;
		}
		public void setTypeClass(Class typeClass) {
			this.typeClass = typeClass;
		}
		
	}

	/**
	 * 脱敏
	 * @param ob
	 */
	public static void desen(Object ob) {
		if(ob==null)
			return;
		Class<? extends Object> clazz = ob.getClass();
		for (DesensitizationName desenName : DesensitizationName.values()) {
			List<String> codeList = desenName.getCodeList();
			for (String name : codeList) {
				String methodGet = "get"+name;
				String methodSet = "set"+name;
				Method get = getMethod(clazz, methodGet);
				Method set = getMethod(clazz, methodSet,desenName.getTypeClass());
				try {
					if(null!=set&&null!=get ) {
						Object obj = get.invoke(ob);
						String value = String.valueOf(get.invoke(ob));
						if(null!=obj&&StringUtil.isNotNull(value)) {
							
								switch (desenName) {
				                 case CHINESE_NAME: {
				                     set.invoke(ob, DesensitizationUtil.chineseName(value));
				                     break;
				                 }
				                 case ID_CARD: {
				                	 set.invoke(ob, DesensitizationUtil.idCardNum(value));
				                     break;
				                 }
				                 case FIXED_PHONE: {
				                	 set.invoke(ob, DesensitizationUtil.fixedPhone(value));
				                     break;
				                 }
				                 case MOBILE_PHONE: {
				                	 set.invoke(ob, DesensitizationUtil.mobilePhone(value));
				                     break;
				                 }
				                 case ADDRESS: {
				                	 set.invoke(ob, DesensitizationUtil.address(value));
				                     break;
				                 }
				                 case EMAIL: {
				                	 set.invoke(ob, DesensitizationUtil.email(value));
				                     break;
				                 }
							 }
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {
//		OutOrderReceiver receiver = new OutOrderReceiver();
//		receiver.setName("hml");
//		receiver.setPhoneNumbe("3725955");
//		receiver.setEmail("hmlluckso@163.com");
//		receiver.setMobileNumber("13563004733");
//		receiver.setIdCardNumber("371526199101080449");
//		System.out.println("脱敏前-----"+receiver.toString());
//		desen(receiver);
//		System.out.println("脱敏后----"+receiver.toString());
	}
	/**
	 * 封装查询的客户跟仓库
	 */
	public static void fillSelectCustomerAndWarehouse(Object cri,Object userDataPermissionsDto){
		try {
			fillSelectCustomerAndWarehouse(cri,userDataPermissionsDto,"WarehouseId","CustomerId");
		} catch (Exception e) {
//			e.printStackTrace();
		}  
		
	}
	
	public static void fillSelectCustomerAndWarehouse(Object cri,Object userDataPermissionsDto,String warehouseIdFiled,String customerIdFiled ){
		try {
			Class<? extends Object> criClazz = cri.getClass();
			Class<? extends Object> userDataPermissionsDtoClazz = userDataPermissionsDto.getClass();
			//是否最高管理员
			Method method = getMethod(userDataPermissionsDtoClazz, "getIsAdmin");
			Boolean isAdmin = (Boolean) method.invoke(userDataPermissionsDto);
			if(isAdmin !=null && isAdmin){
				return;
			}
			//仓库集合
			Method getWarehouseIdList = getMethod(userDataPermissionsDtoClazz, "getWarehouseIdList");
			List<Long> warehouseIdList = (List<Long>) getWarehouseIdList.invoke(userDataPermissionsDto);
			//如果仓库代码为空  
			if(warehouseIdList ==null || warehouseIdList.isEmpty()){
				Method andWarehouseIdEqualTo = getMethod(criClazz, "and"+warehouseIdFiled+"EqualTo", Long.class);
				if(andWarehouseIdEqualTo!=null)
				    andWarehouseIdEqualTo.invoke(cri, -1l);
				return;
			}else{
				Method andWarehouseIdIn = getMethod(criClazz, "and"+warehouseIdFiled+"In", List.class);
				if(andWarehouseIdIn !=null) 
				    andWarehouseIdIn.invoke(cri, warehouseIdList);
			}
			if(customerIdFiled !=null ){
				//客户集合
				Method getCustomerIdList = getMethod(userDataPermissionsDtoClazz, "getCustomerIdList");
				List<Long> customerIdList = (List<Long>) getCustomerIdList.invoke(userDataPermissionsDto);
				if(customerIdList!=null && !customerIdList.isEmpty()){
					Method andCustomerIdIn = getMethod(criClazz, "and"+customerIdFiled+"In", List.class);
					if(andCustomerIdIn !=null)
					    andCustomerIdIn.invoke(cri, customerIdList);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
//			throw e;
		}  
		
	}
	
	/**
	 * 填充创建信息
	 * 
	 * @param createId
	 * @param createName
	 */
	public static void fillCreate(Long createId, String createName, Object object) {
		Class<? extends Object> classObj = object.getClass();
		
		try {
			//创建人id
			Method createIdMethod = getMethod(classObj,"setUserIdCreate", Long.class);
			if(createIdMethod != null)
			    createIdMethod.invoke(object, createId);
			//创建时间
			Method createDateMethod = getMethod(classObj,"setCreateTime", Date.class);
			if(createDateMethod != null) 
			    createDateMethod.invoke(object, new Date());
			//创建名称
			Method createNameMethod = getMethod(classObj,"setUserNameCreate", String.class);
		    if(createNameMethod!=null)
			    createNameMethod.invoke(object, createName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	}

	/**
	 * 填充更新信息
	 * 
	 * @param updateId
	 * @param updateName
	 */
	public static void fillUpdate(Long updateId, String updateName, Object object) {
		Class<? extends Object> classObj = object.getClass();
		
		try {
			//更新者id
			Method updateIdMethod = getMethod(classObj,"setUserIdUpdate", Long.class);
			if(updateIdMethod != null)
			    updateIdMethod.invoke(object, updateId);
			//更新者名称
			Method updateNameMethod = getMethod(classObj,"setUserNameUpdate", String.class);
			if(updateNameMethod != null)
			    updateNameMethod.invoke(object, updateName);
			//更新时间
			Method updateDateMethod = getMethod(classObj,"setUpdateTime", Date.class);
			if(updateDateMethod != null)
			    updateDateMethod.invoke(object, new Date());
		} catch (Exception e) {
		}
		 
	}
	
	public static Method getMethod(Class clazz,String methodName,Class<?>... paramClass){
		try {
			return clazz.getMethod(methodName, paramClass);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	
	/**
	 * 填充客户信息
	 * 
	 * @param updateId
	 * @param updateName
	 */
	public static void fillCustomer(Long customerId,String customerCode,String customerName, Object object) {
		Class<? extends Object> classObj = object.getClass();
		try {
			//客户id
			Method customerIdMethod = getMethod(classObj,"setCustomerId", Long.class);
			if(customerIdMethod != null)
				customerIdMethod.invoke(object, customerId);
			//客户代码
			Method customerCodeMethod = getMethod(classObj,"setCustomerCode", String.class);
			if(customerCodeMethod != null)
				customerCodeMethod.invoke(object, customerCode);
			//客户名称
			Method customerNameMethod = getMethod(classObj,"setCustomerName", String.class);
			if(customerNameMethod != null)
				customerNameMethod.invoke(object, customerName);
		} catch (Exception e) {
		}
		 
	}
	
	/**
	 * 填充仓库信息
	 * 
	 * @param updateId
	 * @param updateName
	 */
	public static void fillWarehouse(Long warehouseId,String warehouseCode,String warehouseName, Object object) {
		Class<? extends Object> classObj = object.getClass();
		try {
			//仓库id
			Method warehouseIdMethod = getMethod(classObj,"setWarehouseId", Long.class);
			if(warehouseIdMethod != null)
				warehouseIdMethod.invoke(object, warehouseId);
			//仓库代码
			Method warehouseCodeMethod = getMethod(classObj,"setWarehouseCode", String.class);
			if(warehouseCodeMethod != null)
				warehouseCodeMethod.invoke(object, warehouseCode);
			//客户名称
			Method warehouseNameMethod = getMethod(classObj,"setWarehouseName", String.class);
			if(warehouseNameMethod != null)
				warehouseNameMethod.invoke(object, warehouseName);
		} catch (Exception e) {
		}
		 
	}
	
	
	/**
	 * 填充货主信息
	 * 
	 * @param updateId
	 * @param updateName
	 */
	public static void fillOwer(Long owerId,String owerCode,String owerName, Object object) {
		Class<? extends Object> classObj = object.getClass();
		try {
			//货主id
			Method owerIdMethod = getMethod(classObj,"setOwerId", Long.class);
			if(owerIdMethod != null)
				owerIdMethod.invoke(object, owerId);
			//货主代码
			Method owerCodeMethod = getMethod(classObj,"setOwerCode", String.class);
			if(owerCodeMethod != null)
				owerCodeMethod.invoke(object, owerCode);
			//客户名称
			Method owerNameMethod = getMethod(classObj,"setOwerName", String.class);
			if(owerNameMethod != null)
				owerNameMethod.invoke(object, owerName);
		} catch (Exception e) {
		}
		 
	}
	
	/**
	 * 填充商家信息
	 * 
	 * @param updateId
	 * @param updateName
	 */
	public static void fillBusiness(Long businessId,String businessCode,String businessName, Object object) {
		Class<? extends Object> classObj = object.getClass();
		try {
			//商家id
			Method businessIdMethod = getMethod(classObj,"setBusinessId", Long.class);
			if(businessIdMethod != null)
				businessIdMethod.invoke(object, businessId);
			//商家代码
			Method businessCodeMethod = getMethod(classObj,"setBusinessCode", String.class);
			if(businessCodeMethod != null)
				businessCodeMethod.invoke(object, businessCode);
			//商家名称
			Method businessNameMethod = getMethod(classObj,"setBusinessName", String.class);
			if(businessNameMethod != null)
				businessNameMethod.invoke(object, businessName);
		} catch (Exception e) {
		}
		 
	}

	/**
	 * 获取具有getter方法的field map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> dataMap = new HashMap<>();
		if (null == obj)
			return dataMap;
		Set<String> fieldNameSet = new HashSet<>();
		List<Field> fields = new ArrayList<>();
		fields.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
		fields.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));
		try {
			fields.addAll(Arrays.asList(obj.getClass().getSuperclass().getSuperclass().getDeclaredFields()));
		} catch (Exception e) {
		}
		
		for (Field f : fields) {
			fieldNameSet.add(f.getName());
		}

		List<Method> methods = new ArrayList<>();
		methods.addAll(Arrays.asList(obj.getClass().getMethods()));
		try {
			methods.addAll(Arrays.asList(obj.getClass().getSuperclass().getMethods()));
			methods.addAll(Arrays.asList(obj.getClass().getSuperclass().getSuperclass().getMethods()));
		} catch (Exception e) {
		}
		for (Method m : methods) {
			if (m.getName().startsWith("get") && m.getTypeParameters().length == 0) {
				String fieldName = m.getName().replaceFirst("get", "");
				fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
				if (fieldNameSet.contains(fieldName)) {
					Object value = null;
					try {
						value = m.invoke(obj);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
					if (null != value)
						if (value instanceof String && ((String) value).trim().length() == 0)
							continue;
						else
							dataMap.put(fieldName, value);
				}
			}

		}
		return dataMap;

	}

	/**
	 * 把source中属性付给target
	 * 
	 * @param source
	 * @param target
	 * @throws Exception
	 * @throws Exception
	 */
	public static void copy(Object source, Object target) {
		Class<? extends Object> class1 = source.getClass();
		takeClazzCopy(class1, source, target);
	}

	private static void takeClazzCopy(Class<? extends Object> cla_ss, Object source, Object target) {
		Field[] declaredFields2 = cla_ss.getDeclaredFields();
		for (Field field : declaredFields2) {
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(source);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			if (value != null) {
				try {
					BeanUtils.setProperty(target, field.getName(), value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (cla_ss.getSuperclass() != Object.class) {
			takeClazzCopy(cla_ss.getSuperclass(), source, target);
		}

	}
	
	public static Object getProperty(Object bean,String name){
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			return propertyUtilsBean.getNestedProperty(bean, name);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return null;
		}
	}

	
	
	/**
	 * 填充状态
	 * @param code
	 * @param name
	 * @param guide
	 * @param object
	 */
	public static void fillStatus(String code, String name,String guide, Object object) {

		Class<? extends Object> classObj = object.getClass();
		try {
			//状态代码
			Method statusCodeMethod = classObj.getMethod("setStatusCode", String.class);
			if(statusCodeMethod != null)
				statusCodeMethod.invoke(object, code);
			//状态名称
			Method statusNameMethod = classObj.getMethod("setStatusName", String.class);
			if(statusNameMethod != null)
				statusNameMethod.invoke(object, name);
			//状态下一步
			Method statusGuideMethod = classObj.getMethod("setStatusGuide", String.class);
			if(statusGuideMethod != null)
				statusGuideMethod.invoke(object, guide);
		} catch (Exception e) {
		}
		
	}
	
	public static <T> T returnNotNull(T source,T replace){
		// returnNotNull
		if(source!=null && StringUtil.isNotNull(source.toString())){
			return source;
		}
		return replace;
	}
	
	

}
