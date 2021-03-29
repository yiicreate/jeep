/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.extension.entity.FlowCopy;
import com.jeeplus.modules.extension.mapper.FlowCopyMapper;

/**
 * 流程抄送Service
 * @author 刘高峰
 * @version 2019-10-10
 */
@Service
@Transactional(readOnly = true)
public class FlowCopyService extends CrudService<FlowCopyMapper, FlowCopy> {

	public FlowCopy get(String id) {
		return super.get(id);
	}

	public List<FlowCopy> findList(FlowCopy flowCopy) {
		return super.findList(flowCopy);
	}

	public Page<FlowCopy> findPage(Page<FlowCopy> page, FlowCopy flowCopy) {
		return super.findPage(page, flowCopy);
	}

	@Transactional(readOnly = false)
	public void save(FlowCopy flowCopy) {
		super.save(flowCopy);
	}

	@Transactional(readOnly = false)
	public void delete(FlowCopy flowCopy) {
		super.delete(flowCopy);
	}

	@Transactional(readOnly = false)
	public void deleteByProcInsId(String procInsId) {
		mapper.deleteByProcInsId(procInsId);
	}



}
