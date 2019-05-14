package com.coe.oom.core.base.util;

import java.math.BigDecimal;

public class MathUtil {

	public static double add(String num1, String num2) {
		BigDecimal bd1 = new BigDecimal(num1);// 构造一个大数
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.add(bd2).doubleValue();// 调用add静态方法把bd2加到bd1上，并返回doubleValue()类型数据，也可以是intValue()
	}

	public static double sub(String num1, String num2) {
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.subtract(bd2).doubleValue();// 减法操作
	}

	public static double mul(String num1, String num2) {
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.multiply(bd2).doubleValue();// 乘法操作
	}

	public static double div(String num1, String num2, int scale) {
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();// 除法操作
	}

	// 要四舍五入，我们借助BigDecimal的除法操作，因为任何数除以1都不变，然后可用scale指定精度，用ROUND_HALF_UP指定为四舍五入
	public static double round(double num, int scale) {
		BigDecimal bd1 = new BigDecimal(num);
		BigDecimal bd2 = new BigDecimal(1);
		return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue(); // 参数：scale保留小数位个数，BigDecimal.ROUND_HALF_UP：向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向上舍入。其他还有：HALF_DOWN
																				// 向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向下舍入。HALF_EVEN
																				// 向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向相邻的偶数舍入
	}

	public static void main(String[] args) {
		String num1 = "12345.07891";
		String num2 = "3333.5101";
		System.out.println("加法操作：" + round(add(num1, num2), 1));
		System.out.println("减法操作：" + round(sub(num1, num2), 1));
		System.out.println("乘法操作：" + round(mul(num1, num2), 1));
		System.out.println("除法操作：" + (div(num1, num2, 1)));
		System.out.println("四舍五入除法操作：" + Math.round((div(num1, num2, 1))));
	}

}
