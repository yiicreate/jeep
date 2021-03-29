/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 流程分类Entity
 * @author 刘高峰
 * @version 2019-10-09
 */
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class ActCategory extends TreeEntity<ActCategory> {
	
	private static final long serialVersionUID = 1L;
	
	
	public ActCategory() {
		super();
	}

	public ActCategory(String id){
		super(id);
	}

	public  ActCategory getParent() {
			return parent;
	}
	
	@Override
	public void setParent(ActCategory parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}