/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 积分明细Entity
 * @author tangxin
 * @version 2021-03-20
 */
@Data
public class IntegralDetail extends DataEntity<IntegralDetail> {
	
	private static final long serialVersionUID = 1L;
	private CommitteeIntegral ci;		// 积分外键 父类
	@ExcelField(title="积分", align=2, sort=2)
	private Integer integral;		// 积分
	@ExcelField(title="积分类型", dictType="integral_type", align=2, sort=3)
	private Integer integralType;		// 积分类型
	
	public IntegralDetail() {
		super();
	}

	public IntegralDetail(String id){
		super(id);
	}

	public IntegralDetail(CommitteeIntegral ci){
		this.ci = ci;
	}

}