/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.treetable.web;

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
import com.jeeplus.modules.test.treetable.entity.CarKind;
import com.jeeplus.modules.test.treetable.service.CarKindService;

/**
 * 车系Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/treetable/carKind")
public class CarKindController extends BaseController {

	@Autowired
	private CarKindService carKindService;

	@ModelAttribute
	public CarKind get(@RequestParam(required=false) String id) {
		CarKind entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = carKindService.get(id);
		}
		if (entity == null){
			entity = new CarKind();
		}
		return entity;
	}


	/**
	 * 车系树表数据
	 */
	@GetMapping("list")
	public AjaxJson list(CarKind carKind) {
		return AjaxJson.success().put("list", carKindService.findList(carKind));
	}

	/**
	 * 根据Id获取车系数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(CarKind carKind) {
		return AjaxJson.success().put("carKind", carKind);
	}

	/**
	 * 保存车系
	 */
	@PostMapping("save")
	public AjaxJson save(CarKind carKind, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(carKind);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		carKindService.save(carKind);//保存
		return AjaxJson.success("保存车系成功");
	}

	/**
	 * 删除车系
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(CarKind carKind) {
		carKindService.delete(carKind);
		return AjaxJson.success("删除车系成功");
	}

	/**
	     * 获取JSON树形数据。
	     * @param extId 排除的ID
	     * @return
	*/
	@RequiresPermissions("user")
	@GetMapping("treeData")
	public AjaxJson treeData(@RequestParam(required = false) String extId) {
		List<CarKind> list = carKindService.findList(new CarKind());
		List rootTree =  carKindService.formatListToTree (new CarKind ("0"),list, extId );
		return AjaxJson.success().put("treeData", rootTree);
	}



}