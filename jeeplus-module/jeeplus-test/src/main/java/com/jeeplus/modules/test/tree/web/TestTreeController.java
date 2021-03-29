/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.tree.web;

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
import com.jeeplus.modules.test.tree.entity.TestTree;
import com.jeeplus.modules.test.tree.service.TestTreeService;

/**
 * 组织机构Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/tree/testTree")
public class TestTreeController extends BaseController {

	@Autowired
	private TestTreeService testTreeService;

	@ModelAttribute
	public TestTree get(@RequestParam(required=false) String id) {
		TestTree entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testTreeService.get(id);
		}
		if (entity == null){
			entity = new TestTree();
		}
		return entity;
	}


	/**
	 * 组织机构树表数据
	 */
	@RequiresPermissions("test:tree:testTree:list")
	@GetMapping("list")
	public AjaxJson list(TestTree testTree) {
		return AjaxJson.success().put("list", testTreeService.findList(testTree));
	}

	/**
	 * 根据Id获取组织机构数据
	 */
	@RequiresPermissions(value={"test:tree:testTree:view","test:tree:testTree:add","test:tree:testTree:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(TestTree testTree) {
		return AjaxJson.success().put("testTree", testTree);
	}

	/**
	 * 保存组织机构
	 */
	@RequiresPermissions(value={"test:tree:testTree:add","test:tree:testTree:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(TestTree testTree, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testTree);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		testTreeService.save(testTree);//保存
		return AjaxJson.success("保存组织机构成功");
	}

	/**
	 * 删除组织机构
	 */
	@RequiresPermissions("test:tree:testTree:del")
	@DeleteMapping("delete")
	public AjaxJson delete(TestTree testTree) {
		testTreeService.delete(testTree);
		return AjaxJson.success("删除组织机构成功");
	}

	/**
     * 获取JSON树形数据。
     * @param extId 排除的ID
     * @return
	*/
	@RequiresPermissions("user")
	@GetMapping("treeData")
	public AjaxJson treeData(@RequestParam(required = false) String extId) {
		List<TestTree> list = testTreeService.findList(new TestTree());
		List rootTree =  testTreeService.formatListToTree (new TestTree ("0"),list, extId );
		return AjaxJson.success().put("treeData", rootTree);
	}

}