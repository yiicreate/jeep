/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.activiti.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.test.activiti.entity.TestActivitiLeave;
import com.jeeplus.modules.test.activiti.service.TestActivitiLeaveService;

/**
 * 请假申请Controller
 * @author liugf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/activiti/testActivitiLeave")
public class TestActivitiLeaveController extends BaseController {

	@Autowired
	private TestActivitiLeaveService testActivitiLeaveService;

	@ModelAttribute
	public TestActivitiLeave get(@RequestParam(required=false) String id) {
		TestActivitiLeave entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testActivitiLeaveService.get(id);
		}
		if (entity == null){
			entity = new TestActivitiLeave();
		}
		return entity;
	}

	/**
	 * 请假申请列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(TestActivitiLeave testActivitiLeave, HttpServletRequest request, HttpServletResponse response) {
		Page<TestActivitiLeave> page = testActivitiLeaveService.findPage(new Page<TestActivitiLeave>(request, response), testActivitiLeave);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取请假申请数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(TestActivitiLeave testActivitiLeave) {
		return AjaxJson.success().put("testActivitiLeave", testActivitiLeave);
	}

	/**
	 * 保存请假申请
	 */
	@PostMapping("save")
	public AjaxJson save(TestActivitiLeave testActivitiLeave, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testActivitiLeave);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑保存
		testActivitiLeaveService.save(testActivitiLeave);//保存
		return AjaxJson.success("保存请假申请成功").put("businessTable", "test_activiti_leave").put("businessId", testActivitiLeave.getId());
	}


	/**
	 * 批量删除请假申请
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testActivitiLeaveService.delete(new TestActivitiLeave(id));
		}
		return AjaxJson.success("删除请假申请成功");
	}


}