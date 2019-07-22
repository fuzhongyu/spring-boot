package com.fzy.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期工具类
 *
 * @author Fucai
 * @date 2018/3/19
 */
public class DateUtil extends DateUtils {


	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String formatDate(Date date) {
		return formatDate(date,"yyyy-MM-dd");
	}


	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}



	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null){
			return "";
		}
		if (pattern==null){
			pattern = "yyyy-MM-dd";
		}
		return DateFormatUtils.format(date, pattern);
	}


	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 转日期
	 */
	public static Date parseDate(String str) {
		return parseDate("yyyy-MM-dd",str);
	}
	
	/**
	 * 按照自定义的类型将时间字符串转换为时间
	 * @param typeStr：自定义转换类型(如：yyyy-MM-dd HH:mm:ss)
	 * @param dateStr：时间字符串
	 * @return
	 */
	public static Date parseDate(String typeStr,String dateStr){
		SimpleDateFormat format=new SimpleDateFormat(typeStr);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long diffDay(Date date) {
		return diffDay(new Date(),date);
	}

	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static long diffDay(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}


	/**
	 * 获取两个时间间的日期列表
	 * @param startDay 开始时间
	 * @param endDay 结束时间
	 * @return
	 */
	public static List<Date> betweenDay(Date startDay,Date endDay){
		List<Date> dayList=new ArrayList<>();
		if (startDay==null || endDay==null || startDay.getTime()>=endDay.getTime()){
			return dayList;
		}
		Date tmp = new Date(startDay.getTime());
		while (endDay.compareTo(tmp) >= 0) {
			dayList.add(tmp);
			tmp =addDay(tmp, 1);
		}
		return dayList;
	}



	/**
	 * 加减日期
	 * @param date 时间
	 * @param expire 加减时间长度（正数加，负数减）
	 * @return
	 */
	public static Date addDay(Date date,int expire){
		return addDate(date,Calendar.DAY_OF_YEAR,expire);
	}


	/**
	 * 获取两个月份间的月份列表
	 * @param startMonth 开始时间
	 * @param endMonth 结束时间
	 * @return
	 */
	public static List<Date> betweenMonth(Date startMonth,Date endMonth){
		List<Date> dayList=new ArrayList<>();
		if (startMonth==null || endMonth==null || startMonth.getTime()>=endMonth.getTime()){
			return dayList;
		}
		Date tmp = new Date(startMonth.getTime());
		while (endMonth.compareTo(tmp) >= 0) {
			dayList.add(tmp);
			tmp =addMonth(tmp, 1);
		}
		return dayList;
	}

	/**
	 * 加减月份
	 * @param date 时间
	 * @param expire 加减长度
	 * @return
	 */
	public static Date addMonth(Date date,int expire){
		return addDate(date,Calendar.MONTH,expire);
	}


	/**
	 * 获取两个月份间的月份列表
	 * @param startYear 开始时间
	 * @param endYear 结束时间
	 * @return
	 */
	public static List<Date> betweenYear(Date startYear,Date endYear){
		List<Date> dayList=new ArrayList<>();
		if (startYear==null || endYear==null || startYear.getTime()>=endYear.getTime()){
			return dayList;
		}
		Date tmp = new Date(startYear.getTime());
		while (endYear.compareTo(tmp) >= 0) {
			dayList.add(tmp);
			tmp =addYear(tmp, 1);
		}
		return dayList;
	}

	/**
	 * 加减年份
	 * @param date 时间
	 * @param expire 加减长度
	 * @return
	 */
	public static Date addYear(Date date,int expire){
		return addDate(date,Calendar.YEAR,expire);
	}
	
	/**
	 * 以当前时间为基准，在某个时间区域(年、月、日，时、分、秒)加减一段时间长度
	 * @param fieldType 时间区域
	 * @param expire 加减时间长度
	 * @return
	 */
	public static Date addDate(Date date,int fieldType, int expire){
		Calendar calendar=Calendar.getInstance();
		if (date==null){
			date = new Date();
		}
		calendar.setTime(date);
		calendar.add(fieldType,expire);
		return calendar.getTime();
	}
	
	/**
	 * 根据时间字符串获取其月份的第一天
	 * @param date
	 * @return
	 */
	public static Date getMonthFirstDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date==null?new Date():date);
		c.set(Calendar.DAY_OF_MONTH,1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		return c.getTime();
	}
	
	/**
	 * 根据时间字符串获取其月份的最后一天
	 * @param date
	 * @return
	 */
	public static Date getMonthLastDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date==null?new Date():date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		return c.getTime();
	}



	/**
	 * 获取一天的开始时间
	 * @param date
	 * @return
	 */
	public static Date getDayStartTime(Date date){
		if (date==null){
			return null;
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		return calendar.getTime();
	}


	/**
	 * 获取一天的结束时间
	 * @param date
	 * @return
	 */
	public static Date getDayEndTime(Date date){
		if (date==null){
			return null;
		}
		date=new Date(date.getTime()+24L*3600*1000);
		return getDayStartTime(date);
	}


}
