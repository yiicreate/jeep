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
import com.jeeplus.modules.extension.entity.Condition;
import com.jeeplus.modules.extension.service.ConditionService;

/**
 * 流程表达式Controller
 * @author liugf
 * @version 2019-09-29
 */
@RestController
@RequestMapping("/extension/condition")
public class ConditionController extends BaseController {

	@Autowired
	private ConditionService conditionService;

	@ModelAttribute
	public Condition get(@RequestParam(required=false) String id) {
		Condition entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = conditionService.get(id);
		}
		if (entity == null){
			entity = new Condition();
		}
		return entity;
	}

	/**
	 * 流程表达式列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(Condition condition, HttpServletRequest request, HttpServletResponse response) {
		Page<Condition> page = conditionService.findPage(new Page<Condition>(request, response), condition);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 查看，增加，编辑流程表达式表单页面
	 * params:
	 * 	mode: add, edit, view, 代表三种模式的页面
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(Condition condition) {
		return AjaxJson.success().put("condition", condition);
	}

	/**
	 * 保存流程表达式
	 */
	@PostMapping("save")
	public AjaxJson save(Condition condition, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(condition);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		conditionService.save(condition);//保存
		return AjaxJson.success("保存流程表达式成功");
	}


	/**
	 * 批量删除流程表达式
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			conditionService.delete(conditionService.get(id));
		}
		return AjaxJson.success("删除流程表达式成功");
	}


}
