/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 流程抄送Entity
 * @author 刘高峰
 * @version 2019-10-10
 */
public class FlowCopy extends DataEntity<FlowCopy> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 抄送用户id
	private String procDefId;		// 流程定义id
	private String procInsId;		// 流程实例id
	private String procDefName;		// 流程标题
	private String procInsName;		// 实例标题
	private String taskName;		// 流程节点
	
	public FlowCopy() {
		super();
	}

	public FlowCopy(String id){
		super(id);
	}

	@ExcelField(title="抄送用户id", align=2, sort=1)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@ExcelField(title="流程定义id", align=2, sort=2)
	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}
	
	@ExcelField(title="流程实例id", align=2, sort=3)
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@ExcelField(title="流程标题", align=2, sort=6)
	public String getProcDefName() {
		return procDefName;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}
	
	@ExcelField(title="实例标题", align=2, sort=7)
	public String getProcInsName() {
		return procInsName;
	}

	public void setProcInsName(String procInsName) {
		this.procInsName = procInsName;
	}
	
	@ExcelField(title="流程节点", align=2, sort=8)
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
}