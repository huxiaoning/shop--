package org.hxn.shopxx.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataUtil {
	
//	private static Calendar calendar = Calendar.getInstance();
	/**
	 * 获取随机整数(注意：可以取到边界值)
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandm(int min, int max) {
		return (int) (Math.random() * (max + 1 - min) + min);
	}
	
	/**
	 * 计算给定时间target增加或减少addAmount(年月日时分秒)
	 * @param target	目录操作时间
	 * @param amount	增加的天数，可以是负数表示减少
	 * @param field	代表年,月,日,时,分,秒
	 * @return
	 */
	public static Map<String,Date> getTimeRange(Date target,int field,int amount){
		Map<String,Date> map = new HashMap<String,Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(target);
		calendar.add(field,amount);
		Date begin = calendar.getTime();
		map.put("begin", begin);
		map.put("end", target);
		return map;
	}
	/**
	 * 计算两个时分秒部分相同的日期相差的天数
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getDays(Date begin, Date end) {
		long b = begin.getTime();
		long e = end.getTime();
		int days = (int) ((e - b) / 1000 / 60 / 60 / 24);
		return days;
	}
}
