/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.extension.entity.FlowCopy;
import com.jeeplus.modules.extension.service.FlowCopyService;

/**
 * 流程抄送Controller
 * @author 刘高峰
 * @version 2019-10-10
 */
@RestController
@RequestMapping("/extension/flowCopy")
public class FlowCopyController extends BaseController {

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
	 * 查看，增加，编辑流程抄送表单页面
	 * params:
	 * 	mode: add, edit, view, 代表三种模式的页面
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(FlowCopy flowCopy) {
		return AjaxJson.success().put("flowCopy", flowCopy);
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
