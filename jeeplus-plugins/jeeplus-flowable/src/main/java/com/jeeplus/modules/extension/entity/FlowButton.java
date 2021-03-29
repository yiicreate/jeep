/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 按钮设置Entity
 * @author 刘高峰
 * @version 2020-12-23
 */
@Data
public class FlowButton extends DataEntity<FlowButton> {

	private static final long serialVersionUID = 1L;
	private String name;		// 按钮名称
	private String code;		// 编码
	private String isHide;		// 是否隐藏
	private String next;		// 下一节点审核人
	private Integer sort;		// 排序
	private TaskDefExtension taskDef;		// 任务节点外键 父类

	public FlowButton() {
		super();
	}

	public FlowButton(String id){
		super(id);
	}

	public FlowButton(TaskDefExtension taskDef){
		this.taskDef = taskDef;
	}

}
