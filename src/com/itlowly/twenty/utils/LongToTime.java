package com.itlowly.twenty.utils;

/**
 * 把long类型转化成时间的工具类
 * 
 * @author lowly_pc
 *
 */
public class LongToTime {

	/**
	 * 获取剩余时间字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String getTime(Long time) {
		time = time / 1000;

		String hour = String.valueOf(time / 3600);

		String cen = String.valueOf((time - Integer.valueOf(hour) * 3600) / 60);

		String sce = String.valueOf(time - Integer.valueOf(hour) * 3600
				- Integer.valueOf(cen) * 60);

		if (cen.equals("0")) {
			cen = "00";
		}

		if (sce.equals("0")) {
			sce = "00";
		}

		return "剩余 " + hour + ":" + cen + ":" + sce;
	}

	
	public static int getTimeHour(Long time) {
		time=time/1000;
		return (int) (time / 3600);
	}

	public static int getTimecen(Long time) {
		int hour = getTimeHour(time);
		time=time/1000;
		return (int) ((time -  hour* 3600) / 60);
	}

	public static int getTimeSce(Long time) {
		int hour = getTimeHour(time);
		int cen = getTimecen(time);
		time=time/1000;
		return (int) (time - hour* 3600
				- cen * 60);
	}
}
