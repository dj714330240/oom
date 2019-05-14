package com.coe.oom.core.base.util;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具类
 * 
 * @author yechao
 * @date 2013年12月12日
 */
public class DateUtil {
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public final static String yyyyMMddHHmmss = "yyyy/MM/dd HH:mm:ss";

	/**	yyyy/MM/dd	*/
	public final static String yyyyMMdd = "yyyy/MM/dd";
	/** yyyymmdd  20150518*/
	public final static String yyyymmdd = "yyyyMMdd";
	public final static String yyyymm = "yyyyMM";

	/**	yyyy-MM-dd	*/
	public final static String yyyy_MM_dd = "yyyy-MM-dd";
	private final static String yyyy_MM_ddHHmmssS = "yyyy-MM-dd HH:mm:ss.S";
	private final static String yyyy_MM_ddHHmmssSS = "yyyy-MM-dd HH:mm:ss.SS";
	private final static String yyyy_MM_ddHHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";

	public final static String MMddYYYY = "MM/dd/yyyy";

	/**
	 * 常用日期格式 中国常用格式	yyyy-MM-dd HH:mm:ss
	 */
	public final static String yyyy_MM_ddHHmmss = "yyyy-MM-dd HH:mm:ss";

	/**	MMM dd,yyyy hh:mm aa	*/
	public final static String MMMdd_comma_yyyyhhmmaa = "MMM dd,yyyy hh:mm aa";

	/**	MMM dd,yyyy HH:mm aa	*/
	public final static String MMMdd_comma_yyyyHHmmaa = "MMM dd,yyyy HH:mm aa";

	/**	MMM dd,yyyy, hh:mm aa	*/
	public final static String MMMdd_comma_yyyy_comma_hhmmaa = "MMM dd,yyyy, hh:mm aa";

	/**	MMM dd,yyyy, HH:mm aa	*/
	public final static String MMMdd_comma_yyyy_comma_HHmmaa = "MMM dd,yyyy, HH:mm aa";

	/**	YYYY-MM-DD HH24:MI:SS	*/
	public final static String YYYYMMDD_HH24MISS = "YYYY-MM-DD HH24:MI:SS";

