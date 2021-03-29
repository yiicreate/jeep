/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.activiti.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 请假申请Entity
 * @author liugf
 * @version 2021-01-05
 */
@Data
public class TestActivitiLeave extends DataEntity<TestActivitiLeave> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="流程实例ID", align=2, sort=1)
	private String procInsId;		// 流程实例ID
	@ExcelField(title="请假类型", dictType="oa_leave_type", align=2, sort=2)
	private String leaveType;		// 请假类型
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="请假开始时间不能为空")
	@ExcelField(title="请假开始时间", align=2, sort=3)
	private Date startTime;		// 请假开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="请假结束时间不能为空")
	@ExcelField(title="请假结束时间", align=2, sort=4)
	private Date endTime;		// 请假结束时间
	@ExcelField(title="请假事由", align=2, sort=5)
	private String reason;		// 请假事由
	
	public TestActivitiLeave() {
		super();
	}

	public TestActivitiLeave(String id){
		super(id);
	}
}