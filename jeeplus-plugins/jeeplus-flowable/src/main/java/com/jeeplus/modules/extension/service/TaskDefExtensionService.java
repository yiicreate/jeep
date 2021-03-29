/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.extension.entity.TaskDefExtension;
import com.jeeplus.modules.extension.mapper.TaskDefExtensionMapper;
import com.jeeplus.modules.extension.entity.FlowAssignee;
import com.jeeplus.modules.extension.mapper.FlowAssigneeMapper;
import com.jeeplus.modules.extension.entity.FlowButton;
import com.jeeplus.modules.extension.mapper.FlowButtonMapper;
import com.jeeplus.modules.extension.entity.FlowCondition;
import com.jeeplus.modules.extension.mapper.FlowConditionMapper;

/**
 * 工作流扩展Service
 * @author 刘高峰
 * @version 2020-12-23
 */
@Service
@Transactional(readOnly = true)
public class TaskDefExtensionService extends CrudService<TaskDefExtensionMapper, TaskDefExtension> {

	@Autowired
	private FlowAssigneeMapper flowAssigneeMapper;
	@Autowired
	private FlowButtonMapper flowButtonMapper;
	@Autowired
	private FlowConditionMapper flowConditionMapper;

	public TaskDefExtension get(String id) {
		TaskDefExtension taskDefExtension = super.get(id);
		taskDefExtension.setFlowAssigneeList(flowAssigneeMapper.findList(new FlowAssignee(taskDefExtension)));
		taskDefExtension.setFlowButtonList(flowButtonMapper.findList(new FlowButton(taskDefExtension)));
		taskDefExtension.setFlowConditionList(flowConditionMapper.findList(new FlowCondition(taskDefExtension)));
		return taskDefExtension;
	}

	public List<TaskDefExtension> findList(TaskDefExtension taskDefExtension) {
		return super.findList(taskDefExtension);
	}

	public Page<TaskDefExtension> findPage(Page<TaskDefExtension> page, TaskDefExtension taskDefExtension) {
		return super.findPage(page, taskDefExtension);
	}

	@Transactional(readOnly = false)
	public void save(TaskDefExtension taskDefExtension) {
		super.save(taskDefExtension);
		for (FlowAssignee flowAssignee : taskDefExtension.getFlowAssigneeList()){
			if (flowAssignee.getId() == null){
				continue;
			}
			if (FlowAssignee.DEL_FLAG_NORMAL.equals(flowAssignee.getDelFlag())){
				if (StringUtils.isBlank(flowAssignee.getId())){
					flowAssignee.setTaskDef(taskDefExtension);
					flowAssignee.preInsert();
					flowAssigneeMapper.insert(flowAssignee);
				}else{
					flowAssignee.preUpdate();
					flowAssigneeMapper.update(flowAssignee);
				}
			}else{
				flowAssigneeMapper.delete(flowAssignee);
			}
		}
		for (FlowButton flowButton : taskDefExtension.getFlowButtonList()){
			if (flowButton.getId() == null){
				continue;
			}
			if (FlowButton.DEL_FLAG_NORMAL.equals(flowButton.getDelFlag())){
				if (StringUtils.isBlank(flowButton.getId())){
					flowButton.setTaskDef(taskDefExtension);
					flowButton.preInsert();
					flowButtonMapper.insert(flowButton);
				}else{
					flowButton.preUpdate();
					flowButtonMapper.update(flowButton);
				}
			}else{
				flowButtonMapper.delete(flowButton);
			}
		}
		for (FlowCondition flowCondition : taskDefExtension.getFlowConditionList()){
			if (flowCondition.getId() == null){
				continue;
			}
			if (FlowCondition.DEL_FLAG_NORMAL.equals(flowCondition.getDelFlag())){
				if (StringUtils.isBlank(flowCondition.getId())){
					flowCondition.setTaskDef(taskDefExtension);
					flowCondition.preInsert();
					flowConditionMapper.insert(flowCondition);
				}else{
					flowCondition.preUpdate();
					flowConditionMapper.update(flowCondition);
				}
			}else{
				flowConditionMapper.delete(flowCondition);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(TaskDefExtension taskDefExtension) {
		super.delete(taskDefExtension);
		flowAssigneeMapper.delete(new FlowAssignee(taskDefExtension));
		flowButtonMapper.delete(new FlowButton(taskDefExtension));
		flowConditionMapper.delete(new FlowCondition(taskDefExtension));
	}

	@Transactional(readOnly = false)
	public void deleteByProcessDefId(String processDefId) {
		TaskDefExtension taskDefExtension = new TaskDefExtension();
		taskDefExtension.setProcessDefId(processDefId);
		List<TaskDefExtension> list = mapper.findList(taskDefExtension);
		for(TaskDefExtension taskDefExtension1: list){
			super.delete(taskDefExtension1);
			flowAssigneeMapper.delete(new FlowAssignee(taskDefExtension1));
			flowButtonMapper.delete(new FlowButton(taskDefExtension1));
			flowConditionMapper.delete(new FlowCondition(taskDefExtension1));

		}

	}

}
