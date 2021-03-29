/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.comm.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 公共Entity
 * @author lh
 * @version 2021-03-12
 */
@Data
public class ComFiles extends DataEntity<ComFiles> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="来源", align=2, sort=1)
	private String source;		// 来源
	@ExcelField(title="附件所属", align=2, sort=2)
	private String ownerId;		// 附件所属
	@ExcelField(title="附件地址", align=2, sort=3)
	private String url;		// 附件地址
	@ExcelField(title="附件类型", align=2, sort=4)
	private String type;		// 附件类型
	
	public ComFiles() {
		super();
	}
	
	public ComFiles(String id){
		super(id);
	}

	public ComFiles(String ownerId,String source){
		this();
		this.ownerId = ownerId;
		this.source = source;
	}
}