/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.form.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.database.datalink.entity.DataSource;

/**
 * 数据表单Entity
 * @author 刘高峰
 * @version 2019-12-24
 */
public class Form extends DataEntity<Form> {

	private static final long serialVersionUID = 1L;
	private String code;		// 表单编码
	private String autoCreate;		// 是否自动建表
	private DataSource dataSource;		// 数据库连接
	private String name;		// 表单名称
	private String tableName;		// 表名
	private String source;		// 表单结构
	private String version;		// 版本号

	public Form() {
		super();
	}

	public Form(String id){
		super(id);
	}

	@ExcelField(title="表单编码", align=2, sort=1)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ExcelField(title="是否自动建表", dictType="yes_no", align=2, sort=2)
	public String getAutoCreate() {
		return autoCreate;
	}

	public void setAutoCreate(String autoCreate) {
		this.autoCreate = autoCreate;
	}

	@ExcelField(title="数据库连接", align=2, sort=3)
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@ExcelField(title="表单名称", align=2, sort=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="表名", align=2, sort=5)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@ExcelField(title="表单结构", align=2, sort=6)
	public String getSource() {
		return source;
	}



	public void setSource(String source) {
		this.source = source;
	}

	@ExcelField(title="版本号", align=2, sort=7)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
