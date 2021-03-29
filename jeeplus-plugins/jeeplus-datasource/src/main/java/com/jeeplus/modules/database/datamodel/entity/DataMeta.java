/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.entity;


import com.jeeplus.core.persistence.DataEntity;

/**
 * 数据资源Entity
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
public class DataMeta extends DataEntity<DataMeta> {

    private static final long serialVersionUID = 1L;
    private String name;        // 字段名
    private String label;        // 字段描述
    private String type;        // 字段类型
    private Boolean isNeed;      // 是否需要分析该字段
    private DataSet dataSet;
    private int sort;//排序


    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public DataMeta() {
        super();
    }

    public DataMeta(String id) {
        super(id);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public Boolean getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(Boolean isNeed) {
        this.isNeed = isNeed;
    }
}
