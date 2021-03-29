/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.tree.entity;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.jeeplus.core.persistence.TreeEntity;
import lombok.Data;

/**
 * 组织机构Entity
 * @author lgf
 * @version 2021-01-05
 */
@Data 
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class TestTree extends TreeEntity<TestTree> {
	
	private static final long serialVersionUID = 1L;
	
	
	public TestTree() {
		super();
	}

	public TestTree(String id){
		super(id);
	}

	public  TestTree getParent() {
			return parent;
	}
	
	@Override
	public void setParent(TestTree parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}