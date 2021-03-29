/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.video.web;

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
import com.jeeplus.modules.test.video.entity.Member;
import com.jeeplus.modules.test.video.service.MemberService;

/**
 * 参与人Controller
 * @author lh
 * @version 2021-03-17
 */
@RestController
@RequestMapping(value = "/test/video/member")
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;

	@ModelAttribute
	public Member get(@RequestParam(required=false) String id) {
		Member entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = memberService.get(id);
		}
		if (entity == null){
			entity = new Member();
		}
		return entity;
	}

	/**
	 * 参与人列表数据
	 */
	@RequiresPermissions("test:video:member:list")
	@GetMapping("list")
	public AjaxJson list(Member member, HttpServletRequest request, HttpServletResponse response) {
		Page<Member> page = memberService.findPage(new Page<Member>(request, response), member);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取参与人数据
	 */
	@RequiresPermissions(value={"test:video:member:view","test:video:member:add","test:video:member:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	public AjaxJson queryById(Member member) {
		return AjaxJson.success().put("member", member);
	}

	/**
	 * 保存参与人
	 */
	@RequiresPermissions(value={"test:video:member:add","test:video:member:edit"},logical=Logical.OR)
	@PostMapping("save")
	public AjaxJson save(Member member, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(member);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		//新增或编辑表单保存
		memberService.save(member);//保存
		return AjaxJson.success("保存参与人成功");
	}


	/**
	 * 批量删除参与人
	 */
	@RequiresPermissions("test:video:member:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			memberService.delete(new Member(id));
		}
		return AjaxJson.success("删除参与人成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:video:member:export")
    @GetMapping("export")
    public AjaxJson exportFile(Member member, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "参与人"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Member> page = memberService.findPage(new Page<Member>(request, response, -1), member);
    		new ExportExcel("参与人", Member.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出参与人记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:video:member:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Member> list = ei.getDataList(Member.class);
			for (Member member : list){
				try{
					memberService.save(member);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条参与人记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条参与人记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入参与人失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入参与人数据模板
	 */
	@RequiresPermissions("test:video:member:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "参与人数据导入模板.xlsx";
    		List<Member> list = Lists.newArrayList();
    		new ExportExcel("参与人数据", Member.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}