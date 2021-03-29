/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.form.mapper;

import com.jeeplus.modules.form.entity.DataTable;
import com.jeeplus.modules.form.entity.DataTableColumn;
import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.form.entity.Form;

import java.util.List;

/**
 * 数据表单MAPPER接口
 * @author 刘高峰
 * @version 2019-12-24
 */
@Mapper
@Repository
public interface FormMapper extends BaseMapper<Form> {

    /**
     * 查询表列表
     *
     * @param dataTable
     * @return
     */
    List<DataTable> findTableList(DataTable dataTable);


    /**
     * 获取数据表字段
     *
     * @param dataTable
     * @return
     */
    List<DataTableColumn> findTableColumnList(DataTable dataTable);


    /**
     * 根据名称获取数据库表
     */
    DataTable findTableByName(DataTable dataTable);
}
