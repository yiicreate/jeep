/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.one.service;

import java.util.List;

import com.jeeplus.database.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.one.entity.LeaveForm;
import com.jeeplus.modules.test.one.mapper.LeaveFormMapper;

/**
 * 请假表单Service
 * @author lgf
 * @version 2021-01-05
 */
@DS ("mssql")
@Service
@Transactional(readOnly = true)
public class LeaveFormService extends CrudService<LeaveFormMapper, LeaveForm> {

	public LeaveForm get(String id) {
		return super.get(id);
	}

	public List<LeaveForm> findList(LeaveForm leaveForm) {
		return super.findList(leaveForm);
	}

	public Page<LeaveForm> findPage(Page<LeaveForm> page, LeaveForm leaveForm) {
		return super.findPage(page, leaveForm);
	}

	@Transactional(readOnly = false)
	public void save(LeaveForm leaveForm) {
		super.save(leaveForm);
	}

	@Transactional(readOnly = false)
	public void delete(LeaveForm leaveForm) {
		super.delete(leaveForm);
	}

}
