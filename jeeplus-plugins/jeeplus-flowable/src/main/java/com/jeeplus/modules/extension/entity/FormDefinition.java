/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 流程表单Entity
 * @author 刘高峰
 * @version 2020-02-02
 */
public class FormDefinition extends DataEntity<FormDefinition> {

	private static final long serialVersionUID = 1L;
	private FormCategory category;		// 分类 父类
	private String name;		// 表单名称
	private FormDefinitionJson formDefinitionJson = new FormDefinitionJson();

	public FormDefinition() {
		super();
	}

	public FormDefinition(String id){
		super(id);
	}

	public FormDefinition(FormCategory category){
		this.category = category;
	}

	public FormCategory getCategory() {
		return category;
	}

	public void setCategory(FormCategory category) {
		this.category = category;
	}

	@ExcelField(title="表单名称", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FormDefinitionJson getFormDefinitionJson() {
		return formDefinitionJson;
	}

	public void setFormDefinitionJson(FormDefinitionJson formDefinitionJson) {
		this.formDefinitionJson = formDefinitionJson;
	}
}
