/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.entity;


import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.database.datalink.entity.DataSource;

import java.util.List;

/**
 * 数据模型Entity
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
public class DataSet extends DataEntity<DataSet> {

    private static final long serialVersionUID = 1L;
    private DataSource db;        // 目标数据库
    private String name;        // 数据源名称
    private String sqlcmd;        // sql语句
    private List<DataMeta> columnList = Lists.newArrayList();    // 表列
    private List<DataParam> params = Lists.newArrayList();

    public DataSet() {
        super();
    }

    public DataSet(String id) {
        super(id);
    }

    public DataSource getDb() {
        return db;
    }

    public void setDb(DataSource db) {
        this.db = db;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlcmd() {
        return sqlcmd;
    }

    public void setSqlcmd(String sqlcmd) {
        this.sqlcmd = sqlcmd;
    }

    public List<DataMeta> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<DataMeta> columnList) {
        this.columnList = columnList;
    }

    public List<DataParam> getParams() {
        return params;
    }

    public void setParams(List<DataParam> params) {
        this.params = params;
    }
}
