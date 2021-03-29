/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.extension.entity.NodeSetting;
import com.jeeplus.modules.extension.mapper.NodeSettingMapper;

/**
 * 节点设置Service
 * @author 刘高峰
 * @version 2021-01-11
 */
@Service
@Transactional(readOnly = true)
public class NodeSettingService extends CrudService<NodeSettingMapper, NodeSetting> {

	public NodeSetting get(String id) {
		return super.get(id);
	}

	public List<NodeSetting> findList(NodeSetting nodeSetting) {
		return super.findList(nodeSetting);
	}

	public Page<NodeSetting> findPage(Page<NodeSetting> page, NodeSetting nodeSetting) {
		return super.findPage(page, nodeSetting);
	}

	@Transactional(readOnly = false)
	public void save(NodeSetting nodeSetting) {
		super.save(nodeSetting);
	}

	@Transactional(readOnly = false)
	public void delete(NodeSetting nodeSetting) {
		super.delete(nodeSetting);
	}

	@Transactional(readOnly = false)
	public NodeSetting queryByKey(String processDefId, String taskDefId, String key) {
		return mapper.queryByKey (processDefId, taskDefId, key);
	}
	@Transactional(readOnly = false)
	public void deleteByDefIdAndTaskId(NodeSetting nodeSetting){
		mapper.deleteByDefIdAndTaskId(nodeSetting);
	}
	@Transactional(readOnly = false)
	public void deleteByProcessDefId(String processDefId){
		NodeSetting nodeSetting = new NodeSetting ();
		nodeSetting.setProcessDefId(processDefId);
		mapper.deleteByProcessDefId(nodeSetting);
	}

}
