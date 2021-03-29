/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web.app;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.extension.entity.TaskDefExtension;
import com.jeeplus.modules.extension.service.TaskDefExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工作流扩展Controller
 * @author liugf
 * @version 2019-09-26
 */
@RestController
@RequestMapping("/app/extension/taskDefExtension")
public class AppTaskDefExtensionController extends BaseController {

	@Autowired
	private TaskDefExtensionService taskDefExtensionService;

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


}
