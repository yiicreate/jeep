/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.grid.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 洲Entity
 * @author lgf
 * @version 2021-01-05
 */
@Data
public class TestContinent extends DataEntity<TestContinent> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="洲名", align=2, sort=1)
	private String name;		// 洲名
	
	public TestContinent() {
		super();
	}
	
	public TestContinent(String id){
		super(id);
	}
}