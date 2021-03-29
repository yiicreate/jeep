/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.video.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.util.List;

/**
 * 直播Entity
 * @author lh
 * @version 2021-03-17
 */
@Data
public class Live extends DataEntity<Live> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="步骤", align=2, sort=1)
	private String step;		// 步骤
	@ExcelField(title="开始时间", align=2, sort=2)
	private String planStart;		// 计划时间
	@ExcelField(title="结束时间", align=2, sort=3)
	private String planEnd;		// 计划时间
	@ExcelField(title="类型", align=2, sort=4)
	private String type;		// 类型 (直播1，会议2)
	@ExcelField(title="群列表ID", align=2, sort=5)
	private String groupId;		// 群列表ID
	@ExcelField(title="直播简介", align=2, sort=6)
	private String introduction;		// 直播简介
	@ExcelField(title="直播主题", align=2, sort=7)
	private String title;		// 直播主题
	private String endTime;		// 结束时间
	private String startTime;		// 开始时间
	@ExcelField(title="直播id", align=2, sort=9)
	private String liveUuid;		// 直播id

	private List<Member> members;
	
	public Live() {
		super();
	}
	
	public Live(String id){
		super(id);
	}
}