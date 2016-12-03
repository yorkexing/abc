package com.team.common.util;

/**
 * 
 * @author Administrator
 *
 */
public class NULL {
    int sqltype = 0;

    /**
     * 构造函数
     * @param c c
     */
    public NULL(Class c) {
        if (c == String.class) {
            sqltype = java.sql.Types.VARCHAR;
        } else if (c == Long.class) {
            sqltype = java.sql.Types.INTEGER;
        } else if (c == Boolean.class) {
            sqltype = java.sql.Types.BOOLEAN;
        } else if (c == Integer.class) {
            sqltype = java.sql.Types.INTEGER;
        } else if (c == Short.class) {
            sqltype = java.sql.Types.SMALLINT;
        } else if (c == Float.class) {
            sqltype = java.sql.Types.FLOAT;
        } else if (c == Double.class) {
            sqltype = java.sql.Types.DOUBLE;
        } else if (c == java.sql.Time.class) {
            sqltype = java.sql.Types.TIME;
        } else if (c == java.sql.Date.class) {
            sqltype = java.sql.Types.DATE;
        } else if (c == java.sql.Timestamp.class || c == java.util.Date.class) {
            sqltype = java.sql.Types.TIMESTAMP;
        }
    }

    /**
     * getSqltype
     * @return int
     */
    public int getSqltype() {
        return sqltype;
    }
}
