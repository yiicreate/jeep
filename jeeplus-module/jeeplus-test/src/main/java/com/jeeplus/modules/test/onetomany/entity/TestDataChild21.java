/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.onetomany.entity;

import com.jeeplus.modules.sys.entity.Area;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 火车票Entity
 * @author liugf
 * @version 2021-01-05
 */
@Data
public class TestDataChild21 extends DataEntity<TestDataChild21> {
	
	private static final long serialVersionUID = 1L;
	@NotNull(message="出发地不能为空")
	@ExcelField(title="出发地", fieldType=Area.class, value="startArea.name", align=2, sort=1)
	private Area startArea;		// 出发地
	@NotNull(message="目的地不能为空")
	@ExcelField(title="目的地", fieldType=Area.class, value="endArea.name", align=2, sort=2)
	private Area endArea;		// 目的地
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="出发时间不能为空")
	@ExcelField(title="出发时间", align=2, sort=3)
	private Date starttime;		// 出发时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="到达时间不能为空")
	@ExcelField(title="到达时间", align=2, sort=4)
	private Date endtime;		// 到达时间
	@NotNull(message="代理价格不能为空")
	@ExcelField(title="代理价格", align=2, sort=5)
	private Double price;		// 代理价格
	private TestDataMainForm testDataMain;		// 业务主表ID 父类
	
	public TestDataChild21() {
		super();
	}

	public TestDataChild21(String id){
		super(id);
	}

	public TestDataChild21(TestDataMainForm testDataMain){
		this.testDataMain = testDataMain;
	}

}