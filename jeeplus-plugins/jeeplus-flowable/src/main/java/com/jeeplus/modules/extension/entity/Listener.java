/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 监听器Entity
 * @author 刘高峰
 * @version 2019-10-14
 */
public class Listener extends DataEntity<Listener> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String listenerType;		// 监听器类型
	private String event;		// 事件
	private String valueType;		// 值类型
	private String value;		// 值
	
	public Listener() {
		super();
	}

	public Listener(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="监听器类型", dictType="", align=2, sort=2)
	public String getListenerType() {
		return listenerType;
	}

	public void setListenerType(String listenerType) {
		this.listenerType = listenerType;
	}
	
	@ExcelField(title="事件", dictType="", align=2, sort=3)
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	@ExcelField(title="值类型", dictType="", align=2, sort=4)
	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	
	@ExcelField(title="值", align=2, sort=5)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}