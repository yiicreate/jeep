/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import com.jeeplus.modules.extension.entity.FormDefinitionJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.extension.entity.FormDefinition;
import com.jeeplus.modules.extension.mapper.FormDefinitionMapper;

/**
 * 流程表单Service
 * @author 刘高峰
 * @version 2020-02-02
 */
@Service
@Transactional(readOnly = true)
public class FormDefinitionService extends CrudService<FormDefinitionMapper, FormDefinition> {

	@Autowired
	private FormDefinitionJsonService formDefinitionJsonService;


	public FormDefinition get(String id) {
		FormDefinition formDefinition = super.get(id);
		return formDefinition;
	}

	public List<FormDefinition> findList(FormDefinition formDefinition) {
		return super.findList(formDefinition);
	}

	public Page<FormDefinition> findPage(Page<FormDefinition> page, FormDefinition formDefinition) {
		return super.findPage(page, formDefinition);
	}

	@Transactional(readOnly = false)
	public void save(FormDefinition formDefinition) {
		super.save(formDefinition);
	}


	@Transactional(readOnly = false)
	public void delete(FormDefinition formDefinition) {
		super.delete(formDefinition);
		FormDefinitionJson formDefinitionJson = new FormDefinitionJson();
		formDefinitionJson.setFormDefinitionId(formDefinition.getId());
		formDefinitionJsonService.deleteByFormDefinitionId(formDefinitionJson);
	}

}
