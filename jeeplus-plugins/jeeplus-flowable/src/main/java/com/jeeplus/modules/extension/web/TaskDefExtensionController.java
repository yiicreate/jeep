/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.extension.entity.TaskDefExtension;
import com.jeeplus.modules.extension.service.TaskDefExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工作流扩展Controller
 * @author 刘高峰
 * @version 2020-12-23
 */
@RestController
@RequestMapping(value = "/extension/taskDefExtension")
public class TaskDefExtensionController extends BaseController {

	@Autowired
	private TaskDefExtensionService taskDefExtensionService;

	@ModelAttribute
	public TaskDefExtension get(@RequestParam(required=false) String id) {
		TaskDefExtension entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = taskDefExtensionService.get(id);
		}
		if (entity == null){
			entity = new TaskDefExtension();
		}
		return entity;
	}

	/**
	 * 工作流扩展列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(TaskDefExtension taskDefExtension, HttpServletRequest request, HttpServletResponse response) {
		Page<TaskDefExtension> page = taskDefExtensionService.findPage(new Page<TaskDefExtension>(request, response), taskDefExtension);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取工作流扩展数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(TaskDefExtension taskDefExtension) {
		return AjaxJson.success().put("taskDefExtension", taskDefExtension);
	}

	@GetMapping("queryByDefIdAndTaskId")
	public AjaxJson queryByDefIdAndTaskId(TaskDefExtension taskDefExtension) throws Exception {
		if(StringUtils.isBlank(taskDefExtension.getProcessDefId()) || StringUtils.isBlank(taskDefExtension.getTaskDefId())){
			return AjaxJson.success().put("taskDefExtension", null);
		}
		List<TaskDefExtension> list = taskDefExtensionService.findList(taskDefExtension);
		if(list.size() > 1){
			throw new Exception("重复的task id定义!");
		}else if(list.size() == 1){
			String id = list.get(0).getId();
			return AjaxJson.success().put("taskDefExtension", taskDefExtensionService.get(id));
		}else {
			return AjaxJson.success().put("taskDefExtension", taskDefExtension);
		}

	}

	/**
	 * 保存工作流扩展
	 */
	@PostMapping("save")
	public AjaxJson save(@RequestBody List<TaskDefExtension> taskDefExtensionList, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		for(TaskDefExtension taskDefExtension: taskDefExtensionList){
			List<TaskDefExtension> list = taskDefExtensionService.findList(taskDefExtension);
			for(TaskDefExtension defExtension:list){
				taskDefExtensionService.delete(defExtension);
			}
			//新增或编辑表单保存
			taskDefExtensionService.save(taskDefExtension);//保存
		}
		return AjaxJson.success("保存工作流扩展成功");
	}


	/**
	 * 批量删除工作流扩展
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			taskDefExtensionService.delete(new TaskDefExtension(id));
		}
		return AjaxJson.success("删除工作流扩展成功");
	}

}
