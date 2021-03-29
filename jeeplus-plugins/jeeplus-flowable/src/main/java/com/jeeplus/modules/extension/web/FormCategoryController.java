/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.extension.entity.FormCategory;
import com.jeeplus.modules.extension.service.FormCategoryService;

/**
 * 流程分类Controller
 * @author 刘高峰
 * @version 2020-02-02
 */
@RestController
@RequestMapping(value = "/extension/formCategory")
public class FormCategoryController extends BaseController {

	@Autowired
	private FormCategoryService formCategoryService;

	@ModelAttribute
	public FormCategory get(@RequestParam(required=false) String id) {
		FormCategory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = formCategoryService.get(id);
		}
		if (entity == null){
			entity = new FormCategory();
		}
		return entity;
	}


	/**
	 * 流程分类树表数据
	 */
	@GetMapping("list")
	public AjaxJson list(FormCategory formCategory) {
		return AjaxJson.success().put("list", formCategoryService.findList(formCategory));
	}

	/**
	 * 根据Id获取流程分类数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(FormCategory formCategory) {
		return AjaxJson.success().put("formCategory", formCategory);
	}

	/**
	 * 保存流程分类
	 */
	@PostMapping("save")
	public AjaxJson save(FormCategory formCategory, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(formCategory);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		formCategoryService.save(formCategory);//保存
		return AjaxJson.success("保存流程分类成功");
	}

	/**
	 * 删除流程分类
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(FormCategory formCategory) {
		formCategoryService.delete(formCategory);
		return AjaxJson.success("删除流程分类成功");
	}

	/**
	     * 获取JSON树形数据。
	     * @param extId 排除的ID
	     * @return
	*/
	@RequiresPermissions("user")
	@GetMapping("treeData")
	public AjaxJson treeData(@RequestParam(required = false) String extId) {
		List<FormCategory> list = formCategoryService.findList(new FormCategory());
		List rootTree = getFormCategoryTree(list, extId);
		return AjaxJson.success().put("treeData", rootTree);
	}

	private List<FormCategory> getFormCategoryTree(List<FormCategory> list, String extId) {
		List<FormCategory> formCategorys = Lists.newArrayList();
		List<FormCategory> rootTrees = formCategoryService.getChildren("0");
		for (FormCategory root : rootTrees) {
		    if (StringUtils.isBlank(extId) ||  !extId.equals(root.getId())) {
		        formCategorys.add(getChildOfTree(root, list, extId));
		    }
		}
		return formCategorys;
	}

	private FormCategory getChildOfTree(FormCategory formCategory, List<FormCategory> formCategoryList, String extId) {
		formCategory.setChildren(Lists.newArrayList());
		for (FormCategory child : formCategoryList) {
		    if (StringUtils.isBlank(extId) ||  (!extId.equals(child.getId()) && child.getParentIds().indexOf("," + extId + ",") == -1)) {
		        if (child.getParentId().equals(formCategory.getId())) {
		            formCategory.getChildren().add(getChildOfTree(child, formCategoryList, extId));
		        }
		    }
		}
		return formCategory;
	}



}
