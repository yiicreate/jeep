/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.extension.entity.Listener;
import com.jeeplus.modules.extension.mapper.ListenerMapper;

/**
 * 监听器Service
 * @author 刘高峰
 * @version 2019-10-14
 */
@Service
@Transactional(readOnly = true)
public class ListenerService extends CrudService<ListenerMapper, Listener> {

	public Listener get(String id) {
		return super.get(id);
	}
	
	public List<Listener> findList(Listener listener) {
		return super.findList(listener);
	}
	
	public Page<Listener> findPage(Page<Listener> page, Listener listener) {
		return super.findPage(page, listener);
	}
	
	@Transactional(readOnly = false)
	public void save(Listener listener) {
		super.save(listener);
	}
	
	@Transactional(readOnly = false)
	public void delete(Listener listener) {
		super.delete(listener);
	}
	
}