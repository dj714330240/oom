package com.coe.oom.core.base.util;


public class DesensitizationUtil {
	
		private static String rightPad(String left,int num,String str) {
			int i = num-left.length();
			for(int l = 0;l<i;l++) {
				left += str;
			}
			return left;
		}
		
		private static String leftPad(String right,int num,String str) {
			int i = num-right.length();
			for(int l = 0;l<i;l++) {
				right = str +right;
			}
			return right;
		}
		
		private static String centerPad(String left,String right,int num,String str) {
			int i = num-left.length()-right.length();
			for(int l = 0;l<i;l++) {
				left += str ;
			}
			return left+right;
		}
		
	
	 
	    /**
	     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
	     * 
	     * @param name
	     * @return
	     */
	    public static String chineseName(String fullName) {
	        if (StringUtil.isNull(fullName)) {
	            return "";
	        }
	        String sub = fullName.substring(0, 1);
	        return rightPad(sub, fullName.length(), "*");
	    }
	 
	    /**
	     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
	     * 
	     * @param familyName
	     * @param givenName
	     * @return
	     */
	    public static String chineseName(String familyName, String givenName) {
	        if (StringUtil.isNull(familyName) || StringUtil.isNull(givenName)) {
	            return "";
	        }
	        return chineseName(familyName + givenName);
	    }
	 
	    /**
	     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
	     * 
	     * @param id
	     * @return
	     */
	    public static String idCardNum(String id) {
	        if (StringUtil.isNull(id)) {
	            return "";
	        }
	        String left = id.substring(0, Math.min(3,id.length()));
	        String right = id.substring(Math.max(id.length()-3,0), id.length());
	        return centerPad(left, right, id.length(), "*");
	    }
	 
	    /**
	     * [固定电话] 后四位，其他隐藏<例子：****1234>
	     * 
	     * @param num
	     * @return
	     */
	    public static String fixedPhone(String num) {
	        if (StringUtil.isNull(num)) {
	            return "";
	        }
	        String right = num.substring(Math.max(num.length()-4,0), num.length());
	        return rightPad(right, num.length(), "*");
	    }
	 
	    /**
	     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
	     * 
	     * @param num
	     * @return
	     */
	    public static String mobilePhone(String num) {
	        if (StringUtil.isNull(num)) {
	            return "";
	        }
	        String left = num.substring(0, Math.min(3,num.length()));
	        String right = num.substring(Math.max(num.length()-4,0), num.length());
	        return centerPad(left, right, num.length(), "*");
	        
	    }
	 
	    /**
	     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市****>
	     * 
	     * @param address
	     * @param sensitiveSize
	     *            敏感信息长度
	     * @return
	     */
	    public static String address(String address) {
	        if (StringUtil.isNull(address)) {
	            return "";
	        }
	        String left = address.substring(0,Math.min(2,address.length()));
	        return rightPad(left,address.length(),"*");
	    }
	 
	    /**
	     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
	     * 
	     * @param email
	     * @return
	     */
	    public static String email(String email) {
	        if (StringUtil.isNull(email)) {
	            return "";
	        }
	        int index = email.indexOf("@");
	        if (index <= 1)
	            return email;
	        else {
	        	String left = email.substring(0, Math.min(1,email.length()));
	        	String right = email.substring(index,email.length());
	        	return centerPad(left, right, email.length(), "*");
	        }
	    }
	 
	    /**
	     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
	     * 
	     * @param cardNum
	     * @return
	     */
	    public static String bankCard(String cardNum) {
	        if (StringUtil.isNull(cardNum)) {
	            return "";
	        }
	        String left = cardNum.substring(0, Math.min(6,cardNum.length()));
        	String right = cardNum.substring(Math.max(0,cardNum.length()-4),cardNum.length());
        	return centerPad(left, right, cardNum.length(), "*");
	    }
	 
	   
}
