/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.comm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.comm.service.ComFilesService;

/**
 * 公共Controller
 * @author lh
 * @version 2021-03-12
 */
@RestController
@RequestMapping(value = "/test/comm/comFiles")
public class ComFilesController extends BaseController {

	@Autowired
	private ComFilesService comFilesService;

	@ModelAttribute
	public ComFiles get(@RequestParam(required=false) String id) {
		ComFiles entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = comFilesService.get(id);
		}
		if (entity == null){
			entity = new ComFiles();
		}
		return entity;
	}

	/**
	 * 公共列表数据
	 */
	@RequiresPermissions("test:comm:comFiles:list")
	@GetMapping("list")
	public AjaxJson list(ComFiles comFiles, HttpServletRequest request, HttpServletResponse response) {
		Page<ComFiles> page = comFilesService.findPage(new Page<ComFiles>(request, response), comFiles);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取公共数据
	 */
	@RequiresPermissions(value={"test:comm:comFiles:view","test:comm:comFiles:add","test:comm:comFiles:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(ComFiles comFiles) {
		return AjaxJson.success().put("comFiles", comFiles);
	}

	/**
	 * 保存公共
	 */
	@RequiresPermissions(value={"test:comm:comFiles:add","test:comm:comFiles:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(ComFiles comFiles, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(comFiles);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		comFilesService.save(comFiles);//保存
		return AjaxJson.success("保存公共成功");
	}


	/**
	 * 批量删除公共
	 */
	@RequiresPermissions("test:comm:comFiles:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			comFilesService.delete(new ComFiles(id));
		}
		return AjaxJson.success("删除公共成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:comm:comFiles:export")
    @GetMapping("export")
    public AjaxJson exportFile(ComFiles comFiles, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "公共"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ComFiles> page = comFilesService.findPage(new Page<ComFiles>(request, response, -1), comFiles);
    		new ExportExcel("公共", ComFiles.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出公共记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:comm:comFiles:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ComFiles> list = ei.getDataList(ComFiles.class);
			for (ComFiles comFiles : list){
				try{
					comFilesService.save(comFiles);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条公共记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条公共记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入公共失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入公共数据模板
	 */
	@RequiresPermissions("test:comm:comFiles:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "公共数据导入模板.xlsx";
    		List<ComFiles> list = Lists.newArrayList();
    		new ExportExcel("公共数据", ComFiles.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}