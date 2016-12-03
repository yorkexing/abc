package com.team.common.mybatis.mapper.annotation;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * 描述java对象的数据库映射信息（数据库表的映射、字段的映射）.
 * 
 * @author david
 */
public class TableMapper {

    /** The table mapper annotation. */
    private Annotation tableMapperAnnotation;

    /** The field mapper cache. */
    private Map<String, FieldMapper> fieldMapperCache;

    /** The field mapper list. */
    private List<FieldMapper> fieldMapperList;

    /**
     * Gets the field mapper list.
     * 
     * @return the field mapper list
     */
    public List<FieldMapper> getFieldMapperList() {
        return fieldMapperList;
    }

    /**
     * Sets the field mapper list.
     * 
     * @param fieldMapperList the new field mapper list
     */
    public void setFieldMapperList(List<FieldMapper> fieldMapperList) {
        this.fieldMapperList = fieldMapperList;
    }

    /**
     * Gets the table mapper annotation.
     * 
     * @return the table mapper annotation
     */
    public Annotation getTableMapperAnnotation() {
        return tableMapperAnnotation;
    }

    /**
     * Sets the table mapper annotation.
     * 
     * @param tableMapperAnnotation the new table mapper annotation
     */
    public void setTableMapperAnnotation(Annotation tableMapperAnnotation) {
        this.tableMapperAnnotation = tableMapperAnnotation;
    }

    /**
     * Gets the field mapper cache.
     * 
     * @return the field mapper cache
     */
    public Map<String, FieldMapper> getFieldMapperCache() {
        return fieldMapperCache;
    }

    /**
     * Sets the field mapper cache.
     * 
     * @param fieldMapperCache the field mapper cache
     * @version
     */
    public void setFieldMapperCache(Map<String, FieldMapper> fieldMapperCache) {
        this.fieldMapperCache = fieldMapperCache;
    }

}
