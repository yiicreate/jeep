/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web.app;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.extension.entity.ActCategory;
import com.jeeplus.modules.extension.service.ActCategoryService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程分类Controller
 * @author 刘高峰
 * @version 2019-10-09
 */
@RestController
@RequestMapping("/app/extension/actCategory")
public class AppActCategoryController extends BaseController {

	@Autowired
	private ActCategoryService actCategoryService;

	@ModelAttribute
	public ActCategory get(@RequestParam(required=false) String id) {
		ActCategory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = actCategoryService.get(id);
		}
		if (entity == null){
			entity = new ActCategory();
		}
		return entity;
	}


	/**
	 * 流程分类树表数据
	 */
	@RequiresPermissions("extension:actCategory:list")
	@GetMapping("list")
	public AjaxJson list(ActCategory actCategory) {
		return AjaxJson.success().put("list", actCategoryService.findList(actCategory));
	}

	/**
	 * 查看，增加，编辑流程分类表单页面
	 * params:
	 * 	mode: add, edit, view, 代表三种模式的页面
	 */
	@RequiresPermissions(value={"extension:actCategory:view","extension:actCategory:add","extension:actCategory:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(ActCategory actCategory) {
		return AjaxJson.success().put("actCategory", actCategory);
	}

	/**
	 * 保存流程分类
	 */
	@RequiresPermissions(value={"extension:actCategory:add","extension:actCategory:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(ActCategory actCategory, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(actCategory);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		actCategoryService.save(actCategory);//保存
		return AjaxJson.success("保存流程分类成功");
	}

	/**
	 * 删除流程分类
	 */
	@RequiresPermissions("extension:actCategory:del")
	@DeleteMapping("delete")
	public AjaxJson delete(ActCategory actCategory) {
		actCategoryService.delete(actCategory);
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
		List<ActCategory> list = actCategoryService.findList(new ActCategory());
		List rootTree = getActCategoryTree(list, extId);
		return AjaxJson.success().put("treeData", rootTree);
	}

	private List<ActCategory> getActCategoryTree(List<ActCategory> list, String extId) {
		List<ActCategory> actCategorys = Lists.newArrayList();
		List<ActCategory> rootTrees = actCategoryService.getChildren("0");
		for (ActCategory root : rootTrees) {
		    if (StringUtils.isBlank(extId) ||  !extId.equals(root.getId())) {
		        actCategorys.add(getChildOfTree(root, list, extId));
		    }
		}
		return actCategorys;
	}

	private ActCategory getChildOfTree(ActCategory actCategory, List<ActCategory> actCategoryList, String extId) {
		actCategory.setChildren(Lists.newArrayList());
		for (ActCategory child : actCategoryList) {
		    if (StringUtils.isBlank(extId) ||  (!extId.equals(child.getId()) && child.getParentIds().indexOf("," + extId + ",") == -1)) {
		        if (child.getParentId().equals(actCategory.getId())) {
		            actCategory.getChildren().add(getChildOfTree(child, actCategoryList, extId));
		        }
		    }
		}
		return actCategory;
	}



}
