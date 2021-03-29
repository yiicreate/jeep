/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.comm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.comm.entity.ComDing;
import com.jeeplus.modules.test.comm.mapper.ComDingMapper;

/**
 * 钉钉推送Service
 * @author lh
 * @version 2021-03-22
 */
@Service
@Transactional(readOnly = false)
public class ComDingService extends CrudService<ComDingMapper, ComDing> {

	public ComDing get(String id) {
		return super.get(id);
	}
	
	public List<ComDing> findList(ComDing comDing) {
		return super.findList(comDing);
	}
	
	public Page<ComDing> findPage(Page<ComDing> page, ComDing comDing) {
		return super.findPage(page, comDing);
	}
	
	@Transactional(readOnly = false)
	public void save(ComDing comDing) {
		super.save(comDing);
	}
	
	@Transactional(readOnly = false)
	public void delete(ComDing comDing) {
		super.delete(comDing);
	}

	public ComDing getBySource(String sourceType,String source ){
		return mapper.getBySource(sourceType,source);
	}
	
}