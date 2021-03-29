/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.literature.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSON;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.comm.service.ComFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import com.jeeplus.modules.test.literature.entity.Material;
import com.jeeplus.modules.test.literature.service.MaterialService;

import static com.jeeplus.modules.test.comm.config.FilesType.MATERIAL_FILE;

/**
 * 数字文史Controller
 * @author lh
 * @version 2021-03-12
 */
@RestController
@RequestMapping(value = "/test/literature/material")
@Api(tags = "资料管理")
public class MaterialController extends BaseController {

	@Autowired
	private MaterialService materialService;

	@Autowired
	private ComFilesService comFilesService;

	@ModelAttribute
	public Material get(@RequestParam(required=false) String id) {
		Material entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = materialService.get(id);
		}
		if (entity == null){
			entity = new Material();
		}
		return entity;
	}

	/**
	 * 数字文史列表数据
	 */
//	@RequiresPermissions("test:literature:material:list")
	@GetMapping("list")
	@ApiOperation("资料列表")
	public AjaxJson list(Material material, HttpServletRequest request, HttpServletResponse response) {
		Page<Material> page = materialService.findPage(new Page<Material>(request, response), material);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 数字文史列表数据
	 */
	@GetMapping("overtList")
	@ApiOperation("公开资料")
	public AjaxJson overtList(Material material, HttpServletRequest request, HttpServletResponse response) {
		material.setStep("1");
		Page<Material> page = materialService.findPage(new Page<Material>(request, response), material);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取数字文史数据
	 */
//	@RequiresPermissions(value={"test:literature:material:view","test:literature:material:add","test:literature:material:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	@ApiOperation("资料详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",value = "资料主键",required = true),
	})
	public AjaxJson queryById(Material material) {
		return AjaxJson.success().put("material", material);
	}

	/**
	 * 保存数字文史
	 */
//	@RequiresPermissions(value={"test:literature:material:add","test:literature:material:edit"},logical=Logical.OR)
	@PostMapping("save")
	@ApiOperation("资料上传")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "material",value = "资料相关",required = true)
	})
	public AjaxJson save(Material material,  Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(material);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		materialService.save(material);//保存

//		if(!StringUtils.isBlank(files)){
//			List<ComFiles> comFilesList = JSON.parseArray(files, ComFiles.class);
//			comFilesService.save(comFilesList,MATERIAL_FILE,material.getId());
//		}
		return AjaxJson.success("保存数字文史成功");
	}

	@PostMapping("overt")
	@ApiOperation("资料公开")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",value = "资料主键",required = true),
			@ApiImplicitParam(name = "step",value = "公开1，不公开0",required = true),
	})
	public AjaxJson overt(Material material, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(material);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		materialService.save(material);//保存

		return AjaxJson.success("保存数字文史成功");
	}


	/**
	 * 批量删除数字文史
	 */
//	@RequiresPermissions("test:literature:material:del")
	@DeleteMapping("delete")
	@ApiOperation("资料删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids",value = "主键",required = true),
	})
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			materialService.delete(new Material(id));
		}
		return AjaxJson.success("删除数字文史成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:literature:material:export")
    @GetMapping("export")
    public AjaxJson exportFile(Material material, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "数字文史"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Material> page = materialService.findPage(new Page<Material>(request, response, -1), material);
    		new ExportExcel("数字文史", Material.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出数字文史记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:literature:material:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Material> list = ei.getDataList(Material.class);
			for (Material material : list){
				try{
					materialService.save(material);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条数字文史记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条数字文史记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入数字文史失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入数字文史数据模板
	 */
	@RequiresPermissions("test:literature:material:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "数字文史数据导入模板.xlsx";
    		List<Material> list = Lists.newArrayList();
    		new ExportExcel("数字文史数据", Material.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}