/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.one.entity;

import com.jeeplus.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 请假表单Entity
 * @author lgf
 * @version 2021-01-05
 */
@Data
public class LeaveForm extends DataEntity<LeaveForm> {
	
	private static final long serialVersionUID = 1L;
    @NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", fieldType=Office.class, value="office.name", align=2, sort=1)
	private Office office;		// 归属部门
    @NotNull(message="员工不能为空")
	@ExcelField(title="员工", fieldType=User.class, value="tuser.name", align=2, sort=2)
	private User tuser;		// 员工
	@ExcelField(title="归属区域", align=2, sort=3)
	private String area;		// 归属区域
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message="请假开始日期不能为空")
	@ExcelField(title="请假开始日期", align=2, sort=4)
	private Date beginDate;		// 请假开始日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message="请假结束日期不能为空")
	@ExcelField(title="请假结束日期", align=2, sort=5)
	private Date endDate;		// 请假结束日期
	
	public LeaveForm() {
		super();
	}
	
	public LeaveForm(String id){
		super(id);
	}
}