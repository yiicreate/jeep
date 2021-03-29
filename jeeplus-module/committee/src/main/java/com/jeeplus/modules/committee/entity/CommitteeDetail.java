/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 常委详情Entity
 * @author tangxin
 * @version 2021-03-24
 */
@Data
public class CommitteeDetail extends DataEntity<CommitteeDetail> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="头像", align=2, sort=1)
	private String photo;		// 头像
	@ExcelField(title="界别", dictType="sectors", align=2, sort=2)
	private String sector;		// 界别
	@ExcelField(title="所在组织", align=2, sort=3)
	private String org;		// 所在组织
	@ExcelField(title="常委名称", align=2, sort=4)
	private String name;		// 常委名称
	private StandingCommittee standing;		// 常委会id 父类
	
	public CommitteeDetail() {
		super();
	}

	public CommitteeDetail(String id){
		super(id);
	}

	public CommitteeDetail(StandingCommittee standing){
		this.standing = standing;
	}

}