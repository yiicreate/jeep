/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.activiti.entity;

import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 报销申请Entity
 * @author liugf
 * @version 2021-01-05
 */
@Data
public class TestActivitiExpense extends DataEntity<TestActivitiExpense> {
	
	private static final long serialVersionUID = 1L;
	@NotNull(message="员工姓名不能为空")
	@ExcelField(title="员工姓名", fieldType=User.class, value="user.name", align=2, sort=1)
	private User user;		// 员工姓名
	@ExcelField(title="流程实例ID", align=2, sort=2)
	private String procInsId;		// 流程实例ID
	@NotNull(message="报销费用不能为空")
	@ExcelField(title="报销费用", align=2, sort=3)
	private Integer cost;		// 报销费用
	@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", fieldType=Office.class, value="office.name", align=2, sort=4)
	private Office office;		// 归属部门
	@ExcelField(title="报销事由", align=2, sort=5)
	private String reason;		// 报销事由
	
	public TestActivitiExpense() {
		super();
	}

	public TestActivitiExpense(String id){
		super(id);
	}
}