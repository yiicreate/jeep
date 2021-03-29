/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 委员相关功能Entity
 * @author tangxin
 * @version 2021-03-24
 */
@Data
public class StandingCommittee extends DataEntity<StandingCommittee> {
	
	private static final long serialVersionUID = 1L;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束日期", align=2, sort=1)
	private Date endTime;		// 结束日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始日期", align=2, sort=2)
	private Date startTime;		// 开始日期
	@ExcelField(title="是否最新", align=2, sort=3)
	private Integer latest;		// 是否最新
	@ExcelField(title="届次", align=2, sort=4)
	private String period;		// 届次
	private List<CommitteeDetail> committeeDetailList = Lists.newArrayList();		// 子表列表
	
	public StandingCommittee() {
		super();
	}

	public StandingCommittee(String id){
		super(id);
	}
}