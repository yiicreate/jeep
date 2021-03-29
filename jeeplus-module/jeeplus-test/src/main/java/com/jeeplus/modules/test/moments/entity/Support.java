/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 委员圈点赞Entity
 * @author lh
 * @version 2021-03-10
 */
@Data
public class Support extends DataEntity<Support> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="评论主键", align=2, sort=1)
	private String pid;		// 评论主键
	@ExcelField(title="类型点赞1评论2", align=2, sort=2)
	private Integer type;		// 类型点赞1评论2
	@ExcelField(title="内容", align=2, sort=3)
	private String content;		// 内容
	@ExcelField(title="委员圈", align=2, sort=4)
	private String shareId;		// 委员圈

	/**
	 * 是否当前用户，不是则不能修改和删除
	 */
	public String isCurrent;
	
	public Support() {
		super();
	}
	
	public Support(String id){
		super(id);
	}

}