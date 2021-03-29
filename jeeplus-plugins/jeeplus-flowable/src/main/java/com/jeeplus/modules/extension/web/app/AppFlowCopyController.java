/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web.app;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.extension.entity.FlowCopy;
import com.jeeplus.modules.extension.service.FlowCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 流程抄送Controller
 * @author 刘高峰
 * @version 2019-10-10
 */
@RestController
@RequestMapping("/app/extension/flowCopy")
public class AppFlowCopyController extends BaseController {

	@Autowired
	private FlowCopyService flowCopyService;

	@ModelAttribute
	public FlowCopy get(@RequestParam(required=false) String id) {
		FlowCopy entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = flowCopyService.get(id);
		}
		if (entity == null){
			entity = new FlowCopy();
		}
		return entity;
	}

	@GetMapping("list")
	public AjaxJson list(FlowCopy flowCopy, HttpServletRequest request, HttpServletResponse response) {
		Page<FlowCopy> page = flowCopyService.findPage(new Page<FlowCopy>(request, response), flowCopy);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 保存流程抄送
	 */
	@PostMapping("save")
	public AjaxJson save(@RequestParam("userIds") String userIds, FlowCopy flowCopy, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(flowCopy);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		for(String userId: userIds.split(",")){
			flowCopy.setId(null);
			flowCopy.setUserId(userId);
			flowCopyService.save(flowCopy);//保存
		}
		return AjaxJson.success("保存流程抄送成功");
	}


	/**
	 * 批量删除流程抄送
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			flowCopyService.delete(flowCopyService.get(id));
		}
		return AjaxJson.success("删除流程抄送成功");
	}

}
