/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.form.entity;

import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.database.datalink.entity.DataSource;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 业务表Entity
 *
 * @author jeeplus
 * @version 2016-10-15
 */
public class DataTable extends DataEntity<DataTable> {

    private static final long serialVersionUID = 1L;
    private String name;    // 名称
    private String comments;        // 描述
    private List<DataTableColumn> columnList = Lists.newArrayList();    // 表列
    private DataSource dataSource;


    public DataTable() {
        super();
    }

    public DataTable(String id) {
        super(id);
    }

    @Length(min = 1, max = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    public List<DataTableColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<DataTableColumn> columnList) {
        this.columnList = columnList;
    }


    /**
     * 获取列名和说明
     *
     * @return
     */
    public String getNameAndComments() {
        return getName() + (comments == null ? "" : "  :  " + comments);
    }


    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}


