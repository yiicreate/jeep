/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.note.web;

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
import com.jeeplus.modules.test.note.entity.TestNote;
import com.jeeplus.modules.test.note.service.TestNoteService;

/**
 * 富文本测试Controller
 * @author liugf
 * @version 2021-01-05
 */
@RestController
@RequestMapping(value = "/test/note/testNote")
public class TestNoteController extends BaseController {

	@Autowired
	private TestNoteService testNoteService;

	@ModelAttribute
	public TestNote get(@RequestParam(required=false) String id) {
		TestNote entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testNoteService.get(id);
		}
		if (entity == null){
			entity = new TestNote();
		}
		return entity;
	}

	/**
	 * 富文本测试列表数据
	 */
	@RequiresPermissions("test:note:testNote:list")
	@GetMapping("list")
	public AjaxJson list(TestNote testNote, HttpServletRequest request, HttpServletResponse response) {
		Page<TestNote> page = testNoteService.findPage(new Page<TestNote>(request, response), testNote);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取富文本测试数据
	 */
	@RequiresPermissions(value={"test:note:testNote:view","test:note:testNote:add","test:note:testNote:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(TestNote testNote) {
		return AjaxJson.success().put("testNote", testNote);
	}

	/**
	 * 保存富文本测试
	 */
	@RequiresPermissions(value={"test:note:testNote:add","test:note:testNote:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(TestNote testNote, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(testNote);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		testNoteService.save(testNote);//保存
		return AjaxJson.success("保存富文本测试成功");
	}


	/**
	 * 批量删除富文本测试
	 */
	@RequiresPermissions("test:note:testNote:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testNoteService.delete(new TestNote(id));
		}
		return AjaxJson.success("删除富文本测试成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:note:testNote:export")
    @GetMapping("export")
    public AjaxJson exportFile(TestNote testNote, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "富文本测试"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TestNote> page = testNoteService.findPage(new Page<TestNote>(request, response, -1), testNote);
    		new ExportExcel("富文本测试", TestNote.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出富文本测试记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:note:testNote:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestNote> list = ei.getDataList(TestNote.class);
			for (TestNote testNote : list){
				try{
					testNoteService.save(testNote);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条富文本测试记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条富文本测试记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入富文本测试失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入富文本测试数据模板
	 */
	@RequiresPermissions("test:note:testNote:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "富文本测试数据导入模板.xlsx";
    		List<TestNote> list = Lists.newArrayList();
    		new ExportExcel("富文本测试数据", TestNote.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}