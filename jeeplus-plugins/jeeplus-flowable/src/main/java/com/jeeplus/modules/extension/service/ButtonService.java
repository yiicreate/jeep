/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.extension.entity.Button;
import com.jeeplus.modules.extension.mapper.ButtonMapper;

/**
 * 常用按钮Service
 * @author 刘高峰
 * @version 2019-10-07
 */
@Service
@Transactional(readOnly = true)
public class ButtonService extends CrudService<ButtonMapper, Button> {

	public Button get(String id) {
		return super.get(id);
	}
	
	public List<Button> findList(Button button) {
		return super.findList(button);
	}
	
	public Page<Button> findPage(Page<Button> page, Button button) {
		return super.findPage(page, button);
	}
	
	@Transactional(readOnly = false)
	public void save(Button button) {
		super.save(button);
	}
	
	@Transactional(readOnly = false)
	public void delete(Button button) {
		super.delete(button);
	}
	
}