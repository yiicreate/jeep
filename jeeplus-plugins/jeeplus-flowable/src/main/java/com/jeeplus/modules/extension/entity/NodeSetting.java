/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 节点设置Entity
 * @author 刘高峰
 * @version 2021-01-11
 */
@Data
public class NodeSetting extends DataEntity<NodeSetting> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="流程定义id", align=2, sort=1)
	private String processDefId;		// 流程定义id
	@ExcelField(title="节点id", align=2, sort=2)
	private String taskDefId;		// 节点id
	@ExcelField(title="变量名", align=2, sort=3)
	private String key;		// 变量名
	@ExcelField(title="变量值", align=2, sort=4)
	private String value;		// 变量值
	
	public NodeSetting() {
		super();
	}
	
	public NodeSetting(String id){
		super(id);
	}
}