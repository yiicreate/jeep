/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.DictType;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.service.DictTypeService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.MenuUtils;
import com.jeeplus.modules.sys.utils.RouterUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典Controller
 * @author jeeplus
 * @version 2017-05-16
 */
@RestController
@RequestMapping("/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictTypeService dictTypeService;

	@ModelAttribute
	public DictType get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dictTypeService.get(id);
		}else{
			return new DictType();
		}
	}

	@RequiresPermissions("sys:dict:list")
	@GetMapping("getDictValue")
	public AjaxJson getDictValue(String dictTypeId) {
		Map<String, Object> page = new HashMap<String, Object>();
		if(dictTypeId == null || "".equals(dictTypeId)){
			page.put("list","[]");
			page.put("count",0);
		}else{
			List<DictValue> list = dictTypeService.get(dictTypeId).getDictValueList();
			page.put("list",list);
			page.put("count", list.size());
		}
		return AjaxJson.success().put("page", page);
	}

	@RequiresPermissions("sys:dict:list")
	@GetMapping("type/list")
	public AjaxJson data(DictType dictType, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DictType> page = dictTypeService.findPage(new Page<DictType>(request, response), dictType);
		return AjaxJson.success().put("page",page);
	}


	@RequiresPermissions(value={"sys:dict:view","sys:dict:add","sys:dict:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(DictType dictType) {
		return AjaxJson.success().put("dictType", dictType);
	}

	@RequiresPermissions(value={"sys:dict:view","sys:dict:add","sys:dict:edit"},logical=Logical.OR)
	@GetMapping("queryDictValue")
	public AjaxJson queryDictValue(String dictValueId) {
		DictValue dictValue;
		if(dictValueId == null || "".equals(dictValueId)){
			dictValue =  new DictValue();
		}else{
			dictValue = dictTypeService.getDictValue(dictValueId);
		}
		return AjaxJson.success().put("dictValue", dictValue);
	}


	@RequiresPermissions(value={"sys:dict:add","sys:dict:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(DictType dictType, Model model) {
		if(jeePlusProperites.isDemoMode()){
			return AjaxJson.error("演示模式，不允许操作！");
		}
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(dictType);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		dictTypeService.save(dictType);
		return AjaxJson.success("保存字典类型'" + dictType.getDescription() + "'成功！");
	}

	@RequiresPermissions(value={"sys:dict:add","sys:dict:edit"},logical=Logical.OR)
	@PostMapping("saveDictValue")
	public AjaxJson saveDictValue(String dictValueId, DictValue dictValue, Model model) {
		if(jeePlusProperites.isDemoMode()){
			return AjaxJson.error("演示模式，不允许操作！");
		}
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(dictValue);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		dictValue.setId(dictValueId);
		dictTypeService.saveDictValue(dictValue);
		return AjaxJson.success("保存键值'" + dictValue.getLabel() + "'成功！");
	}


	@RequiresPermissions("sys:dict:edit")
	@DeleteMapping("deleteDictValue")
	public AjaxJson deleteDictValue(String ids, Model model) {
		AjaxJson j = new AjaxJson();
		if(jeePlusProperites.isDemoMode()){
			return AjaxJson.error("演示模式，不允许操作！");
		}
		dictTypeService.batchDeleteDictValue(ids.split(","));
		return AjaxJson.success("删除键值成功！");
	}

	/**
	 * 批量删除
	 */
	@RequiresPermissions("sys:dict:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		AjaxJson j = new AjaxJson();
		if(jeePlusProperites.isDemoMode()){
			return AjaxJson.error("演示模式，不允许操作！");
		}
		String idArray[] =ids.split(",");
		dictTypeService.batchDelete(idArray);
		return AjaxJson.success("删除字典成功！");
	}



	@GetMapping("listData")
	public AjaxJson listData(@RequestParam(required=false) String type) {
		DictType dictType = new DictType();
		dictType.setType(type);
		return AjaxJson.success().put("list", dictTypeService.findList(dictType));
	}

	@GetMapping("getDictMap")
	public AjaxJson getDictMap() {
		AjaxJson j = new AjaxJson();
		j.put("dictList", DictUtils.getDictMap());
		return j;
	}


}
