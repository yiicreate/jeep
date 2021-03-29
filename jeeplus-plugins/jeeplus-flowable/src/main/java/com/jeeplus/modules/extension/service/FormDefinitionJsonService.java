/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.extension.entity.FormDefinitionJson;
import com.jeeplus.modules.extension.mapper.FormDefinitionJsonMapper;

/**
 * 流程表单Service
 * @author 刘高峰
 * @version 2020-02-02
 */
@Service
@Transactional(readOnly = true)
public class FormDefinitionJsonService extends CrudService<FormDefinitionJsonMapper, FormDefinitionJson> {

	public FormDefinitionJson get(String id) {
		return super.get(id);
	}

	public List<FormDefinitionJson> findList(FormDefinitionJson formDefinitionJson) {
		return super.findList(formDefinitionJson);
	}

	public Page<FormDefinitionJson> findPage(Page<FormDefinitionJson> page, FormDefinitionJson formDefinitionJson) {
		return super.findPage(page, formDefinitionJson);
	}

	@Transactional(readOnly = true)
	public Integer getMaxVersion(FormDefinitionJson formDefinitionJson) {
		return mapper.getMaxVersion(formDefinitionJson);
	}

	@Transactional(readOnly = false)
	public void save(FormDefinitionJson formDefinitionJson) {
		super.save(formDefinitionJson);
	}

	@Transactional(readOnly = false)
	public void delete(FormDefinitionJson formDefinitionJson) {
		super.delete(formDefinitionJson);
	}

	@Transactional(readOnly = false)
	public void deleteByFormDefinitionId(FormDefinitionJson formDefinitionJson) {
		mapper.deleteByFormDefinitionId(formDefinitionJson);
	}

	@Transactional(readOnly = false)
	public void updatePrimary(FormDefinitionJson formDefinitionJson) {
		mapper.updatePrimary(formDefinitionJson);
	}

}
