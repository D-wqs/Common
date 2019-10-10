package com.stamper.yx.common.sys;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhf_10@163.com
 * @Description
 * @date 2019/5/19 0019 10:33
 */
public class DateUtil {
	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);

	private static final String FORMAT_SHORT = "yyyy-MM-dd";
	private static final SimpleDateFormat shortSDF = new SimpleDateFormat(FORMAT_SHORT);


	/**
	 * 获取指定时间的Date对象
	 *
	 * @param time 8:30:00
	 * @return 当前8:30:00的Date对象
	 */
	public static Date getAppointDate(String time) {
		if (StringUtils.isNotBlank(time)) {
			String format = shortSDF.format(new Date()) + " " + time;
			Date date = null;
			try {
				date = sdf.parse(format);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		}
		return null;
	}

	/**
	 * 获取指定时间Date对象的字符串表现形式
	 * @param date
	 * @return 2019-11-12 9:00:45
	 */
	public static String format(Date date) {
		if (date != null) {
			return sdf.format(date);
		}
		return null;
	}

	public static Date parse(String time) {
		if (StringUtils.isNotBlank(time)) {
			try {
				return sdf.parse(time);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}



	public static String distanceOfTimeInWords(long fromTime, long toTime, String format) {
		return distanceOfTimeInWords(new Date(fromTime), new Date(toTime), format, 7);
	}

	public static String distanceOfTimeInWords(long fromTime, long toTime, String format, int days) {
		return distanceOfTimeInWords(new Date(fromTime), new Date(toTime), format, days);
	}

	public static String distanceOfTimeInWords(long fromTime, long toTime, int days) {
		return distanceOfTimeInWords(new Date(fromTime), new Date(toTime), "MM-dd HH:mm", days);
	}

	public static String distanceOfTimeInWords(long fromTime, long toTime) {
		return distanceOfTimeInWords(new Date(fromTime), new Date(toTime), "MM-dd HH:mm", 7);
	}

	public static String distanceOfTimeInWords(Date fromTime, Date toTime, int days) {
		return distanceOfTimeInWords(fromTime, toTime, "MM-dd HH:mm", days);
	}

	public static String distanceOfTimeInWords(Date fromTime, Date toTime, String format) {
		return distanceOfTimeInWords(fromTime, toTime, format, 7);
	}

	public static String distanceOfTimeInWords(Date fromTime, Date toTime) {
		return distanceOfTimeInWords(fromTime, toTime, "MM-dd HH:mm", 1);
	}

	public static String distanceOfTimeInWords(String fromTime) {
		try {
			Date fromDay = sdf.parse(fromTime);
			return distanceOfTimeInWords(fromDay, new Date());
		} catch (ParseException e) {
			return null;
		}
	}

	public static String distanceOfTimeInWords(Date fromTime) {
		return distanceOfTimeInWords(fromTime, new Date());
	}

	/**
	 * 截止时间时间到起始时间间隔的时间描述
	 *
	 * @param fromTime 起始时间
	 * @param toTime   截止时间
	 * @param format   格式化
	 * @param days     超过此天数，将按format格式化显示实际时间
	 * @return
	 */
	public static String distanceOfTimeInWords(Date fromTime, Date toTime, String format, int days) {
		long distanceInMinutes = (toTime.getTime() - fromTime.getTime()) / 60000;
		String message = "";
		if (distanceInMinutes == 0) {
			message = "几秒钟前";
		} else if (distanceInMinutes >= 1 && distanceInMinutes < 60) {
			message = distanceInMinutes + "分钟前";
		} else if (distanceInMinutes >= 60 && distanceInMinutes < 1400) {
			message = (distanceInMinutes / 60) + "小时前";
		} else if (distanceInMinutes >= 1440 && distanceInMinutes <= (1440 * days)) {
			message = (distanceInMinutes / 1440) + "天前";
		} else {
			message = new SimpleDateFormat(format).format(fromTime);
		}
		return message;
	}

	public static void main(String[] args) throws ParseException {
		String time = "2019-11-23 11:23:11";
		Date parse = parse(time);
		System.out.println(format(parse));
	}
}
