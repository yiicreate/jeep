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
import com.jeeplus.modules.test.comm.entity.ComDing;
import com.jeeplus.modules.test.comm.service.ComDingService;

/**
 * 钉钉推送Controller
 * @author lh
 * @version 2021-03-22
 */
@RestController
@RequestMapping(value = "/test/comm/comDing")
public class ComDingController extends BaseController {

	@Autowired
	private ComDingService comDingService;

	@ModelAttribute
	public ComDing get(@RequestParam(required=false) String id) {
		ComDing entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = comDingService.get(id);
		}
		if (entity == null){
			entity = new ComDing();
		}
		return entity;
	}

	/**
	 * 推送列表数据
	 */
	@RequiresPermissions("test:comm:comDing:list")
	@GetMapping("list")
	public AjaxJson list(ComDing comDing, HttpServletRequest request, HttpServletResponse response) {
		Page<ComDing> page = comDingService.findPage(new Page<ComDing>(request, response), comDing);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取推送数据
	 */
	@RequiresPermissions(value={"test:comm:comDing:view","test:comm:comDing:add","test:comm:comDing:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(ComDing comDing) {
		return AjaxJson.success().put("comDing", comDing);
	}

	/**
	 * 保存推送
	 */
	@RequiresPermissions(value={"test:comm:comDing:add","test:comm:comDing:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(ComDing comDing, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(comDing);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		comDingService.save(comDing);//保存
		return AjaxJson.success("保存推送成功");
	}


	/**
	 * 批量删除推送
	 */
	@RequiresPermissions("test:comm:comDing:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			comDingService.delete(new ComDing(id));
		}
		return AjaxJson.success("删除推送成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:comm:comDing:export")
    @GetMapping("export")
    public AjaxJson exportFile(ComDing comDing, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "推送"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ComDing> page = comDingService.findPage(new Page<ComDing>(request, response, -1), comDing);
    		new ExportExcel("推送", ComDing.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出推送记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:comm:comDing:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ComDing> list = ei.getDataList(ComDing.class);
			for (ComDing comDing : list){
				try{
					comDingService.save(comDing);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条推送记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条推送记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入推送失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入推送数据模板
	 */
	@RequiresPermissions("test:comm:comDing:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "推送数据导入模板.xlsx";
    		List<ComDing> list = Lists.newArrayList();
    		new ExportExcel("推送数据", ComDing.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}