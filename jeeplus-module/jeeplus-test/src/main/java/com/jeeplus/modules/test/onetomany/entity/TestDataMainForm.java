/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.onetomany.entity;

import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Area;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 票务代理Entity
 * @author liugf
 * @version 2021-01-05
 */
@Data
public class TestDataMainForm extends DataEntity<TestDataMainForm> {
	
	private static final long serialVersionUID = 1L;
	@NotNull(message="用户不能为空")
	@ExcelField(title="用户", fieldType=User.class, value="tuser.name", align=2, sort=1)
	private User tuser;		// 用户
	@NotNull(message="所属部门不能为空")
	@ExcelField(title="所属部门", fieldType=Office.class, value="office.name", align=2, sort=2)
	private Office office;		// 所属部门
	@NotNull(message="所属区域不能为空")
	@ExcelField(title="所属区域", fieldType=Area.class, value="area.name", align=2, sort=3)
	private Area area;		// 所属区域
	@ExcelField(title="名称", align=2, sort=4)
	private String name;		// 名称
	@ExcelField(title="性别", dictType="sex", align=2, sort=5)
	private String sex;		// 性别
	@ExcelField(title="身份证照片", align=2, sort=6)
	private String file;		// 身份证照片
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="加入日期", align=2, sort=7)
	private Date inDate;		// 加入日期
	private Date beginInDate;		// 开始 加入日期
	private Date endInDate;		// 结束 加入日期
	private List<TestDataChild21> testDataChild21List = Lists.newArrayList();		// 子表列表
	private List<TestDataChild22> testDataChild22List = Lists.newArrayList();		// 子表列表
	private List<TestDataChild23> testDataChild23List = Lists.newArrayList();		// 子表列表
	
	public TestDataMainForm() {
		super();
	}

	public TestDataMainForm(String id){
		super(id);
	}
}