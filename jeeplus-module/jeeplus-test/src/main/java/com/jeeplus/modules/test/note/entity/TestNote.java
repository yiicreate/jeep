/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.note.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 富文本测试Entity
 * @author liugf
 * @version 2021-01-05
 */
@Data
public class TestNote extends DataEntity<TestNote> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="标题", align=2, sort=7)
	private String title;		// 标题
	@ExcelField(title="内容", align=2, sort=8)
	private String contents;		// 内容
	
	public TestNote() {
		super();
	}
	
	public TestNote(String id){
		super(id);
	}
}