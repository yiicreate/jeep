/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 流程分类Entity
 * @author 刘高峰
 * @version 2020-02-02
 */
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class FormCategory extends TreeEntity<FormCategory> {
	
	private static final long serialVersionUID = 1L;
	
	private List<FormDefinition> formDefinitionList = Lists.newArrayList();		// 子表列表
	
	public FormCategory() {
		super();
	}

	public FormCategory(String id){
		super(id);
	}

	public  FormCategory getParent() {
			return parent;
	}
	
	@Override
	public void setParent(FormCategory parent) {
		this.parent = parent;
		
	}
	
	public List<FormDefinition> getFormDefinitionList() {
		return formDefinitionList;
	}

	public void setFormDefinitionList(List<FormDefinition> formDefinitionList) {
		this.formDefinitionList = formDefinitionList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}