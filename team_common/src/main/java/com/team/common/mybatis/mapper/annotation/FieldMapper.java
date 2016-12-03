package com.team.common.mybatis.mapper.annotation;

import org.apache.ibatis.type.JdbcType;

// TODO: Auto-generated Javadoc
/**
 * 字段映射类，用于描述java对象字段和数据库表字段之间的对应关系.
 * 
 * @author david
 */
public class FieldMapper {
    
    /** Java对象字段名. */
    private String fieldName;
    
    /** 数据库表字段名. */
    private String dbFieldName;
    
    /** 数据库字段对应的jdbc类型. */
    private JdbcType jdbcType;

    /**
     * Gets the db field name.
     * 
     * @return the db field name
     */
    public String getDbFieldName() {
        return dbFieldName;
    }

    /**
     * Sets the db field name.
     * 
     * @param dbFieldName the new db field name
     */
    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    /**
     * Gets the field name.
     * 
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the field name.
     * 
     * @param fieldName the new field name
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Gets the jdbc type.
     * 
     * @return the jdbc type
     */
    public JdbcType getJdbcType() {
        return jdbcType;
    }

    /**
     * Sets the jdbc type.
     * 
     * @param jdbcType the new jdbc type
     */
    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }
}
