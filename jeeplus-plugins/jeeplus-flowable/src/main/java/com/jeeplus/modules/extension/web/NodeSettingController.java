/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.web;

import java.util.List;

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
import com.jeeplus.modules.extension.entity.NodeSetting;
import com.jeeplus.modules.extension.service.NodeSettingService;

/**
 * 节点设置Controller
 * @author 刘高峰
 * @version 2021-01-11
 */
@RestController
@RequestMapping(value = "/extension/nodeSetting")
public class NodeSettingController extends BaseController {

	@Autowired
	private NodeSettingService nodeSettingService;

	@ModelAttribute
	public NodeSetting get(@RequestParam(required=false) String id) {
		NodeSetting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = nodeSettingService.get(id);
		}
		if (entity == null){
			entity = new NodeSetting();
		}
		return entity;
	}

	/**
	 * 节点列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(NodeSetting nodeSetting, HttpServletRequest request, HttpServletResponse response) {
		Page<NodeSetting> page = nodeSettingService.findPage(new Page<NodeSetting>(request, response), nodeSetting);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取节点数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(NodeSetting nodeSetting) {
		return AjaxJson.success().put("nodeSetting", nodeSetting);
	}


	/**
	 * 根据Id获取节点数据
	 */
	@GetMapping("queryValueByKey")
	public AjaxJson queryById(String processDefId, String taskDefId, String key) {
		NodeSetting nodeSetting = nodeSettingService.queryByKey (processDefId, taskDefId, key);
		if(nodeSetting == null){
			return AjaxJson.success ().put ("value", "");
		}else{
			return AjaxJson.success ().put ("value", nodeSetting.getValue ());
		}
	}




	/**
	 * 保存节点
	 */

	/**
	 * 保存表单只读配置
	 */
	@PostMapping("save")
	public AjaxJson save(@RequestBody List<NodeSetting> nodeSettingList) throws Exception{

		for(NodeSetting nodeSetting: nodeSettingList){
			nodeSettingService.deleteByDefIdAndTaskId(nodeSetting);//删除节点旧的属性
		}

		for(NodeSetting nodeSetting: nodeSettingList){
			nodeSettingService.save (nodeSetting);
		}

		return AjaxJson.success("保存配置成功");
	}

	/**
	 * 批量删除节点
	 */
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			nodeSettingService.delete(new NodeSetting(id));
		}
		return AjaxJson.success("删除节点成功");
	}



}
