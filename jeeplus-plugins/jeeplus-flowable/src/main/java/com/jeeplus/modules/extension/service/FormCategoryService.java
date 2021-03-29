/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.extension.entity.FormCategory;
import com.jeeplus.modules.extension.mapper.FormCategoryMapper;

/**
 * 流程分类Service
 * @author 刘高峰
 * @version 2020-02-02
 */
@Service
@Transactional(readOnly = true)
public class FormCategoryService extends TreeService<FormCategoryMapper, FormCategory> {

	public FormCategory get(String id) {
		return super.get(id);
	}
	
	public List<FormCategory> findList(FormCategory formCategory) {
		if (StringUtils.isNotBlank(formCategory.getParentIds())){
			formCategory.setParentIds(","+formCategory.getParentIds()+",");
		}
		return super.findList(formCategory);
	}
	
	@Transactional(readOnly = false)
	public void save(FormCategory formCategory) {
		super.save(formCategory);
	}
	
	@Transactional(readOnly = false)
	public void delete(FormCategory formCategory) {
		super.delete(formCategory);
	}
	
}