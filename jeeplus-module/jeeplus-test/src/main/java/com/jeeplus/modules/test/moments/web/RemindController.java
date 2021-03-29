/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.web;

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
import com.jeeplus.modules.test.moments.entity.Remind;
import com.jeeplus.modules.test.moments.service.RemindService;

/**
 * 提醒人Controller
 * @author lh
 * @version 2021-03-18
 */
@RestController
@RequestMapping(value = "/test/moments/remind")
public class RemindController extends BaseController {

	@Autowired
	private RemindService remindService;

	@ModelAttribute
	public Remind get(@RequestParam(required=false) String id) {
		Remind entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = remindService.get(id);
		}
		if (entity == null){
			entity = new Remind();
		}
		return entity;
	}

	/**
	 * 提醒人列表数据
	 */
	@RequiresPermissions("test:moments:remind:list")
	@GetMapping("list")
	public AjaxJson list(Remind remind, HttpServletRequest request, HttpServletResponse response) {
		Page<Remind> page = remindService.findPage(new Page<Remind>(request, response), remind);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取提醒人数据
	 */
	@RequiresPermissions(value={"test:moments:remind:view","test:moments:remind:add","test:moments:remind:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(Remind remind) {
		return AjaxJson.success().put("remind", remind);
	}

	/**
	 * 保存提醒人
	 */
	@RequiresPermissions(value={"test:moments:remind:add","test:moments:remind:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(Remind remind, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(remind);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		remindService.save(remind);//保存
		return AjaxJson.success("保存提醒人成功");
	}


	/**
	 * 批量删除提醒人
	 */
	@RequiresPermissions("test:moments:remind:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			remindService.delete(new Remind(id));
		}
		return AjaxJson.success("删除提醒人成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:moments:remind:export")
    @GetMapping("export")
    public AjaxJson exportFile(Remind remind, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "提醒人"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Remind> page = remindService.findPage(new Page<Remind>(request, response, -1), remind);
    		new ExportExcel("提醒人", Remind.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出提醒人记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:moments:remind:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Remind> list = ei.getDataList(Remind.class);
			for (Remind remind : list){
				try{
					remindService.save(remind);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条提醒人记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条提醒人记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入提醒人失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入提醒人数据模板
	 */
	@RequiresPermissions("test:moments:remind:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "提醒人数据导入模板.xlsx";
    		List<Remind> list = Lists.newArrayList();
    		new ExportExcel("提醒人数据", Remind.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}