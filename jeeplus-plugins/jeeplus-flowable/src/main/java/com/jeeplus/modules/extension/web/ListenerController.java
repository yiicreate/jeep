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
import com.jeeplus.modules.extension.entity.Listener;
import com.jeeplus.modules.extension.service.ListenerService;

/**
 * 监听器Controller
 * @author 刘高峰
 * @version 2019-10-14
 */
@RestController
@RequestMapping("/extension/listener")
public class ListenerController extends BaseController {

	@Autowired
	private ListenerService listenerService;

	@ModelAttribute
	public Listener get(@RequestParam(required=false) String id) {
		Listener entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = listenerService.get(id);
		}
		if (entity == null){
			entity = new Listener();
		}
		return entity;
	}

	/**
	 * 监听器列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(Listener listener, HttpServletRequest request, HttpServletResponse response) {
		Page<Listener> page = listenerService.findPage(new Page<Listener>(request, response), listener);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 查看，增加，编辑监听器表单页面
	 * params:
	 * 	mode: add, edit, view, 代表三种模式的页面
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(Listener listener) {
		return AjaxJson.success().put("listener", listener);
	}

	/**
	 * 保存监听器
	 */
	@PostMapping("save")
	public AjaxJson save(Listener listener, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(listener);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		listenerService.save(listener);//保存
		return AjaxJson.success("保存监听器成功");
	}


	/**
	 * 批量删除监听器
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			listenerService.delete(listenerService.get(id));
		}
		return AjaxJson.success("删除监听器成功");
	}

}
