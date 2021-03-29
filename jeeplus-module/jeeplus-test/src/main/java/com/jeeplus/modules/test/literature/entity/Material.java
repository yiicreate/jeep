/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.literature.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import lombok.Data;

import java.util.List;

/**
 * 数字文史Entity
 * @author lh
 * @version 2021-03-12
 */
@Data
public class Material extends DataEntity<Material> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="步骤草稿0，公开1，不公开2", align=2, sort=1)
	private String step;		// 步骤草稿0，公开1，不公开2
	@ExcelField(title="内容", align=2, sort=2)
	private String content;		// 内容
	@ExcelField(title="关键字", align=2, sort=3)
	private String keyword;		// 关键字
	@ExcelField(title="标题", align=2, sort=4)
	private String title;		// 标题

	private List<ComFiles> comFiles;
	
	public Material() {
		super();
	}
	
	public Material(String id){
		super(id);
	}
}