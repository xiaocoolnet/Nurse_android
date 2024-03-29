package chinanurse.cn.nurse.utils;

import java.util.Calendar;
import java.util.Locale;
public class TimeToolUtils {
	/**
	 * 获得当前时间，时间戳
	 * 
	 * @return 当前时间的时间戳
	 */
	public static long getNowTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 获得当前时间，按照给定的格式格式化<br/>
	 * 
	 * @param fromateType
	 *            格式化格式
	 * @return 当前时间
	 */
	public static String getNowTime(String fromateType) {
		return fromateTimeShow(getNowTime(), fromateType);
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 *            时间，UNIX时间戳格式
	 * @param fType
	 *            格式
	 * @return 格式化后的时间
	 */
	public static String fromateTimeShow(long time, String fType) {
		java.util.Date date = new java.util.Date(time);
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(fType, Locale.getDefault());
		return format.format(date);
	}

	/**
	 * @exception 
	 * 1分钟以内  显示“刚刚”  1小时以内    显示“几分钟前”，24小时以内  显示“几小时前”
	 * 30天内是x天前，12个月内是x月前，12个月以上是x年前
	 * @param time
	 * @return
	 */
	public static String fromateTimeShowByRule(long time){
		long timeDistance=getNowTime() - time;
		long years=timeDistance/(1000 * 60 * 60 * 24*365);
		long days =(timeDistance-years*(1000 * 60 * 60 * 24*365))/(1000 * 60 * 60 * 24);
		long hours = (timeDistance - years*(1000 * 60 * 60 * 24*365)- days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (timeDistance- years*(1000 * 60 * 60 * 24*365)-days*(1000 * 60 * 60 *24)-hours*(1000* 60 * 60))/(1000* 60);
		long second =(timeDistance- years*(1000 * 60 * 60 * 24*365)-days*(1000 * 60 * 60 *24)-hours*(1000* 60 * 60)-minutes*(1000* 60))/1000;
		if (years==0){
				if (days==0) {
					if (hours==0) {
						if (minutes==0) {
							return second+"秒前";
						}else{
							return minutes+"分钟前";
						}
					}else{
						return hours+"小时前";
					}
				}else if(days<=30){
					return days+"天前";
				}else{
					return days/30+"月前";
				}
		}else{
			return years+"年前";
		}
	}
	/**
	 * 判断一个UNIX时间戳是否是今天以前的时间<br/>
	 * 如果异常则返回true
	 * 
	 * @param time
	 *            要判断的时间
	 * @return true表示是今天以前的时间，false表示不是 @ 未知异常
	 */
	public static boolean ifBeforeToday(long time) {
		boolean result = true;
		try {
			long now = getTodayTime(0, 0, 0);
			if ((now - time) > 0) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			result = true;
		}
		return result;
	}

	/**
	 * 根据今天的时间，时、分、秒来获得一个UNIX时间戳<br/>
	 * 如果异常则返回0L
	 * 
	 * @param hours
	 *            时
	 * @param minute
	 *            分
	 * @param seconds
	 *            秒
	 * @return UNIX时间戳 @ 未知异常
	 */
	public static long getTodayTime(int hours, int minute, int seconds) {
		long result = 0L;
		try {
			Calendar nowC = Calendar.getInstance();
			nowC.set(nowC.get(Calendar.YEAR), nowC.get(Calendar.MONTH), nowC.get(Calendar.DAY_OF_MONTH), hours, minute, seconds);
			result = nowC.getTime().getTime();
		} catch (Exception e) {
			result = 0L;
		}
		return result;
	}

}