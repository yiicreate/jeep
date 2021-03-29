/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web.app;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.config.properties.JeePlusProperites;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 机构Controller
 * @author jeeplus
 * @version 2019-5-15
 */
@RestController
@RequestMapping("/app/sys/office")
public class AppOfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;


	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@GetMapping("treeData")
	public AjaxJson treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
                                              @RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Office> list = officeService.findList(isAll);
		List rootTree = getRootTree(list, extId, type, grade, isAll);
		return AjaxJson.success().put("treeData",rootTree);
	}

	private List<Office> getRootTree(List<Office> list, String extId, String type, Long grade, Boolean isAll) {
		List<Office> offices = Lists.newArrayList();
		List<Office> rootTrees = officeService.getChildren("0");
		for (Office root:rootTrees){
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(root.getId()) && root.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(root.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(root.getGrade()) <= grade.intValue()))
					&& JeePlusProperites.YES.equals(root.getUseable())){
				offices.add(getChildOfTree(root, list, extId, type, grade, isAll));
			}
		}
		return offices;
	}

	private  Office getChildOfTree(Office officeItem,  List<Office> officeList, String extId, String type, Long grade, Boolean isAll) {
		officeItem.setChildren(Lists.newArrayList());
		if("2".equals(type) && officeItem.getType().equals("1")){
			officeItem.setDisabled(true);
		}else {
			officeItem.setDisabled(false);
		}
		for (Office child : officeList) {
			if ((StringUtils.isBlank(extId) || (extId!=null && !extId.equals(child.getId()) && child.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(child.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(child.getGrade()) <= grade.intValue()))
					&& JeePlusProperites.YES.equals(child.getUseable())){
				if (child.getParentId().equals(officeItem.getId())) {
					officeItem.getChildren().add(getChildOfTree(child, officeList, extId, type, grade, isAll));

				}
			}
		}
		return officeItem;
	}
}
