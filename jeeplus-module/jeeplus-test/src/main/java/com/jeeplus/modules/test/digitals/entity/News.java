/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.digitals.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import lombok.Data;

import java.util.List;

/**
 * 数字化办公-新闻宣传Entity
 * @author lh
 * @version 2021-03-12
 */
@Data
public class News extends DataEntity<News> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="状态0草稿，1已发布", align=2, sort=1)
	private String step;		// 状态0草稿，1已发布
	@ExcelField(title="来源", align=2, sort=2)
	private String form;		// 来源
	@ExcelField(title="类别", align=2, sort=3)
	private String type;		// 类别
	@ExcelField(title="内容", align=2, sort=4)
	private String content;		// 内容
	@ExcelField(title="关键字", align=2, sort=5)
	private String keyword;		// 关键字
	@ExcelField(title="标题", align=2, sort=6)
	private String title;		// 标题

	private List<ComFiles> comFiles;
	
	public News() {
		super();
	}
	
	public News(String id){
		super(id);
	}
}