/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.entity;


import com.jeeplus.core.persistence.DataEntity;

/**
 * 数据模型参数Entity
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
public class DataParam extends DataEntity<DataParam> {

    private static final long serialVersionUID = 1L;
    private DataSet dataSet;
    private String field;        // 字段
    private String defaultValue;// 映射关系
    private int sort;           //排序

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public DataParam() {
        super();
    }

    public DataParam(String id) {
        super(id);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }
}
