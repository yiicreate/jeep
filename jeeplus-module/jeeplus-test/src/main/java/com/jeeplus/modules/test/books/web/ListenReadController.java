/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.books.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.test.books.entity.ListenRead;
import com.jeeplus.modules.test.books.service.ListenReadService;
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

/**
 * 在线听书看书Controller
 * @author lc
 * @version 2021-03-16
 */
@RestController
@RequestMapping(value = "/books/listenRead")
@Api(tags = "在线听书看书")
public class ListenReadController extends BaseController {

	@Autowired
	private ListenReadService listenReadService;

	@ModelAttribute
	public ListenRead get(@RequestParam(required=false) String id) {
		ListenRead entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = listenReadService.get(id);
		}
		if (entity == null){
			entity = new ListenRead();
		}
		return entity;
	}

	/**
	 * 在线听书看书列表数据
	 */
	@RequiresPermissions("books:listenRead:list")
	@GetMapping("list")
	@ApiOperation(value = "获取书籍列表")
	public AjaxJson list(ListenRead listenRead, HttpServletRequest request, HttpServletResponse response) {
		Page<ListenRead> page = listenReadService.findPage(new Page<ListenRead>(request, response), listenRead);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取在线听书看书数据
	 */
	@RequiresPermissions(value={"books:listenRead:view","books:listenRead:add","books:listenRead:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	@ApiOperation(value = "根据Id获取书籍数据")
	public AjaxJson queryById(ListenRead listenRead) {
		return AjaxJson.success().put("listenRead", listenRead);
	}

	/**
	 * 保存在线听书看书
	 */
	@RequiresPermissions(value={"books:listenRead:add","books:listenRead:edit"},logical=Logical.OR)
	@PostMapping("save")
	@ApiOperation(value = "新建书籍数据")
	public AjaxJson save(ListenRead listenRead, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(listenRead);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		listenReadService.save(listenRead);//保存
		return AjaxJson.success("保存在线听书看书成功");
	}


	/**
	 * 批量删除在线听书看书
	 */
	@RequiresPermissions("books:listenRead:del")
	@DeleteMapping("delete")
	@ApiOperation(value = "删除书籍数据")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			listenReadService.delete(new ListenRead(id));
		}
		return AjaxJson.success("删除在线听书看书成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("books:listenRead:export")
    @GetMapping("export")
    public AjaxJson exportFile(ListenRead listenRead, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "在线听书看书"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ListenRead> page = listenReadService.findPage(new Page<ListenRead>(request, response, -1), listenRead);
    		new ExportExcel("在线听书看书", ListenRead.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出在线听书看书记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("books:listenRead:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ListenRead> list = ei.getDataList(ListenRead.class);
			for (ListenRead listenRead : list){
				try{
					listenReadService.save(listenRead);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条在线听书看书记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条在线听书看书记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入在线听书看书失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入在线听书看书数据模板
	 */
	@RequiresPermissions("books:listenRead:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "在线听书看书数据导入模板.xlsx";
    		List<ListenRead> list = Lists.newArrayList();
    		new ExportExcel("在线听书看书数据", ListenRead.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}