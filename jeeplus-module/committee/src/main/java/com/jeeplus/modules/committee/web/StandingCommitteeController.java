/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.web;

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
import com.jeeplus.modules.committee.entity.StandingCommittee;
import com.jeeplus.modules.committee.service.StandingCommitteeService;

/**
 * 委员相关功能Controller
 * @author tangxin
 * @version 2021-03-24
 */
@RestController
@RequestMapping(value = "/committee/standingCommittee")
public class StandingCommitteeController extends BaseController {

	@Autowired
	private StandingCommitteeService standingCommitteeService;

	@ModelAttribute
	public StandingCommittee get(@RequestParam(required=false) String id) {
		StandingCommittee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = standingCommitteeService.get(id);
		}
		if (entity == null){
			entity = new StandingCommittee();
		}
		return entity;
	}

	/**
	 * 委员会列表数据
	 */
	@RequiresPermissions("committee:standingCommittee:list")
	@GetMapping("list")
	public AjaxJson list(StandingCommittee standingCommittee, HttpServletRequest request, HttpServletResponse response) {
		Page<StandingCommittee> page = standingCommitteeService.findPage(new Page<StandingCommittee>(request, response), standingCommittee);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取委员会数据
	 */
	@RequiresPermissions(value={"committee:standingCommittee:view","committee:standingCommittee:add","committee:standingCommittee:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(StandingCommittee standingCommittee) {
		return AjaxJson.success().put("standingCommittee", standingCommittee);
	}

	/**
	 * 保存委员会
	 */
	@RequiresPermissions(value={"committee:standingCommittee:add","committee:standingCommittee:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(StandingCommittee standingCommittee, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(standingCommittee);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		standingCommitteeService.save(standingCommittee);//保存
		return AjaxJson.success("保存委员会成功");
	}


	/**
	 * 批量删除委员会
	 */
	@RequiresPermissions("committee:standingCommittee:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			standingCommitteeService.delete(new StandingCommittee(id));
		}
		return AjaxJson.success("删除委员会成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("committee:standingCommittee:export")
    @GetMapping("export")
    public AjaxJson exportFile(StandingCommittee standingCommittee, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "委员会"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<StandingCommittee> page = standingCommitteeService.findPage(new Page<StandingCommittee>(request, response, -1), standingCommittee);
    		new ExportExcel("委员会", StandingCommittee.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出委员会记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("committee:standingCommittee:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<StandingCommittee> list = ei.getDataList(StandingCommittee.class);
			for (StandingCommittee standingCommittee : list){
				try{
					standingCommitteeService.save(standingCommittee);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条委员会记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条委员会记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入委员会失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入委员会数据模板
	 */
	@RequiresPermissions("committee:standingCommittee:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "委员会数据导入模板.xlsx";
    		List<StandingCommittee> list = Lists.newArrayList();
    		new ExportExcel("委员会数据", StandingCommittee.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}