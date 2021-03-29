/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.cppcc.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 政协通讯评论详情Entity
 * @author lc
 * @version 2021-03-15
 */
@Data
public class ComComments extends DataEntity<ComComments> {
	
	private static final long serialVersionUID = 1L;
	@NotNull(message="排序不能为空")
	@ExcelField(title="排序", align=2, sort=1)
	private Integer sort;		// 排序
	@ExcelField(title="父节点id", align=2, sort=2)
	private String parentId;		// 父节点id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="评论时间不能为空")
	@ExcelField(title="评论时间", align=2, sort=3)
	private Date commentTime;		// 评论时间
	@NotNull(message="评论人不能为空")
	@ExcelField(title="评论人", fieldType=User.class, value="commentUser.name", align=2, sort=4)
	private User commentUser;		// 评论人
	private ComData comData;		// 政协通讯管理主表id 父类
	
	public ComComments() {
		super();
	}

	public ComComments(String id){
		super(id);
	}

	public ComComments(ComData comData){
		this.comData = comData;
	}

}