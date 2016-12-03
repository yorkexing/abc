package com.team.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class DateTimeUtil.
 */
public class DateTimeUtil {
    /** The logger. */

    /** The DEFAUL t_ pattern. */
    public static String DEFAULT_PATTERN = "yyyy.MM.dd HH:mm:ss";

    public static String DATE_PATTERN = "yyyy.MM.dd";

    /** The DEFAUL t_ pattern. */
    public static final String MASS_MSG_PATTERN = "yyyyMMddHHmmss";

    public static String RFCFormat = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final String MILLISECOND_PATTERN = "yyyyMMddHHmmssSSS";

    public static String LAST_MO_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取格式化的时间字符串.
     * 
     * @param pattern
     *            the pattern
     * @param date
     *            the date
     * @return the format date str
     * @return
     */
    public static String getFormatDateStr(String pattern, Date date) {
        if (null == pattern || pattern.trim().length() == 0) {
            pattern = DEFAULT_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        if (null == date) {
            date = new Date();
        }
        String dateStr = sdf.format(date);
        return dateStr;
    }
    
    /**
     * 获取格式化的时间字符串.
     * 
     * @param pattern
     *            the pattern
     * @param date
     *            the date
     * @return the format date str
     * @return
     */
    public static String getYearStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        if (null == date) {
            date = new Date();
        }
        String yearStr = sdf.format(date);
        return yearStr;
    }

    
    
    /**
     * 获取当前时间对象
     * 
     * @param pattern
     *            时间格式 默认格式为: yyyy/MM/dd HH:mm:ss
     * @param source
     *            时间字符串
     * @return String
     */
    public static Date getDateOBJ(String pattern, String source) {
        if (null == pattern || pattern.trim().length() == 0) {
            pattern = DEFAULT_PATTERN;
        }
        if (null == source) {
            source = getFormatDateStrOwn(DEFAULT_PATTERN);
        }
        Date date;
        try {
            date = new SimpleDateFormat(pattern).parse(source);
        } catch (Exception e) {
            date = new Date();
        }
        return date;
    }

    /**
     * 获取当前时间的格式化的时间字符串.
     * 
     */
    public static String getFormatDateStr(Date date) {
        if (null == date) {
            date = new Date();
        }

        String pattern = DEFAULT_PATTERN;

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 获取当前时间的格式化的时间字符串.
     * 
     */
    public static String getFormatDateStrDefault() {

        String pattern = DEFAULT_PATTERN;
        String dateStr = getFormatDateStrOwn(pattern);
        return dateStr;
    }
    
    /**
     * 获取当前时间的格式化的时间字符串. DateTimeUtil的MASS_MSG_PATTERN
     * @param pattern pattern
     */
    public static String getFormatDate(String pattern) {
        String dateStr = getFormatDateStrOwn(pattern);
        return dateStr;
    }

    /**
     * 内部条用方法
     * 
     */
    private static String getFormatDateStrOwn(String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    /**
     * 将字符串转换为时间.
     * 
     * @param pattern
     *            the pattern
     * @param dateStr
     *            the date str
     * @return the date
     * @return
     */
    public static Date parseStringToDate(String pattern, String dateStr) {
        if (null == pattern || pattern.trim().length() == 0) {
            pattern = DEFAULT_PATTERN;
        }

        if (null == dateStr || dateStr.trim().length() == 0) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * date1比date2小，即date1比date2早， 返回-1 date1比date2大，即date1比date2晚，返回1
     * 一样大，或者任意一个为空，返回0；.
     * 
     * @param date1
     *            the date1
     * @param date2
     *            the date2
     * @return the int
     * @return
     */
    public static int compare(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return 0;
        }
        if (date1.before(date2)) {
            return -1;
        }
        if (date1.after(date2)) {
            return 1;
        }
        if (date1.getTime() == date2.getTime()) {
            return 0;
        }
        return 0;
    }

    /**
     * 将时间往后调，millis为正数，往前为负数, millis单位为毫秒.
     * 
     * @param date
     *            the date
     * @param millis
     *            the millis
     * @return the date
     * @return
     */
    public static Date adjustDate(Date date, long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime() + millis);
        return c.getTime();
    }

    /**
     * 2014-09-26T11:21:00+08:00</createtime>
     * 
     * @param date
     * @return
     */
    public static String getRFC399DateStr(Date d) {
        if (d == null) {
            d = new Date();
        }

        SimpleDateFormat s = new SimpleDateFormat(RFCFormat);
        String time = s.format(d);
        int length = time.length();
        time = time.substring(0, length - 2) + ":" + time.substring(length - 2);

        return time;
    }

    /**
     * 比较date1比date2早多少天/月/年
     * 
     * @param date1
     *            需要比较的时间 不能为空(null),需要正确的日期格式
     * @param date2
     *            被比较的时间 为空(null)则为当前时间
     * @param type
     *            0:比较日期， 1：比较月份， 2：比较年
     * @param pattern
     *            日期格式： 需要和传入的date1,date2的格式一致
     * @return 返回正数，表示相差的天/月/年，返回-1表示date1比date2晚
     */
    public static int compareDate(String date1, String date2, int type, String pattern) {

        // 如果为空，设置为当前时间
        if (null == date2) {
            date2 = DateTimeUtil.getFormatDateStr(pattern, new Date());
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(parseStringToDate(pattern, date1));
        c2.setTime(parseStringToDate(pattern, date2));

        // List list = new ArrayList();
        int n = 0;
        while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
            // list.add(c.getTime()); // 这里可以把间隔的日期存到数组中 打印出来
            n++;
            if (type == 1) {
                c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
            } else {
                c1.add(Calendar.DATE, 1); // 比较天数，日期+1
            }
        }

        n = n - 1;

        if (type == 2) {
            n = n / 365;
        }
        return n;
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        String date = DateTimeUtil.getFormatDateStr("yyyy-MM-dd", new Date());
        String changeDate = "2015.08.08 17:09:22";
        long adjust = 30 * 24 * 60 * 60 * 1000l;

        String changeAddValidDays = getFormatDateStr(
                DateTimeUtil.adjustDate(DateTimeUtil.parseStringToDate(DEFAULT_PATTERN, changeDate), adjust));

        String now = "2015.08.10";
        System.out.println(DateTimeUtil.compareDate(now, changeAddValidDays, 0, DATE_PATTERN));
        System.out.println(date);

        System.out.println(getRFC399DateStr(null));

    }
}
