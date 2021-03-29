/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.video.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 参与人Entity
 * @author lh
 * @version 2021-03-17
 */
@Data
public class Member extends DataEntity<Member> {
	
	private static final long serialVersionUID = 1L;
	private String uid;		// 钉钉唯一标识
	@ExcelField(title="是否参与", align=2, sort=2)
	private String isIn;		// 是否参与
	@ExcelField(title="视频主键", align=2, sort=3)
	private String liveId;		// 视频主键
	@ExcelField(title="是否发起人", align=2, sort=4)
	private String launch;		// 是否发起人
	@ExcelField(title="退出时间", align=2, sort=5)
	private String endTime;		// 退出时间
	@ExcelField(title="参与时间", align=2, sort=6)
	private String startTime;		// 参与时间
	private String userId;		// 用户
	@ExcelField(title="用户名", align=2, sort=7)
	private String name;		// 用户

	private String photo; //头像
	
	public Member() {
		super();
	}
	
	public Member(String id){
		super(id);
	}
}