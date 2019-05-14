package com.coe.oom.core.base.util;

import com.coe.oom.core.base.constant.Constant;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author yechao
 * @date 2013年12月12日
 */
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            “ ”、null 都返回true
	 * @return
	 */
	public static boolean isNull(String str) {
		return (null == str || "".equals(str.trim()));
	}
	
	/**
	 * 判断字符串是否都为空
	 * @param strList
	 * @return
	 */
	public static boolean isAllNull(String ... strList) {
		for (String str : strList) {
			if(isNotNull(str)){
				return false;
			}
		}
		return true;
		
	}

	/**
	 * 是否包含中文
	 *
	 * @param str
	 * @return
	 */
	public static boolean isContainsChinese(String str) {
		if (isNull(str)) {
			return false;
		}
		if (str.length() == str.replaceFirst(Constant.CHINESE_REGEX, "").length()) {
			return false;
		}
		return true;
	}
	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            非空返回true
	 * @return
	 */
	public static boolean isNotNull(String str) {
		return (null != str && !"".equals(str.trim()));
	}

	/**
	 * 判断2个字符串是否相同 比String 自带的Equal的好处是 ,可以对比 2个都为 null的字符串
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isEqual(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 != null && s2 == null) {
			return false;
		}
		if (s2 != null && s1 == null) {
			return false;
		}
		if (s1.equals(s2)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断2个字符串是否相同 比String 自带的Equal的好处是 ,可以对比 2个都为 null的字符串
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isEqual(String s1, String[] s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 != null && s2 == null) {
			return false;
		}
		if (s2 != null && s1 == null) {
			return false;
		}
		for (String temp : s2) {
			if (s1.equals(temp)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断2个字符串是否相同 比String 自带的Equal的好处是 ,可以对比 2个都为 null的字符串
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isEqualIgnoreCase(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 != null && s2 == null) {
			return false;
		}
		if (s2 != null && s1 == null) {
			return false;
		}
		if (s1.equalsIgnoreCase(s2)) {
			return true;
		}
		return false;
	}

	/**
	 * 去除重复
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> removeDuplicate(List<String> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (StringUtil.isEqual(list.get(j), list.get(i))) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	/**
	 * 去除整数后的000. 非0 不能去掉
	 * 
	 * @param sNum
	 * @return
	 */
	public static String getRightStr(String sNum) {
		DecimalFormat decimalFormat = new DecimalFormat("#.000000");
		String resultStr = decimalFormat.format(new Double(sNum));
		if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) {
			resultStr = resultStr.substring(0, resultStr.indexOf("."));
		}
		return resultStr;
	}
	public static void main(String[] args) {
		/*String str=getRightStr("0");*/
//		System.out.println(str);
//		Integer i=Integer.parseInt(str);
//		System.out.println(str);
		
		System.out.println(trimZero("20.00"));	
		
	}

	 
	/**
	 * php版本md5 16 后base64 ,只是base64不同
	 * 
	 * @param str
	 * @return
	 */
	public static String encoderByMd5_PHP(String str) {
		try {
			String newstr = it.sauronsoftware.base64.Base64.encode(md5_16(str));
			return newstr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成签名
	 * 
	 * @param str
	 * @return
	 */
	public static String encoderByMd5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成签名
	 * 
	 * @param str
	 * @return
	 */
	public static String encoderByMd5Hex(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			//BASE64Encoder base64en = new BASE64Encoder();
			String newstr = byte2HexStr(md5.digest(str.getBytes("utf-8")));
			return newstr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String byte2HexStr(byte buf[]) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) {  
                String hex = Integer.toHexString(buf[i] & 0xFF);  
                if (hex.length() == 1) {  
                        hex = '0' + hex;  
                }  
                sb.append(hex.toUpperCase());  
        }  
        return sb.toString();  
} 
	
	/**
	 * 字符串 转 urlEncode
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String urlEncode(String str, String encoding) {
		try {
			return URLEncoder.encode(str, encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * urlEncode转 字符串
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String urlDeEncode(String str, String encoding) {
		try {
			return URLDecoder.decode(str, encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String base64en(String str) {
		try {
			BASE64Encoder base64en = new BASE64Encoder();
			String newstr = base64en.encode(str.getBytes("utf-8"));
			return newstr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String base64de(String str) {
		try {
			BASE64Decoder base64en = new BASE64Decoder();
			return new String(base64en.decodeBuffer(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * md5加密方法
	 * 
	 * @return String 返回32位md5加密字符串(16位加密取substring(8,24))
	 */
	public final static String md5_32(String plainText) {
		String md5Str = null;
		try {
			StringBuffer buf = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes("utf-8"));
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			// 32位的加密
			md5Str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}

	/**
	 * md5 16位加密方法
	 * 
	 */
	public final static String md5_16(String plainText) {
		String md5Str = null;
		try {
			StringBuffer buf = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			// 32位的加密
			md5Str = buf.toString();
			// 16位的加密
			md5Str = buf.toString().substring(8, 24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}

	/**
	 * 除了字母和数字的字符 都作为分隔符
	 * 
	 * @param strs
	 * @return
	 */
	public static String[] splitW(String strs) {
		if (strs == null) {
			return null;
		}
		strs = strs.replaceAll("[^\\w]+", " ");
		strs = strs.replaceAll("\\s+", " ");
		String noArray[] = strs.split(" ");
		return noArray;
	}

	public static String[] splitByReg(String strs, String reg) {
		if (strs == null) {
			return null;
		}
		strs = strs.replaceAll(reg, " ");
		strs = strs.replaceAll("\\s+", " ");
		String noArray[] = strs.split(" ");
		return noArray;
	}

	/**
	 * 下划线转驼峰
	 * 
	 * @param voName
	 * @return
	 */
	public static String toOntRowName(String voName) {
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		for (int i = 0; i < voName.length(); i++) {
			char cur = voName.charAt(i);
			if (cur == '_') {
				flag = true;

			} else {
				if (flag) {
					sb.append(Character.toUpperCase(cur));
					flag = false;
				} else {
					sb.append(Character.toLowerCase(cur));

				}

			}
		}
		return sb.toString();
	}

	/**
	 * 驼峰转下划线
	 * 
	 * @param voName
	 * @return
	 */
	public static String toInRowName(String voName) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < voName.length(); i++) {
			char cur = voName.charAt(i);
			if (Character.isUpperCase(cur)) {
				sb.append("_");
				sb.append(cur);
			} else {
				sb.append(cur);
			}
		}
		return sb.toString().toLowerCase();
	}

	/**
	 * 数字 1234 转 ABD
	 * 
	 * @param number
	 * @return
	 */
	public static String numberToABC(int number) {
		char c = (char) (number + 64);
		return c + "";
	}

	public static String concatArray(String[] strs) {
		String concats = "";
		for (String str : strs) {
			concats += str + ",";
		}
		if (concats.length() > 0) {
			concats = concats.substring(0, concats.length() - 1);
		}
		return concats;
	}

	public static String concatList(List strs) {
		String concats = "";
		for (Object str : strs) {
			concats += str + ",";
		}
		if (concats.length() > 0) {
			concats = concats.substring(0, concats.length() - 1);
		}
		return concats;
	}

	 

	public static boolean isEquals(String str, String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (StringUtil.isEqual(str, strings[i])) {
				return true;
			}
		}
		return false;
	}

	public static String trim(String str) {
		if (isNotNull(str)) {
			str = str.trim();
		}
		return str;
	}

	public static String concatArrayParam(String param) {
		param = param.replaceAll("[^\\d\\w\\-/,]", "");
		while (param.contains(",,")) {
			param = param.replaceAll(",,", ",");
		}
		while ((param.endsWith(","))) {
			param = param.substring(0, param.length() - 1);
		}
		while ((param.startsWith(","))) {
			param = param.substring(1, param.length());
		}
		param = param.replaceAll(",", "','");
		param = "'" + param + "'";
		return param;
	}

	public static String getEncoding(String str) {
		String encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是UTF-8
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是GB2312
				String s = encode;
				return s; // 是的话，返回“GB2312“，以下代码同理
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是ISO-8859-1
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是GBK
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return ""; // 如果都不是，说明输入的内容不属于常见的编码格式。
	}

	public static String getDigital(String str) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	private static String getHexString(byte b) {
		String hexStr = Integer.toHexString(b);
		int m = hexStr.length();
		if (m < 2) {
			hexStr = "0" + hexStr;
		} else {
			hexStr = hexStr.substring(m - 2);
		}
		return hexStr;
	}

	private static String getBinaryString(int i) {
		String binaryStr = Integer.toBinaryString(i);
		int length = binaryStr.length();
		for (int l = 0; l < 8 - length; l++) {
			binaryStr = "0" + binaryStr;
		}
		return binaryStr;
	}

	public static String gbToUtf8(String str) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String s = str.substring(i, i + 1);
			if (s.charAt(0) > 0x80) {
				byte[] bytes = s.getBytes("Unicode");
				String binaryStr = "";
				for (int j = 2; j < bytes.length; j += 2) {
					// the first byte
					String hexStr = getHexString(bytes[j + 1]);
					String binStr = getBinaryString(Integer.valueOf(hexStr, 16));
					binaryStr += binStr;
					// the second byte
					hexStr = getHexString(bytes[j]);
					binStr = getBinaryString(Integer.valueOf(hexStr, 16));
					binaryStr += binStr;
				}
				// convert unicode to utf-8
				String s1 = "1110" + binaryStr.substring(0, 4);
				String s2 = "10" + binaryStr.substring(4, 10);
				String s3 = "10" + binaryStr.substring(10, 16);
				byte[] bs = new byte[3];
				bs[0] = Integer.valueOf(s1, 2).byteValue();
				bs[1] = Integer.valueOf(s2, 2).byteValue();
				bs[2] = Integer.valueOf(s3, 2).byteValue();
				String ss = new String(bs, "UTF-8");
				sb.append(ss);
			} else {
				sb.append(s);
			}
		}
		return sb.toString();
	}
	/**
	 * 
	 * @param str
	 * @param cls
	 * @param regex
	 * @return
	 */
	public static <T> List<T>  stringToList(String str,Class<T> cls,String regex) {
		if(isNull(str)){
			return null;
		}
		List<T> backList=new ArrayList<T>();
		String[] split = str.split(regex);
		for (String single : split) {
			if(isNotNull(single)){
				try {
					Constructor<T> constructor = cls.getConstructor(String.class);
					backList.add(constructor.newInstance(single));
				} catch (Exception e) {
				}
			 }
		}
		return backList;
	}
	 
	public static <T> List<T> splitByReg(String str,Class<T> cls,String regex) {
		 return stringToList(str, cls, regex);
	}
	
	public static String parseForTuofeng(String str){
		char[] charArray = str.toCharArray();
		String newStr="";
		for (int i = 0; i < charArray.length; i++) {
			if(charArray[i]=='_'){
	            if(i+1< charArray.length){
	            	newStr+=String.valueOf(charArray[++i]).toUpperCase();
	            }
			}else{
				newStr+=String.valueOf(charArray[i]).toLowerCase();
			}
		}
		return newStr;
	}
	
	public static String join(String [] strList,String join){
		String string = Arrays.toString(strList);
		return (string.replace(", ", join).replace("[", "").replace("]", ""));
	}
	
	public static String getZero(int length) {
		String zeroStr="";
		for (int i = 0; i < length; i++) {
			zeroStr+="0";
		}
		return zeroStr;
	}
	
	/**
	 * 如果不为空 返回第一个值
	 * @param sourceStr
	 * @param relaceStr
	 * @return
	 */
	public static String returnNotNull(String sourceStr,String relaceStr){
		if(isNull(sourceStr)){
			return relaceStr;
		} 
		return relaceStr;
	}
	
	/**
	 * 去除非数字字符
	 * @param sourceNumber
	 * @return
	 */
	public static String trimExcludeNumber(String sourceNumber){
		if(isNull(sourceNumber)){
			return null;
		}
		return sourceNumber.replaceAll("[^\\d]+", sourceNumber);
	}
	
	
	public static String trimZero(String numberStr) {
//		if(StringUtil.isNull(numberStr)){
//			return null;
//		}
		if(null == numberStr){
			return null;
		}
		if(!numberStr.contains(".")){
			return	numberStr;
		}
		
		StringBuffer newStr = new StringBuffer();
		StringBuffer reverse = new StringBuffer(numberStr).reverse();
		char[] charArray = reverse.toString().toCharArray();
		boolean xx=true;
		for (char c : charArray) {
			if(xx && String.valueOf(c).equals("0")){
				continue;
			}
			xx = false;
			newStr.append(c);
		}
		String reverse2 = newStr.reverse().toString();
		if(reverse2.toString().endsWith(".")){
			reverse2=reverse2.replace(".", "");
		}
		
		return reverse2;
		
	}
	
	public static String sort(String str){
		if(StringUtil.isNull(str)){
			return null;
		}
		char[] s1 = str.toCharArray();
		for (int i = 0; i < s1.length; i++) {
			for (int j = 0; j < i; j++) {
				if (s1[i] < s1[j]) {
					char temp = s1[i];
					s1[i] = s1[j];
					s1[j] = temp;
				}
			}
		}
		// 再次将字符数组转换为字符串，也可以直接利用String.valueOf(s1)转换
		String st = new String(s1);
		return st;
	}

	
	
}