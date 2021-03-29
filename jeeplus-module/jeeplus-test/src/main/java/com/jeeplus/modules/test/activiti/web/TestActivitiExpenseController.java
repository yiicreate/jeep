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
import com.jeeplus.modules.test.activiti.entity.TestActivitiExpense;
import com.jeeplus.modules.test.activiti.service.TestActivitiExpenseService;

/**
 * 报销申请Controller
 * @author liugf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/activiti/testActivitiExpense")
public class TestActivitiExpenseController extends BaseController {

	@Autowired
	private TestActivitiExpenseService testActivitiExpenseService;

	@ModelAttribute
	public TestActivitiExpense get(@RequestParam(required=false) String id) {
		TestActivitiExpense entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testActivitiExpenseService.get(id);
		}
		if (entity == null){
			entity = new TestActivitiExpense();
		}
		return entity;
	}

	/**
	 * 报销申请列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(TestActivitiExpense testActivitiExpense, HttpServletRequest request, HttpServletResponse response) {
		Page<TestActivitiExpense> page = testActivitiExpenseService.findPage(new Page<TestActivitiExpense>(request, response), testActivitiExpense);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取报销申请数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(TestActivitiExpense testActivitiExpense) {
		return AjaxJson.success().put("testActivitiExpense", testActivitiExpense);
	}

	/**
	 * 保存报销申请
	 */
	@PostMapping("save")
	public AjaxJson save(TestActivitiExpense testActivitiExpense, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testActivitiExpense);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑保存
		testActivitiExpenseService.save(testActivitiExpense);//保存
		return AjaxJson.success("保存报销申请成功").put("businessTable", "test_activiti_expense").put("businessId", testActivitiExpense.getId());
	}


	/**
	 * 批量删除报销申请
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testActivitiExpenseService.delete(new TestActivitiExpense(id));
		}
		return AjaxJson.success("删除报销申请成功");
	}


}