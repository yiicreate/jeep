/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 审核人Entity
 * @author 刘高峰
 * @version 2020-12-23
 */
@Data
public class FlowAssignee extends DataEntity<FlowAssignee> {

	private static final long serialVersionUID = 1L;
	private String type;		// 用户类型
	private String value;		// 用户来自
	private String condition;		// 附加条件
	private String operationType;		// 运算类型
	private Integer sort;		// 排序
	private TaskDefExtension taskDef;		// 外键 父类

	public FlowAssignee() {
		super();
	}

	public FlowAssignee(String id){
		super(id);
	}

	public FlowAssignee(TaskDefExtension taskDef){
		this.taskDef = taskDef;
	}

}
