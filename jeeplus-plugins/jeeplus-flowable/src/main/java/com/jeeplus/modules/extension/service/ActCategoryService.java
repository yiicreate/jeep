/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.extension.entity.ActCategory;
import com.jeeplus.modules.extension.mapper.ActCategoryMapper;

/**
 * 流程分类Service
 * @author 刘高峰
 * @version 2019-10-09
 */
@Service
@Transactional(readOnly = true)
public class ActCategoryService extends TreeService<ActCategoryMapper, ActCategory> {

	public ActCategory get(String id) {
		return super.get(id);
	}
	
	public List<ActCategory> findList(ActCategory actCategory) {
		if (StringUtils.isNotBlank(actCategory.getParentIds())){
			actCategory.setParentIds(","+actCategory.getParentIds()+",");
		}
		return super.findList(actCategory);
	}
	
	@Transactional(readOnly = false)
	public void save(ActCategory actCategory) {
		super.save(actCategory);
	}
	
	@Transactional(readOnly = false)
	public void delete(ActCategory actCategory) {
		super.delete(actCategory);
	}
	
}