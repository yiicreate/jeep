/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.extension.entity.Condition;
import com.jeeplus.modules.extension.mapper.ConditionMapper;

/**
 * 流程表达式Service
 * @author liugf
 * @version 2019-09-29
 */
@Service
@Transactional(readOnly = true)
public class ConditionService extends CrudService<ConditionMapper, Condition> {

	public Condition get(String id) {
		return super.get(id);
	}
	
	public List<Condition> findList(Condition condition) {
		return super.findList(condition);
	}
	
	public Page<Condition> findPage(Page<Condition> page, Condition condition) {
		return super.findPage(page, condition);
	}
	
	@Transactional(readOnly = false)
	public void save(Condition condition) {
		super.save(condition);
	}
	
	@Transactional(readOnly = false)
	public void delete(Condition condition) {
		super.delete(condition);
	}
	
}