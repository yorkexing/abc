package com.team.common.util;

/**
 * 产生随机数
 * 
 * @author Administrator
 *
 */
public class RandomUtil {

	/**
	 * 根据位数位数获取随机数
	 * 
	 * @param count
	 *            count
	 * @return
	 */
	public static String getRandom(int count) {
		String strtmp = "";
		for (int i = 0; i < count; i++) {
			int randomcount = (int) (Math.random() * 9) + 1;
			strtmp = strtmp + randomcount;
		}

		return strtmp;

	}

}
