/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.onetomany.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.test.onetomany.entity.TestDataMainForm;
import com.jeeplus.modules.test.onetomany.mapper.TestDataMainFormMapper;
import com.jeeplus.modules.test.onetomany.entity.TestDataChild21;
import com.jeeplus.modules.test.onetomany.mapper.TestDataChild21Mapper;
import com.jeeplus.modules.test.onetomany.entity.TestDataChild22;
import com.jeeplus.modules.test.onetomany.mapper.TestDataChild22Mapper;
import com.jeeplus.modules.test.onetomany.entity.TestDataChild23;
import com.jeeplus.modules.test.onetomany.mapper.TestDataChild23Mapper;

/**
 * 票务代理Service
 * @author liugf
 * @version 2021-01-05
 */
@Service
@Transactional(readOnly = true)
public class TestDataMainFormService extends CrudService<TestDataMainFormMapper, TestDataMainForm> {

	@Autowired
	private TestDataChild21Mapper testDataChild21Mapper;
	@Autowired
	private TestDataChild22Mapper testDataChild22Mapper;
	@Autowired
	private TestDataChild23Mapper testDataChild23Mapper;

	public TestDataMainForm get(String id) {
		TestDataMainForm testDataMainForm = super.get(id);
		testDataMainForm.setTestDataChild21List(testDataChild21Mapper.findList(new TestDataChild21(testDataMainForm)));
		testDataMainForm.setTestDataChild22List(testDataChild22Mapper.findList(new TestDataChild22(testDataMainForm)));
		testDataMainForm.setTestDataChild23List(testDataChild23Mapper.findList(new TestDataChild23(testDataMainForm)));
		return testDataMainForm;
	}

	public List<TestDataMainForm> findList(TestDataMainForm testDataMainForm) {
		return super.findList(testDataMainForm);
	}

	public Page<TestDataMainForm> findPage(Page<TestDataMainForm> page, TestDataMainForm testDataMainForm) {
		return super.findPage(page, testDataMainForm);
	}

	@Transactional(readOnly = false)
	public void save(TestDataMainForm testDataMainForm) {
		super.save(testDataMainForm);
		for (TestDataChild21 testDataChild21 : testDataMainForm.getTestDataChild21List()){
			if (testDataChild21.getId() == null){
				continue;
			}
			if (TestDataChild21.DEL_FLAG_NORMAL.equals(testDataChild21.getDelFlag())){
				if (StringUtils.isBlank(testDataChild21.getId())){
					testDataChild21.setTestDataMain(testDataMainForm);
					testDataChild21.preInsert();
					testDataChild21Mapper.insert(testDataChild21);
				}else{
					testDataChild21.preUpdate();
					testDataChild21Mapper.update(testDataChild21);
				}
			}else{
				testDataChild21Mapper.delete(testDataChild21);
			}
		}
		for (TestDataChild22 testDataChild22 : testDataMainForm.getTestDataChild22List()){
			if (testDataChild22.getId() == null){
				continue;
			}
			if (TestDataChild22.DEL_FLAG_NORMAL.equals(testDataChild22.getDelFlag())){
				if (StringUtils.isBlank(testDataChild22.getId())){
					testDataChild22.setTestDataMain(testDataMainForm);
					testDataChild22.preInsert();
					testDataChild22Mapper.insert(testDataChild22);
				}else{
					testDataChild22.preUpdate();
					testDataChild22Mapper.update(testDataChild22);
				}
			}else{
				testDataChild22Mapper.delete(testDataChild22);
			}
		}
		for (TestDataChild23 testDataChild23 : testDataMainForm.getTestDataChild23List()){
			if (testDataChild23.getId() == null){
				continue;
			}
			if (TestDataChild23.DEL_FLAG_NORMAL.equals(testDataChild23.getDelFlag())){
				if (StringUtils.isBlank(testDataChild23.getId())){
					testDataChild23.setTestDataMain(testDataMainForm);
					testDataChild23.preInsert();
					testDataChild23Mapper.insert(testDataChild23);
				}else{
					testDataChild23.preUpdate();
					testDataChild23Mapper.update(testDataChild23);
				}
			}else{
				testDataChild23Mapper.delete(testDataChild23);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(TestDataMainForm testDataMainForm) {
		super.delete(testDataMainForm);
		testDataChild21Mapper.delete(new TestDataChild21(testDataMainForm));
		testDataChild22Mapper.delete(new TestDataChild22(testDataMainForm));
		testDataChild23Mapper.delete(new TestDataChild23(testDataMainForm));
	}

}