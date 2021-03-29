/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.books.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 在线听书看书Entity
 * @author lc
 * @version 2021-03-16
 */
@Data
public class ListenRead extends DataEntity<ListenRead> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="书籍名称", align=2, sort=1)
	private String bookName;		// 书籍名称
	@ExcelField(title="书籍作者", align=2, sort=2)
	private String bookAuthor;		// 书籍作者
	@ExcelField(title="书籍内容", align=2, sort=3)
	private String book;		// 书籍内容
	
	public ListenRead() {
		super();
	}
	
	public ListenRead(String id){
		super(id);
	}
}