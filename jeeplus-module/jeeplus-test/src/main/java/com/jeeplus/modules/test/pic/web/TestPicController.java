/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.pic.web;

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
import com.jeeplus.modules.test.pic.entity.TestPic;
import com.jeeplus.modules.test.pic.service.TestPicService;

/**
 * 图片管理Controller
 * @author lgf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/pic/testPic")
public class TestPicController extends BaseController {

	@Autowired
	private TestPicService testPicService;

	@ModelAttribute
	public TestPic get(@RequestParam(required=false) String id) {
		TestPic entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testPicService.get(id);
		}
		if (entity == null){
			entity = new TestPic();
		}
		return entity;
	}

	/**
	 * 图片管理列表数据
	 */
	@RequiresPermissions("test:pic:testPic:list")
	@GetMapping("list")
	public AjaxJson list(TestPic testPic, HttpServletRequest request, HttpServletResponse response) {
		Page<TestPic> page = testPicService.findPage(new Page<TestPic>(request, response), testPic);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取图片管理数据
	 */
	@RequiresPermissions(value={"test:pic:testPic:view","test:pic:testPic:add","test:pic:testPic:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(TestPic testPic) {
		return AjaxJson.success().put("testPic", testPic);
	}

	/**
	 * 保存图片管理
	 */
	@RequiresPermissions(value={"test:pic:testPic:add","test:pic:testPic:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(TestPic testPic, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testPic);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		testPicService.save(testPic);//保存
		return AjaxJson.success("保存图片管理成功");
	}


	/**
	 * 批量删除图片管理
	 */
	@RequiresPermissions("test:pic:testPic:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testPicService.delete(new TestPic(id));
		}
		return AjaxJson.success("删除图片管理成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:pic:testPic:export")
    @GetMapping("export")
    public AjaxJson exportFile(TestPic testPic, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "图片管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestPic> page = testPicService.findPage(new Page<TestPic>(request, response, -1), testPic);
    		new ExportExcel("图片管理", TestPic.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出图片管理记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:pic:testPic:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestPic> list = ei.getDataList(TestPic.class);
			for (TestPic testPic : list){
				try{
					testPicService.save(testPic);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条图片管理记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条图片管理记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入图片管理失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入图片管理数据模板
	 */
	@RequiresPermissions("test:pic:testPic:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "图片管理数据导入模板.xlsx";
    		List<TestPic> list = Lists.newArrayList();
    		new ExportExcel("图片管理数据", TestPic.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}