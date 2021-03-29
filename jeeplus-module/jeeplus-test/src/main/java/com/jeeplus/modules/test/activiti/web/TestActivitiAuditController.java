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
import com.jeeplus.modules.test.activiti.entity.TestActivitiAudit;
import com.jeeplus.modules.test.activiti.service.TestActivitiAuditService;

/**
 * 薪酬调整申请Controller
 * @author liugf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/activiti/testActivitiAudit")
public class TestActivitiAuditController extends BaseController {

	@Autowired
	private TestActivitiAuditService testActivitiAuditService;

	@ModelAttribute
	public TestActivitiAudit get(@RequestParam(required=false) String id) {
		TestActivitiAudit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testActivitiAuditService.get(id);
		}
		if (entity == null){
			entity = new TestActivitiAudit();
		}
		return entity;
	}

	/**
	 * 薪酬调整申请列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(TestActivitiAudit testActivitiAudit, HttpServletRequest request, HttpServletResponse response) {
		Page<TestActivitiAudit> page = testActivitiAuditService.findPage(new Page<TestActivitiAudit>(request, response), testActivitiAudit);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取薪酬调整申请数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(TestActivitiAudit testActivitiAudit) {
		return AjaxJson.success().put("testActivitiAudit", testActivitiAudit);
	}

	/**
	 * 保存薪酬调整申请
	 */
	@PostMapping("save")
	public AjaxJson save(TestActivitiAudit testActivitiAudit, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testActivitiAudit);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑保存
		testActivitiAuditService.save(testActivitiAudit);//保存
		return AjaxJson.success("保存薪酬调整申请成功").put("businessTable", "test_activiti_audit").put("businessId", testActivitiAudit.getId());
	}


	/**
	 * 批量删除薪酬调整申请
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testActivitiAuditService.delete(new TestActivitiAudit(id));
		}
		return AjaxJson.success("删除薪酬调整申请成功");
	}


}