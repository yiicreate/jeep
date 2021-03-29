/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 委员积分Entity
 * @author tangxin
 * @version 2021-03-20
 */
@Data
public class CommitteeIntegral extends DataEntity<CommitteeIntegral> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="单位", fieldType=String.class, value="", align=2, sort=1)
	private String org;		// 单位
	@ExcelField(title="总分", align=2, sort=2)
	private Integer integral;		// 总分
	@ExcelField(title="委员ID", fieldType=String.class, value="", align=2, sort=3)
	private String committeeId;		// 委员ID
	private String committee;		// 委员
	private String office;		// 委员
	private List<IntegralDetail> integralDetailList = Lists.newArrayList();		// 子表列表
	
	public CommitteeIntegral() {
		super();
	}

	public CommitteeIntegral(String id){
		super(id);
	}
}