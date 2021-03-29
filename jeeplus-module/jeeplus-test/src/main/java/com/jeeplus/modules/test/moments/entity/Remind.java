/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 提醒人Entity
 * @author lh
 * @version 2021-03-18
 */
@Data
public class Remind extends DataEntity<Remind> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="姓名", align=2, sort=1)
	private String name;		// 姓名
	@ExcelField(title="提醒人", align=2, sort=2)
	private String user;		// 提醒人
	@ExcelField(title="委员圈", align=2, sort=3)
	private String shareId;		// 委员圈
	
	public Remind() {
		super();
	}
	
	public Remind(String id){
		super(id);
	}
}