/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.activiti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.test.activiti.entity.TestActivitiLeave;
import com.jeeplus.modules.test.activiti.mapper.TestActivitiLeaveMapper;

/**
 * 请假申请Service
 * @author liugf
 * @version 2021-01-05
 */
@Service
@Transactional(readOnly = true)
public class TestActivitiLeaveService extends CrudService<TestActivitiLeaveMapper, TestActivitiLeave> {


	public TestActivitiLeave get(String id) {
		TestActivitiLeave testActivitiLeave = super.get(id);
		return testActivitiLeave;
	}

	public List<TestActivitiLeave> findList(TestActivitiLeave testActivitiLeave) {
		return super.findList(testActivitiLeave);
	}

	public Page<TestActivitiLeave> findPage(Page<TestActivitiLeave> page, TestActivitiLeave testActivitiLeave) {
		return super.findPage(page, testActivitiLeave);
	}

	@Transactional(readOnly = false)
	public void save(TestActivitiLeave testActivitiLeave) {
		super.save(testActivitiLeave);
	}

	@Transactional(readOnly = false)
	public void delete(TestActivitiLeave testActivitiLeave) {
		super.delete(testActivitiLeave);
	}

}