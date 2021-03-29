/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.books.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.books.entity.ListenRead;
import com.jeeplus.modules.test.books.mapper.ListenReadMapper;

/**
 * 在线听书看书Service
 * @author lc
 * @version 2021-03-16
 */
@Service
@Transactional(readOnly = true)
public class ListenReadService extends CrudService<ListenReadMapper, ListenRead> {

	public ListenRead get(String id) {
		return super.get(id);
	}
	
	public List<ListenRead> findList(ListenRead listenRead) {
		return super.findList(listenRead);
	}
	
	public Page<ListenRead> findPage(Page<ListenRead> page, ListenRead listenRead) {
		return super.findPage(page, listenRead);
	}
	
	@Transactional(readOnly = false)
	public void save(ListenRead listenRead) {
		super.save(listenRead);
	}
	
	@Transactional(readOnly = false)
	public void delete(ListenRead listenRead) {
		super.delete(listenRead);
	}
	
}