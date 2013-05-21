package com.pitaya.framework.utils;

import java.util.Date;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 User: xiaoshu Date: 2008-9-24 */
public class DateUtils { // repeat inventing the wheels - reiz
	public static String getTimeDiffString(Date date) {
		long diff = ((new Date()).getTime() - date.getTime()) / 1000;

		Calendar calendar = getCalendar(date);

		//second
		if (DateUtils.addTime(Calendar.SECOND, -60).getTime().before(date)) {
			return diff + "秒钟前";
		}

		//minute
		if (DateUtils.addTime(Calendar.MINUTE, -60).getTime().before(date)) {
			return (diff / 60) + "分钟前";
		}

		//hour
		if (DateUtils.addTime(Calendar.HOUR, -24).getTime().before(date)) {
			return (diff / 3600) + "小时前";
		}

		//day
		if (DateUtils.addTime(Calendar.MONTH, -1).getTime().before(date)) {
			return (diff / (3600 * 24)) + "天前";
		}

		//month
		if (DateUtils.addTime(Calendar.YEAR, -1).getTime().before(date)) {
			int x = DateUtils.getNow().get(Calendar.MONTH) - DateUtils.getCalendar(date).get(Calendar.MONTH);
			if(x>0) return x + "月前";
			else return (x+12) + "月前";
		}

		//year
		return (DateUtils.getNow().get(Calendar.YEAR) - calendar.get(Calendar.YEAR)) + "年前";
	}

	public static Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 取得当前时间
	 *
	 * @return Calendar
	 */
	public static Calendar getNow() {
		return DateUtils.getCalendar(new Date(System.currentTimeMillis()));
	}

	public static Calendar addTime(int calendarField, int amount) {
		Calendar calendar = getNow();
		calendar.add(calendarField, amount);
		return calendar;
	}

	public static final String DATE_PATTEN_YMD = "yyyy-MM-dd";
	public static final String DATE_PATTEN_YMDHM = "yyyy-MM-dd HH:mm";
	public static final String DATE_PATTEN_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTEN_YMDHMSSSS = "yyyy-MM-dd HH:mm:ss SSS";

	private static final SimpleDateFormat YMD = new SimpleDateFormat(DATE_PATTEN_YMD);
	private static final SimpleDateFormat YMDHM = new SimpleDateFormat(DATE_PATTEN_YMDHM);
	private static final SimpleDateFormat YMDHMS = new SimpleDateFormat(DATE_PATTEN_YMDHMS);
	private static final SimpleDateFormat YMDHMSSSS = new SimpleDateFormat(DATE_PATTEN_YMDHMSSSS);

	private static final Map<String, SimpleDateFormat> formatterCache = new HashMap<String, SimpleDateFormat>();

	static {
		formatterCache.put(DATE_PATTEN_YMD, YMD);
		formatterCache.put(DATE_PATTEN_YMDHM, YMDHM);
		formatterCache.put(DATE_PATTEN_YMDHMS, YMDHMS);
		formatterCache.put(DATE_PATTEN_YMDHMSSSS, YMDHMSSSS);
	}

	private static SimpleDateFormat getFormatter(String patten) {
		synchronized (formatterCache) {
			SimpleDateFormat formatter = formatterCache.get(patten);
			if (formatter == null) {
				try {
					formatter = new SimpleDateFormat(patten);
					formatter.format(DateUtils.getNow().getTime());
				} catch (Exception e) {
					formatter = null;
				}
				if(formatter == null) return null;
				else {
					formatterCache.put(patten,formatter);
				}
			}
			return formatter;
		}
	}

	public static String formatDate(String patten, Date date) {
		SimpleDateFormat formatter = DateUtils.getFormatter(patten);
		if(formatter == null) return null;
		else return formatter.format(date);
	}

	public static Date parseDate(String patten, String dateString) {
		SimpleDateFormat formatter = DateUtils.getFormatter(patten);
		if(formatter == null) return null;
		else {
			try{
				return formatter.parse(dateString);
			} catch (ParseException e){
				return null;
			}
		}
	}

	public static String formatDateYMD(Date date) {
		return DateUtils.formatDate(DATE_PATTEN_YMD,date);
	}

	public static Date parseDateYMD(String dateString) {
		return DateUtils.parseDate(DATE_PATTEN_YMD,dateString);
	}

	public static String formatDateYMDHM(Date date) {
		return DateUtils.formatDate(DATE_PATTEN_YMDHM,date);
	}

	public static Date parseDateYMDHM(String dateString) {
		return DateUtils.parseDate(DATE_PATTEN_YMDHM,dateString);
	}

	public static String formatDateYMDHMS(Date date) {
		return DateUtils.formatDate(DATE_PATTEN_YMDHMS,date);
	}

	public static Date parseDateYMDHMS(String dateString) {
		return DateUtils.parseDate(DATE_PATTEN_YMDHMS,dateString);
	}

	public static String formatDateYMDHMSSSS(Date date) {
		return DateUtils.formatDate(DATE_PATTEN_YMDHMSSSS,date);
	}

	public static Date parseDateYMDHMSSSS(String dateString) {
		return DateUtils.parseDate(DATE_PATTEN_YMDHMSSSS,dateString);
	}

	public static int getWeeksBetween(Date start, Date end) {
		if (start.after(end)) {
			return 0;
		}
		Calendar startCal = getCalendar(start);
		Calendar endCal = getCalendar(end);
		int weeks = 0;
		while(startCal.before(endCal)) {
			weeks ++;
			startCal.add(Calendar.DATE, 7);
		}
		return weeks;
	}
	public static int getDaysBetween(Date start, Date end) {
		if (start.after(end)) {
			return 0;
		}
		Calendar startCal = getCalendar(parseDateYMD(formatDateYMD(start)));
		Calendar endCal =  getCalendar(parseDateYMD(formatDateYMD(end)));
		int days = 0;
		while(startCal.before(endCal)) {
			days ++;
			startCal.add(Calendar.DATE, 1);
		}
		return days;
	}
	
//	public static void main(String args[]){
//		Date end = new Date();
//		Date start  = parseDateYMDHMS("2010-03-12 23:00:00");
//		System.out.println(getDaysBetween(start,end));
//	}
	

	public static int getYear(Date date) {
		Calendar c = DateUtils.getCalendar(date);
		return c.get(Calendar.YEAR);
	}

	public static int getYear() {
		return DateUtils.getYear(DateUtils.getNow().getTime());
	}

	public static int getMonth(Date date) {
		Calendar c = DateUtils.getCalendar(date);
		return c.get(Calendar.MONTH);
	}

	public static int getMonth() {
		// I wouldn't use this method because I don't understand the mean of this method from the method's signature.
		// Instead, I can use its sister as follows:
		// 		DateUtil.getMonth(new Date())
		// So I don't have to tell everyone using comment I will get the month of now.
		// --------------------------------------------------------------------------- reiz
		return DateUtils.getMonth(DateUtils.getNow().getTime());
	}

	public static int getDate(Date date) {
		Calendar c = DateUtils.getCalendar(date);
		return c.get(Calendar.DATE);
	}

	public static int getDate() {
		return DateUtils.getDate(DateUtils.getNow().getTime());
	}
	
	public static Date getDate(Date date,String patten){
		String dateString = formatDate(patten,date);
		return parseDate(patten,dateString);
	}
	
}
