/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.test.shop.entity.Category;
import com.jeeplus.modules.test.shop.mapper.CategoryMapper;

/**
 * 商品类型Service
 * @author liugf
 * @version 2021-01-05
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends TreeService<CategoryMapper, Category> {

	public Category get(String id) {
		return super.get(id);
	}

	public List<Category> findList(Category category) {
		if (StringUtils.isNotBlank(category.getParentIds())){
			category.setParentIds(","+category.getParentIds()+",");
		}
		return super.findList(category);
	}

	@Transactional(readOnly = false)
	public void save(Category category) {
		super.save(category);
	}

	@Transactional(readOnly = false)
	public void delete(Category category) {
		super.delete(category);
	}

}