	/**
	 * 9999-12-31
	 */
	public final static Date MAX_DATE = parse("9999-12-31");
	/**
	 * 9999-12-31 23:59:59
	 */
	public final static Date MAX_DATE_TIME = parse("9999-12-31 23:59:59");
	/**
	 * 字符串的日期类型 转换称 Date 类型
	 * 
	 * @param timeContent
	 *            字符串的日期类型
	 * @param formatStyle
	 *            日期格式
	 * @return
	 */
	public static Date stringConvertDate(String timeContent, String formatStyle) {
		SimpleDateFormat format = new SimpleDateFormat(formatStyle);
		try {
			return format.parse(timeContent);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	public static boolean checkDate(String time, String format) {
		if (stringConvertDate(time, format) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Date 转化成 String 类型的字符串日期格式
	 * 
	 * @param date
	 * @param formatStyle
	 *            转化成 什么格式
	 * @return
	 */
	public static String dateConvertString(Date date, String formatStyle) {
		if(null==date)
			return "";
		SimpleDateFormat format = new SimpleDateFormat(formatStyle);
		return format.format(date);
	}

	/**
	 * 字符串日期格式 转换成 带 地区标识的 Date 类型
	 * 
	 * @param strDate
	 * @param locale
	 * @param formatStyle
	 * @return
	 */
	public static Date stringConvertLocalDate(String strDate, Locale locale, String formatStyle) {
		SimpleDateFormat srcsdf = new SimpleDateFormat(formatStyle, locale);
		try {
			return srcsdf.parse(strDate);
		} catch (ParseException e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * Calendar 类型 转 XMLGregorianCalendar 类型
	 * 
	 * @param calendar
	 * @return
	 */
	public static XMLGregorianCalendar calendarTurnToXMLGregorianCalendar(Calendar calendar) {
		GregorianCalendar greCalendar = new GregorianCalendar();
		greCalendar.setTime(calendar.getTime());
		XMLGregorianCalendar re = new XMLGregorianCalendarImpl(greCalendar);
		return re;
	}

	/**
	 * 获取本地时间相对的UTC | GMT时间
	 * 
	 * @return
	 */
	public static Date getUtcTime() {
		// 1、取得本地时间：
		Calendar calendar = Calendar.getInstance();
		// 2、取得时间偏移量：
		final int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		final int dstOffset = calendar.get(Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return calendar.getTime();
	}

	/**
	 * 获取2个时间相差多少秒
	 *
	 * @param date1
	 * @param date2
	 * @return date1-date2
	 */
	public static Long getDiffSeconds(Date date1, Date date2) {
		long milliseconds1 = date1.getTime();
		long milliseconds2 = date2.getTime();
		long diff = milliseconds1 - milliseconds2;
		if (diff < 0) {
			diff = -diff;
		}
		long diffSeconds = diff / (1000);
		return diffSeconds;
	}

	/**
	 * 获取2个时间相差多少分钟
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long getDiffMinutes(Date date1, Date date2) {
		Long diffMinutes = getDiffSeconds(date1, date2) / 60;
		return diffMinutes;
	}

	/**
	 * 获取2个时间直接 相差多少小时
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long getDiffHours(Date date1, Date date2) {
		Long diffHours = getDiffMinutes(date1, date2) / 60;
		return diffHours;
	}

	/**
	 * 获取2个时间直接 相差多少天
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long getDiffDays(Date date1, Date date2) {
		Long diffDays = getDiffHours(date1, date2) / 24;
		return diffDays;
	}

	public static String getDaysAgoStart(int ago) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -ago);
		String time = DateUtil.dateConvertString(calendar.getTime(), DateUtil.yyyy_MM_dd);
		String timeFrom = time + " 00:00:00";
		return timeFrom;
	}

	public static String getSevenDaysAgoStart() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		String time = DateUtil.dateConvertString(calendar.getTime(), DateUtil.yyyy_MM_dd);
		String timeFrom = time + " 00:00:00";
		return timeFrom;
	}

	/**
	 * 指定日期的某天前的开始时间
	 *
	 * @param date
	 * @param ago
	 * @return
	 */
	public static Date getDaysAgoStart(Date date, int ago) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -ago);
		String time = DateUtil.dateConvertString(calendar.getTime(), DateUtil.yyyy_MM_dd);
		String timeFrom = time + " 00:00:00";
		return DateUtil.stringConvertDate(timeFrom, DateUtil.yyyy_MM_ddHHmmss);
	}

	public static String getTodayStart() {
		String time = DateUtil.dateConvertString(new Date(), DateUtil.yyyy_MM_dd);
		String timeFrom = time + " 00:00:00";
		return timeFrom;
	}

	public static String getTodayEnd() {
		String time = DateUtil.dateConvertString(new Date(), DateUtil.yyyy_MM_dd);
		String timeTo = time + " 23:59:59";
		return timeTo;
	}

	public static String getDaysAgoEnd(int ago) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -ago);
		String time = DateUtil.dateConvertString(calendar.getTime(), DateUtil.yyyy_MM_dd);
		String timeFrom = time + " 23:59:59";
		return timeFrom;
	}

	/**
	 * 指定日期的某天前的结束时间
	 *
	 * @param date
	 * @param ago
	 * @return
	 */
	public static Date getDaysAgoEnd(Date date, int ago) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -ago);
		String time = DateUtil.dateConvertString(calendar.getTime(), DateUtil.yyyy_MM_dd);
		String timeFrom = time + " 23:59:59";
		return DateUtil.stringConvertDate(timeFrom, DateUtil.yyyy_MM_ddHHmmss);
	}

	/**
	 * 获得指定日期的下一天
	 *
	 * @param day
	 * @return
	 */
	public static String getNextDayTime(String day) {
		Date date = DateUtil.stringConvertDate(day, DateUtil.yyyy_MM_ddHHmmss);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);

		return new SimpleDateFormat(DateUtil.yyyy_MM_ddHHmmss).format(cal.getTime());
	}

	/**
	 * 获得指定日期的前一天
	 *
	 * @param day
	 * @return
	 */
	public static String getPreDayTime(String day) {
		Date date = DateUtil.stringConvertDate(day, DateUtil.yyyy_MM_ddHHmmss);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);

