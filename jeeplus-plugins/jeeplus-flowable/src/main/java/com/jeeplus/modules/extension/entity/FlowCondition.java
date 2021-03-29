/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 流程条件Entity
 * @author 刘高峰
 * @version 2020-12-23
 */
@Data
public class FlowCondition extends DataEntity<FlowCondition> {

	private static final long serialVersionUID = 1L;
	@ExcelField(title="变量名", align=2, sort=1)
	private String field;		// 变量名
	@ExcelField(title="比较类型", align=2, sort=2)
	private String compare;		// 比较类型
	@ExcelField(title="比较值", align=2, sort=3)
	private String value;		// 比较值
	@ExcelField(title="运算类型", align=2, sort=4)
	private String logic;		// 运算类型
	@ExcelField(title="排序", align=2, sort=5)
	private Integer sort;		// 排序
	private TaskDefExtension taskDef;		// 外键 父类

	public FlowCondition() {
		super();
	}

	public FlowCondition(String id){
		super(id);
	}

	public FlowCondition(TaskDefExtension taskDef){
		this.taskDef = taskDef;
	}

}
