/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 流程表达式Entity
 * @author liugf
 * @version 2019-09-29
 */
public class Condition extends DataEntity<Condition> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String expression;		// 表达式
	
	public Condition() {
		super();
	}

	public Condition(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="表达式", align=2, sort=2)
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
}