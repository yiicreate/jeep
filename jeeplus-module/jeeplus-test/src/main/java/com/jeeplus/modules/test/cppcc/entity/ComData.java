/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.cppcc.entity;

import java.util.Date;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 政协通讯Entity
 * @author lc
 * @version 2021-03-15
 */
@Data
public class ComData extends DataEntity<ComData> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="政协通讯", align=2, sort=1)
	private String communicationData;		// 政协通讯
	private Date beginUpdateDate;		// 开始 更新时间
	private Date endUpdateDate;		// 结束 更新时间
	private List<ComComments> comCommentsList = Lists.newArrayList();		// 子表列表
	
	public ComData() {
		super();
	}

	public ComData(String id){
		super(id);
	}
}