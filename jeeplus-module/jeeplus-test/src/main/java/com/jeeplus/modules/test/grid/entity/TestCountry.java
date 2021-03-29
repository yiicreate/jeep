/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.grid.entity;

import com.jeeplus.modules.test.grid.entity.TestContinent;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 国家Entity
 * @author lgf
 * @version 2021-01-05
 */
@Data
public class TestCountry extends DataEntity<TestCountry> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="国名", align=2, sort=1)
	private String name;		// 国名
	@ExcelField(title="人口", align=2, sort=2)
	private String sum;		// 人口
    @NotNull(message="所属洲不能为空")
	@ExcelField(title="所属洲", fieldType=TestContinent.class, value="continent.name", align=2, sort=3)
	private TestContinent continent;		// 所属洲
	
	public TestCountry() {
		super();
	}
	
	public TestCountry(String id){
		super(id);
	}
}