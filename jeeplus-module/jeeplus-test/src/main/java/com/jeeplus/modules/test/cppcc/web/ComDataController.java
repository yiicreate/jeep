/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.cppcc.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.test.cppcc.entity.ComData;
import com.jeeplus.modules.test.cppcc.service.ComDataService;

/**
 * 政协通讯Controller
 * @author lc
 * @version 2021-03-15
 */
@RestController
@RequestMapping(value = "/cppcc/communication/comData")
@Api(tags = "政协通讯")
public class ComDataController extends BaseController {

	@Autowired
	private ComDataService comDataService;

	@ModelAttribute
	public ComData get(@RequestParam(required=false) String id) {
		ComData entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = comDataService.get(id);
		}
		if (entity == null){
			entity = new ComData();
		}
		return entity;
	}

	/**
	 * 政协通讯管理列表数据
	 */
	@RequiresPermissions("cppcc:communication:comData:list")
	@GetMapping("list")
	@ApiOperation(value = "政协通讯列表")
	public AjaxJson list(ComData comData, HttpServletRequest request, HttpServletResponse response) {
		Page<ComData> page = comDataService.findPage(new Page<ComData>(request, response), comData);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取政协通讯管理数据
	 */
	@RequiresPermissions(value={"cppcc:communication:comData:view","cppcc:communication:comData:add","cppcc:communication:comData:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	@ApiOperation(value = "根据Id获取政协通讯管理数据")
	public AjaxJson queryById(ComData comData) {
		return AjaxJson.success().put("comData", comData);
	}

	/**
	 * 保存政协通讯管理
	 */
	@RequiresPermissions(value={"cppcc:communication:comData:add","cppcc:communication:comData:edit"},logical=Logical.OR)
	@PostMapping("save")
	@ApiOperation(value = "新建政协通讯以及评论数据")
	public AjaxJson save(ComData comData, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(comData);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		comDataService.save(comData);//保存
		return AjaxJson.success("保存政协通讯管理成功");
	}


	/**
	 * 批量删除政协通讯管理
	 */
	@RequiresPermissions("cppcc:communication:comData:del")
	@DeleteMapping("delete")
	@ApiOperation(value = "删除政协通讯以及评论数据")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			comDataService.delete(new ComData(id));
		}
		return AjaxJson.success("删除政协通讯管理成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("cppcc:communication:comData:export")
    @GetMapping("export")
    public AjaxJson exportFile(ComData comData, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "政协通讯管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ComData> page = comDataService.findPage(new Page<ComData>(request, response, -1), comData);
    		new ExportExcel("政协通讯管理", ComData.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出政协通讯管理记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cppcc:communication:comData:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ComData> list = ei.getDataList(ComData.class);
			for (ComData comData : list){
				try{
					comDataService.save(comData);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条政协通讯管理记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条政协通讯管理记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入政协通讯管理失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入政协通讯管理数据模板
	 */
	@RequiresPermissions("cppcc:communication:comData:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "政协通讯管理数据导入模板.xlsx";
    		List<ComData> list = Lists.newArrayList();
    		new ExportExcel("政协通讯管理数据", ComData.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}