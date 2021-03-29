/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.activiti.entity;

import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.Office;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 薪酬调整申请Entity
 * @author liugf
 * @version 2021-01-05
 */
@Data
public class TestActivitiAudit extends DataEntity<TestActivitiAudit> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="流程实例ID", align=2, sort=1)
	private String procInsId;		// 流程实例ID
	@NotNull(message="变动用户不能为空")
	@ExcelField(title="变动用户", fieldType=User.class, value="user.name", align=2, sort=2)
	private User user;		// 变动用户
	@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", fieldType=Office.class, value="office.name", align=2, sort=3)
	private Office office;		// 归属部门
	@ExcelField(title="岗位", align=2, sort=4)
	private String post;		// 岗位
	@ExcelField(title="性别", dictType="sex", align=2, sort=5)
	private String sex;		// 性别
	@ExcelField(title="学历", align=2, sort=6)
	private String edu;		// 学历
	@ExcelField(title="调整原因", align=2, sort=7)
	private String content;		// 调整原因
	@ExcelField(title="现行标准 薪酬档级", align=2, sort=8)
	private String olda;		// 现行标准 薪酬档级
	@ExcelField(title="现行标准 月工资额", align=2, sort=9)
	private String oldb;		// 现行标准 月工资额
	@ExcelField(title="现行标准 年薪总额", align=2, sort=10)
	private String oldc;		// 现行标准 年薪总额
	@ExcelField(title="调整后标准 薪酬档级", align=2, sort=11)
	private String newa;		// 调整后标准 薪酬档级
	@ExcelField(title="调整后标准 月工资额", align=2, sort=12)
	private String newb;		// 调整后标准 月工资额
	@ExcelField(title="调整后标准 年薪总额", align=2, sort=13)
	private String newc;		// 调整后标准 年薪总额
	@ExcelField(title="月增资", align=2, sort=14)
	private String addNum;		// 月增资
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="执行时间不能为空")
	@ExcelField(title="执行时间", align=2, sort=15)
	private Date exeDate;		// 执行时间
	
	public TestActivitiAudit() {
		super();
	}

	public TestActivitiAudit(String id){
		super(id);
	}
}