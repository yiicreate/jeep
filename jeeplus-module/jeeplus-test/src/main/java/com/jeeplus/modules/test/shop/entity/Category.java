/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.jeeplus.core.persistence.TreeEntity;
import lombok.Data;

/**
 * 商品类型Entity
 * @author liugf
 * @version 2021-01-05
 */
@Data 
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class Category extends TreeEntity<Category> {
	
	private static final long serialVersionUID = 1L;
	
	
	public Category() {
		super();
	}

	public Category(String id){
		super(id);
	}

	public  Category getParent() {
			return parent;
	}
	
	@Override
	public void setParent(Category parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}