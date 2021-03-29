/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.tree.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.test.tree.entity.TestTree;
import com.jeeplus.modules.test.tree.mapper.TestTreeMapper;

/**
 * 组织机构Service
 * @author lgf
 * @version 2021-01-05
 */
@Service
@Transactional(readOnly = true)
public class TestTreeService extends TreeService<TestTreeMapper, TestTree> {

	public TestTree get(String id) {
		return super.get(id);
	}

	public List<TestTree> findList(TestTree testTree) {
		if (StringUtils.isNotBlank(testTree.getParentIds())){
			testTree.setParentIds(","+testTree.getParentIds()+",");
		}
		return super.findList(testTree);
	}

	@Transactional(readOnly = false)
	public void save(TestTree testTree) {
		super.save(testTree);
	}

	@Transactional(readOnly = false)
	public void delete(TestTree testTree) {
		super.delete(testTree);
	}

}