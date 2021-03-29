/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 工作流扩展Entity
 * @author 刘高峰
 * @version 2020-12-23
 */
@Data
public class TaskDefExtension extends DataEntity<TaskDefExtension> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="流程定义id", align=2, sort=1)
	private String processDefId;		// 流程定义id
	@ExcelField(title="任务定义id", align=2, sort=2)
	private String taskDefId;		// 任务定义id
	private List<FlowAssignee> flowAssigneeList = Lists.newArrayList();		// 子表列表
	private List<FlowButton> flowButtonList = Lists.newArrayList();		// 子表列表
	private List<FlowCondition> flowConditionList = Lists.newArrayList();		// 子表列表
	
	public TaskDefExtension() {
		super();
	}

	public TaskDefExtension(String id){
		super(id);
	}
}