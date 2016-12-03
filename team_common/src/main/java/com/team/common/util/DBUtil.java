package com.team.common.util;

public class DBUtil {
	 private static final int DATE_LENGTH_10 = 10;
	  /**
     * 转化使用
     * @param o o
     * @param oclass oclass
     * @return Object
     */
    public static Object trans(Object o, Class oclass) {
        if (o != null && o instanceof String && ((String) o).trim().length() <= 0) {
            o = null;
        }
        if (o != null) {
            if (o instanceof String) {
                if (oclass.equals(java.sql.Date.class)) {
                    return strToDate((String) o, DateTimeUtil.DEFAULT_PATTERN);
                } else if (oclass.equals(java.sql.Time.class)) {
                    return strToTime((String) o, DateTimeUtil.DEFAULT_PATTERN);
                } else if (oclass.equals(java.sql.Timestamp.class)) {
                    return strToTimestamp((String) o, DateTimeUtil.DEFAULT_PATTERN);
                } else if (oclass.equals(Long.class)) {
                    return Long.valueOf((String) o);
                } else if (oclass.equals(Short.class)) {
                    return Short.valueOf((String) o);
                } else if (oclass.equals(Integer.class)) {
                    return Integer.valueOf((String) o);
                } else if (oclass.equals(Float.class)) {
                    return Float.valueOf((String) o);
                } else if (oclass.equals(Double.class)) {
                    return Double.valueOf((String) o);
                } else {
                    return o;
                }
            } else {
                return o;
            }
        } else {
            return new NULL(oclass);
        }
    }
    
    /**
     * 
     * @param value
     *            String
     * @param pattern
     *            String
     * @return Date
     */
    public static java.sql.Date strToDate(String value, String pattern) {
        if (value.length() <= DATE_LENGTH_10) {
            value = value + " 00:00:00";
        }
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(pattern);
        java.text.ParsePosition pos = new java.text.ParsePosition(0);
        return new java.sql.Date(format.parse(value, pos).getTime());
    }

    /**
     * 字符串按某种格式转成时间
     * 
     * @param value
     *            String
     * @param pattern
     *            String
     * @return Time
     */

    public static java.sql.Time strToTime(String value, String pattern) {
        if (value.length() <= DATE_LENGTH_10) {
            value = value + " 00:00:00";
        }
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(pattern);
        java.text.ParsePosition pos = new java.text.ParsePosition(0);
        return new java.sql.Time(format.parse(value, pos).getTime());
    }

    /**
     * 字符串按某种格式转成时间戳
     * 
     * @param value
     *            String
     * @param pattern
     *            String
     * @return Date
     */
    public static java.sql.Timestamp strToTimestamp(String value, String pattern) {

        if (value.length() <= DATE_LENGTH_10) {
            value = value + " 00:00:00";
        }
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(pattern);
        java.text.ParsePosition pos = new java.text.ParsePosition(0);
        java.util.Date date = format.parse(value, pos);
        return new java.sql.Timestamp(date.getTime());
    }
}
