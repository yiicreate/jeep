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
import com.jeeplus.modules.extension.entity.Button;
import com.jeeplus.modules.extension.service.ButtonService;

/**
 * 常用按钮Controller
 * @author 刘高峰
 * @version 2019-10-07
 */
@RestController
@RequestMapping("/extension/button")
public class ButtonController extends BaseController {

	@Autowired
	private ButtonService buttonService;

	@ModelAttribute
	public Button get(@RequestParam(required=false) String id) {
		Button entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = buttonService.get(id);
		}
		if (entity == null){
			entity = new Button();
		}
		return entity;
	}

	/**
	 * 常用按钮列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(Button button, HttpServletRequest request, HttpServletResponse response) {
		Page<Button> page = buttonService.findPage(new Page<Button>(request, response), button);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 查看，增加，编辑常用按钮表单页面
	 * params:
	 * 	mode: add, edit, view, 代表三种模式的页面
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(Button button) {
		return AjaxJson.success().put("button", button);
	}

	/**
	 * 保存常用按钮
	 */
	@PostMapping("save")
	public AjaxJson save(Button button, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(button);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		buttonService.save(button);//保存
		return AjaxJson.success("保存常用按钮成功");
	}


	/**
	 * 批量删除常用按钮
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			buttonService.delete(buttonService.get(id));
		}
		return AjaxJson.success("删除常用按钮成功");
	}


}
