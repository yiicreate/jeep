/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.shop.web;

import java.util.List;

import org.apache.shiro.authz.annotation.Logical;
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
import com.jeeplus.modules.test.shop.entity.Category;
import com.jeeplus.modules.test.shop.service.CategoryService;

/**
 * 商品类型Controller
 * @author liugf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/shop/category")
public class CategoryController extends BaseController {

	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	public Category get(@RequestParam(required=false) String id) {
		Category entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = categoryService.get(id);
		}
		if (entity == null){
			entity = new Category();
		}
		return entity;
	}


	/**
	 * 商品类型树表数据
	 */
	@RequiresPermissions("test:shop:category:list")
	@GetMapping("list")
	public AjaxJson list(Category category) {
		return AjaxJson.success().put("list", categoryService.findList(category));
	}

	/**
	 * 根据Id获取商品类型数据
	 */
	@RequiresPermissions(value={"test:shop:category:view","test:shop:category:add","test:shop:category:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(Category category) {
		return AjaxJson.success().put("category", category);
	}

	/**
	 * 保存商品类型
	 */
	@RequiresPermissions(value={"test:shop:category:add","test:shop:category:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(Category category, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(category);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		categoryService.save(category);//保存
		return AjaxJson.success("保存商品类型成功");
	}

	/**
	 * 删除商品类型
	 */
	@RequiresPermissions("test:shop:category:del")
	@DeleteMapping("delete")
	public AjaxJson delete(Category category) {
		categoryService.delete(category);
		return AjaxJson.success("删除商品类型成功");
	}

	/**
     * 获取JSON树形数据。
     * @param extId 排除的ID
     * @return
	*/
	@RequiresPermissions("user")
	@GetMapping("treeData")
	public AjaxJson treeData(@RequestParam(required = false) String extId) {
		List<Category> list = categoryService.findList(new Category());
		List rootTree =  categoryService.formatListToTree (new Category ("0"),list, extId );
		return AjaxJson.success().put("treeData", rootTree);
	}

}