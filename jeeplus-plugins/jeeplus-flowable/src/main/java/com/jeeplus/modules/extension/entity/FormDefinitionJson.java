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
public class FormDefinitionJson extends DataEntity<FormDefinitionJson> {
	
	private static final long serialVersionUID = 1L;
	private String formDefinitionId;		// 表单定义id
	private String json;		// 流程表单结构体
	private Integer version;		// 版本号
	private String status;		// 状态
	private String isPrimary;		// 是否主版本
	
	public FormDefinitionJson() {
		super();
	}

	public FormDefinitionJson(String id){
		super(id);
	}

	@ExcelField(title="表单定义id", align=2, sort=5)
	public String getFormDefinitionId() {
		return formDefinitionId;
	}

	public void setFormDefinitionId(String formDefinitionId) {
		this.formDefinitionId = formDefinitionId;
	}
	
	@ExcelField(title="流程表单结构体", align=2, sort=6)
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	@ExcelField(title="版本号", align=2, sort=7)
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@ExcelField(title="状态", align=2, sort=8)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="是否主版本", align=2, sort=9)
	public String getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}
	
}