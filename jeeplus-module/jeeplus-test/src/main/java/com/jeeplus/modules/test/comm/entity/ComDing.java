/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.comm.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 钉钉推送Entity
 * @author lh
 * @version 2021-03-22
 */
@Data
public class ComDing extends DataEntity<ComDing> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="来源类型", align=2, sort=1)
	private String sourceType;		// 来源类型
	@ExcelField(title="来源", align=2, sort=2)
	private String source;		// 来源
	@ExcelField(title="推送类型", align=2, sort=3)
	private String dingType;		// 推送类型
	@ExcelField(title="钉钉推送", align=2, sort=4)
	private String dingId;		// 钉钉推送
	
	public ComDing() {
		super();
	}
	
	public ComDing(String id){
		super(id);
	}
}