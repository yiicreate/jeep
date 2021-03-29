/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import lombok.Data;

import java.util.List;

/**
 * 委员圈Entity
 * @author lh
 * @version 2021-03-10
 */
@Data
public class Share extends DataEntity<Share> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="内容", align=2, sort=1)
	private String content;		// 内容

	private String invis;		// 不可见人员
	private String vis;		// 可见人员

	
	public Share() {
		super();
	}

	public Share(String id){
		super(id);
	}

	/**
	 * 是否当前用户，不是则不能修改和删除
	 */
	public String isCurrent;

	public List<Support> supports;

	public List<ComFiles> comFiles;

	public List<Remind> reminds;

	public List<User> invisUsers;

	public List<User> visUsers;

}