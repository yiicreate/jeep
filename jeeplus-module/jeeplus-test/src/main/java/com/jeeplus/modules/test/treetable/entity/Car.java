/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.treetable.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 车辆Entity
 * @author lgf
 * @version 2021-01-05
 */
 
@Data
public class Car extends DataEntity<Car> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="品牌", align=2, sort=1)
	private String name;		// 品牌
	private CarKind kind;		// 车系 父类
	
	public Car() {
		super();
	}

	public Car(String id){
		super(id);
	}

	public Car(CarKind kind){
		this.kind = kind;
	}
}