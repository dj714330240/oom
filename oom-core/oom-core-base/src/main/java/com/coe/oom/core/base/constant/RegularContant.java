package com.coe.oom.core.base.constant;

/**
 * 
 * 常用正则
 *
 */
public class RegularContant {

	//纯数字正则
	public static final String NUMBER_REGEX = "^[0-9]+$";

	//小数正则
	public static final String DECIMAL_REGEX = "^([0-9]{1,}[.]*[0-9]{3})$";
	//小数或整数可等于0 //大于0的整数或小数^([1-9]\d*(\.\d*[1-9])?)|(0\.\d*[1-9])$
	public static final String BIGDECIMAL_REGEX = "^[0-9]+([.]{1}[0-9]+){0,1}$";

	//字母跟数字
	public static final String NUMBER_LETTER_REGEX = "^[a-zA-Z0-9]+$";
	
	//Y/N校验
	public static final String YN="[Y|N]{1}";
	
	//时间格式校验  yyyy-mm-dd
	public static final String YYYYMMDD="^\\d{4}(\\-|\\-|\\.)\\d{1,2}\\1\\d{1,2}$";
	
	//时间格式校验  yyyy-MM-dd hh:mm:ss
	public static final String YYYYMMDDHHMMSS="^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";

	/**手机号码**/
	public static  final String MOBILE_PHONE="^0?(13|14|15|17|18|19)[0-9]{9}$";
	
	/**邮箱**/
	public static  final String EMAIL="^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	
	/**邮编**/
	public static  final String POSTCODE="^\\d{6}$";
}

