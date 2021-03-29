/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 常用按钮Entity
 * @author 刘高峰
 * @version 2019-10-07
 */
public class Button extends DataEntity<Button> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String code;		// 编码
	private String sort;		// 排序
	
	public Button() {
		super();
	}

	public Button(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="编码", align=2, sort=2)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="排序", align=2, sort=3)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
}