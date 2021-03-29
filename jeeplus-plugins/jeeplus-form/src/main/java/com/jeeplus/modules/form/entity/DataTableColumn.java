/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.form.entity;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 业务表字段Entity
 *
 * @author jeeplus
 * @version 2016-10-15
 */
public class DataTableColumn extends DataEntity<DataTableColumn> {

    private static final long serialVersionUID = 1L;
    private DataTable dataTable;    // 归属表
    private String name;        // 列名
    private String comments;    // 描述
    private String jdbcType;    // JDBC类型


    public DataTableColumn() {
        super();
    }

    public DataTableColumn(String id) {
        super(id);
    }

    public DataTableColumn(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    @Length(min = 1, max = 200)
    public String getName() {
        return StringUtils.lowerCase(name);
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * 获取列名和说明
     *
     * @return
     */
    public String getNameAndComments() {
        return getName() + (comments == null ? "" : "  :  " + comments);
    }


    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}