		return new SimpleDateFormat(DateUtil.yyyy_MM_ddHHmmss).format(cal.getTime());
	}

	public static Date getNextPrevDay(Date date, int dayNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, dayNum);
		return calendar.getTime();
	}

	public static Date getNextPrevDay(int nowDay, int dayNum) {
		String day = String.valueOf(nowDay);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(day.substring(0, 2)), Integer.parseInt(day.substring(2, 4)), Integer.parseInt(day.substring(4, 6)));
		calendar.add(Calendar.DAY_OF_MONTH, dayNum);
		return calendar.getTime();
	}

	public static int getDay(Date data) {
		java.text.DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String eTime = dateFormat.format(data);
		return Integer.parseInt(eTime);
	}

	public static long getTime(String time) {
		Date a = DateUtil.stringConvertDate("2015-09-15 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date b = DateUtil.stringConvertDate("2015-09-15 " + time, "yyyy-MM-dd HH:mm:ss");
		return b.getTime() - a.getTime();
	}

	/**
	 * 判断time1 是否早于time2
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean before(String time1, String time2) {
		Date date1 = DateUtil.stringConvertDate(time1, DateUtil.yyyy_MM_ddHHmmss);
		Date date2 = DateUtil.stringConvertDate(time2, DateUtil.yyyy_MM_ddHHmmss);

		return date1.before(date2);
	}

	/**
	 * 获得当前系统时间
	 * 
	 * @return
	 */
	public static String getSystemDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.yyyy_MM_ddHHmmss);
		return sdf.format(new Date());
	}

	/**
	 * 获得当前系统时间
	 * 
	 * @return
	 */
	public static String getSystemDate2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 得到某个时间段加上多少小时的时间
	 * 
	 * @param dateTime
	 * @param hours
	 * @return
	 */
	public static String getDateTimeAddHours(Date dateTime, Integer hours) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.yyyy_MM_ddHHmmss);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add(Calendar.HOUR, hours);
		return simpleDateFormat.format(calendar.getTime());
	}

	public static String getDateTimeAddDates(Date dateTime, Integer hours) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.yyyy_MM_dd);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add(Calendar.DATE, hours);
		return simpleDateFormat.format(calendar.getTime());
	}

	public static Date getDateTimeByLong(Long createDateTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(createDateTime);
		return calendar.getTime();
	}

	public static String getTimeByLong(Long time) {
		if (time == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.yyyy_MM_ddHHmmss);

		String sd = sdf.format(new Date(time));

		return sd;

	}

	/**
	 * 一个月的最大天数 ay 2017年5月23日
	 * 
	 * @param Month
	 * @return
	 * @throws Exception
	 */
	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int cc = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return cc;
	}

	/**
	 * 获取这个月的第一天 ay 2017年5月23日
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthFirstDay(Date date) {
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM");
		return sdf0.format(date) + "-01 00:00:00";
	}

	/**
	 * 获取这个月的最后一天 ay 2017年5月23日
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthLastDay(Date date) {
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM");
		int cc = 30;
		try {
			cc = getDayOfMonth(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf0.format(date) + "-" + cc + " 23:59:59";
	}

	
	public static Long getMonthAgo() {
		 Calendar cal = Calendar.getInstance(); 
		 cal.add(Calendar.MONTH, -1); 
		return cal.getTimeInMillis();
	}
	
	public static Date addMonth(Date nd, int month) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(nd);
		rightNow.add(Calendar.MONTH, month);
		Date dt = rightNow.getTime();
		return dt;
	}
	
	public static Date addDate(Date nd, int date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(nd);
		rightNow.add(Calendar.DATE, date);
		Date dt = rightNow.getTime();
		return dt;
	}

	
	  public static boolean isDate(String date)    
      {    
          /**  
           * 判断日期格式和范围  
           */    
          String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";    
              
          Pattern pat = Pattern.compile(rexp);      
              
          Matcher mat = pat.matcher(date);      
              
          boolean dateType = mat.matches();    
  
          return dateType;    
      }  
	  public static void main(String[] args) throws ParseException {
		System.out.println(isDate("20170405"));
		System.out.println(stringConvertDate("20170405", DateUtil.yyyyMMdd));
		
		String datestr = "20110219";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Date strToDate = sdf.parse(datestr);
		System.out.println(dateConvertString(strToDate,DateUtil.yyyy_MM_dd));
	}
	/**
	 * 根据有效期计算有效天数
	 * 
	 * @param validityDate
	 * @return
	 */

	public static Long getExpireDate(String validityDate) {
		try {
			if (StringUtil.isNull(validityDate)) {
				return null;
			}
			validityDate=validityDate.trim();
			int length = validityDate.length();
			if (length == 10) {
				validityDate = validityDate + " 00:00:00";
			}
			if (length == 8) {
				String years =  validityDate.substring(0, 4);
				String month = validityDate.substring(4, 6);
				String day = validityDate.substring(6, 8);
				validityDate = years + "-" + month + "-" + day + " 00:00:00";
			}
			if (length == 6) {
				
				String years = getSystemDate().substring(0, 2) + validityDate.substring(0, 2);
				String month = validityDate.substring(2, 4);
				String day = validityDate.substring(4, 6);
				validityDate = years + "-" + month + "-" + day + " 00:00:00";
			}
			if (!validityDate.matches("\\d{4}-\\d{1,2}-\\d{1,2} 00:00:00")) {
				return null;
			}
			if (DateUtil.before(validityDate, DateUtil.getSystemDate())) {
				return -1L;
			} else {
				return DateUtil.getDiffDays(DateUtil.stringConvertDate(validityDate, DateUtil.yyyy_MM_ddHHmmss), new Date());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public static Long stringConvertTime(String timeContent, String formatStyle) {
		SimpleDateFormat format = new SimpleDateFormat(formatStyle);
		try {
			Date date = format.parse(timeContent);
			if(null!=date)
				return date.getTime();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到n天之后是周几
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(int days) {
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 给指定时间添加分钟
	 * 
	 * @param nd
	 * @param min
	 * @return
	 */
	public static Date addMForDate(Date nd, int min) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(nd);
		rightNow.add(Calendar.MINUTE, min);
		Date dt = rightNow.getTime();
		return dt;
	}
	
	/**
	 * 
	 * @param date
	 * @param field {@link Calendar}
	 * @param amount
	 * @return
	 */
	public static Date add(Date date ,int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}
	
	/**
	 * 校验用户输入是否满足日期格式   只是针对效期
	 * @param expiryDate
	 * @return
	 */
	public static boolean checkDateForExpiryDate(String expiryDate){
		if(StringUtil.isNull(expiryDate)){
			return  false;
		}
		//直接满足日期格式
		if(DateUtil.checkDate(expiryDate, DateUtil.yyyy_MM_dd)&&DateUtil.isDate(expiryDate)){
			return true;
		}
		if(expiryDate.length()!=8){
			return false;
		}
		//拼接之后满足日期格式
		String expiryDateTemp=expiryDate.substring(0, 4)+"-"+expiryDate.substring(4, 6)+"-"+expiryDate.substring(6, 8);
		if(!DateUtil.checkDate(expiryDateTemp, DateUtil.yyyy_MM_dd)||!DateUtil.isDate(expiryDate)){
				return false;
		}
		return true;
	}
	
	/**
	 * 解析前端传过来的效期  
	 * @param date
	 * @param format
	 * @return
	 */
	public static String parseDateForExpiryDate(String expiryDate){
		if(StringUtil.isNull(expiryDate)){
			return  null;
		}
		if(DateUtil.checkDate(expiryDate, DateUtil.yyyy_MM_dd)){
			return expiryDate;
		}
		if(expiryDate.length()!=8){
			return null;
		}
		String expiryDateTemp=expiryDate.substring(0, 4)+"-"+expiryDate.substring(4, 6)+"-"+expiryDate.substring(6, 8);
		if(DateUtil.checkDate(expiryDateTemp, DateUtil.yyyy_MM_dd)){
				return expiryDateTemp;
		}
		return null;
	}
	
	/**
	 * 格式化模板：yyyy-MM-dd HH:mm:ss
	*/
	public static String format(Long timeMillis) {
		return format(timeMillis, "");
	}
	/**
	 * 格式化模板：yyyy-MM-dd HH:mm:ss
	 * @param nullValueFormat	timeMillis为空或转换失败时返回的值
	 * @return
	 */
	public static String format(Long timeMillis,String nullValueFormat) {
		if(null==timeMillis)return nullValueFormat;
		try {
			SimpleDateFormat format = new SimpleDateFormat(DateUtil.yyyy_MM_ddHHmmss);
			return format.format(new Date(timeMillis));
		} catch (Exception e) {
			return nullValueFormat;
		}
	}
	/**
	 * 
	 * @param dateTimeStr yyyy/MM/dd [HH:mm:ss[.SSS]] 或 yyyy-MM-dd [HH:mm:ss[.SSS]]
	 * @return
	 */
	public static Date parse(String dateTimeStr) {
		if(null==dateTimeStr)return null;
		try {
			String str = dateTimeStr.replaceAll("/", "-").trim();
			switch (str.length()) {
			case 8:
				return stringConvertDate(str, "yyyyMMdd");
			case 10:
				return stringConvertDate(str, yyyy_MM_dd);
			case 19:
				return stringConvertDate(str, yyyy_MM_ddHHmmss);
			case 21:
				return stringConvertDate(str, yyyy_MM_ddHHmmssS);
			case 22:
				return stringConvertDate(str, yyyy_MM_ddHHmmssSS);
			case 23:
				return stringConvertDate(str, yyyy_MM_ddHHmmssSSS);
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 比较两个日期是否相等，都为null时相等
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isEqual(Date d1, Date d2) {
		if(null==d1 && null==d2)
			return true;
		if(null==d1 || null==d2)
			return false;
		return d1.equals(d2);
	}
	/**
	 * d1 是否 比d2 早
	 * null被当成9999-12-31 23:59:59
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean before(Date d1, Date d2) {
		Date date1 = null==d1 ? MAX_DATE_TIME : d1; 
		Date date2 = null==d2 ? MAX_DATE_TIME : d2;
		return date1.before(date2);
	}
	
}
