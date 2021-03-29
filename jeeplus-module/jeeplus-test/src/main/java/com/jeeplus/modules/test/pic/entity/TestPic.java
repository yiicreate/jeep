/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.pic.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 图片管理Entity
 * @author lgf
 * @version 2021-01-05
 */
@Data
public class TestPic extends DataEntity<TestPic> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="标题", align=2, sort=1)
	private String title;		// 标题
	@ExcelField(title="图片路径", align=2, sort=2)
	private String pic;		// 图片路径
	
	public TestPic() {
		super();
	}
	
	public TestPic(String id){
		super(id);
	}
}