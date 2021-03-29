/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.committee.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.committee.dto.CommitteeDetailDto;
import com.jeeplus.modules.committee.entity.CommitteeIntegral;
import com.jeeplus.modules.committee.service.CommitteeIntegralService;
import com.jeeplus.core.persistence.Page;
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
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;

import java.util.List;

/**
 * 委员积分Controller
 * @author tangxin
 * @version 2021-03-20
 */
@RestController
@RequestMapping(value = "/committee/committeeIntegral")
public class CommitteeIntegralController extends BaseController {

	@Autowired
	private CommitteeIntegralService committeeIntegralService;

	@ModelAttribute
	public CommitteeIntegral get(@RequestParam(required=false) String id) {
		CommitteeIntegral entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = committeeIntegralService.get(id);
		}
		if (entity == null){
			entity = new CommitteeIntegral();
		}
		return entity;
	}

	/**
	 * 委员积分列表数据
	 */
	@RequiresPermissions("committee:committeeIntegral:list")
	@GetMapping("list")
	public AjaxJson list(CommitteeIntegral committeeIntegral, HttpServletRequest request, HttpServletResponse response) {
		Page<CommitteeIntegral> page = committeeIntegralService.findPage(new Page<CommitteeIntegral>(request, response), committeeIntegral);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取委员积分数据
	 */
	@RequiresPermissions(value={"committee:committeeIntegral:view","committee:committeeIntegral:add","committee:committeeIntegral:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(CommitteeIntegral committeeIntegral) {
		return AjaxJson.success().put("committeeIntegral", committeeIntegral);
	}

	/**
	 * 保存委员积分
	 */
	@RequiresPermissions(value={"committee:committeeIntegral:add","committee:committeeIntegral:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(CommitteeIntegral committeeIntegral, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(committeeIntegral);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		committeeIntegralService.save(committeeIntegral);//保存
		return AjaxJson.success("保存委员积分成功");
	}


	@PostMapping("saveDetail")
	public AjaxJson save(CommitteeDetailDto committeeDetailDto) {
		//新增或编辑表单保存
		committeeIntegralService.saveDetail(committeeDetailDto.getCommitteeId(),
				committeeDetailDto.getIntegralType(),committeeDetailDto.getIntegral());//保存
		return AjaxJson.success("保存委员积分明细成功");
	}


	/**
	 * 批量删除委员积分
	 */
	@RequiresPermissions("committee:committeeIntegral:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			committeeIntegralService.delete(new CommitteeIntegral(id));
		}
		return AjaxJson.success("删除委员积分成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("committee:committeeIntegral:export")
    @GetMapping("export")
    public AjaxJson exportFile(CommitteeIntegral committeeIntegral, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "委员积分"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CommitteeIntegral> page = committeeIntegralService.findPage(new Page<CommitteeIntegral>(request, response, -1), committeeIntegral);
    		new ExportExcel("委员积分", CommitteeIntegral.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出委员积分记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("committee:committeeIntegral:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CommitteeIntegral> list = ei.getDataList(CommitteeIntegral.class);
			for (CommitteeIntegral committeeIntegral : list){
				try{
					committeeIntegralService.save(committeeIntegral);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条委员积分记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条委员积分记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入委员积分失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入委员积分数据模板
	 */
	@RequiresPermissions("committee:committeeIntegral:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "委员积分数据导入模板.xlsx";
    		List<CommitteeIntegral> list = Lists.newArrayList();
    		new ExportExcel("委员积分数据", CommitteeIntegral.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}