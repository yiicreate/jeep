/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.treetable.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeeplus.core.persistence.TreeEntity;
import lombok.Data;

/**
 * 车系Entity
 * @author lgf
 * @version 2021-01-05
 */
@Data
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class CarKind extends TreeEntity<CarKind> {
	
	private static final long serialVersionUID = 1L;
	
	private List<Car> carList = Lists.newArrayList();		// 子表列表
	
	public CarKind() {
		super();
	}

	public CarKind(String id){
		super(id);
	}

	public  CarKind getParent() {
			return parent;
	}
	
	@Override
	public void setParent(CarKind parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}