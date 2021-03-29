/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.video.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.ding.utils.TimeUtil;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.test.comm.entity.ComDing;
import com.jeeplus.modules.test.comm.service.ComDingService;
import com.jeeplus.modules.test.video.service.MemberService;
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
import com.jeeplus.modules.test.video.entity.Live;
import com.jeeplus.modules.test.video.service.LiveService;

import static com.jeeplus.modules.test.video.service.LiveService.*;

/**
 * 直播Controller
 * @author lh
 * @version 2021-03-17
 */
@RestController
@RequestMapping(value = "/test/video/live")
@Api(tags = "直播/会议管理")
public class LiveController extends BaseController {

	@Autowired
	private LiveService liveService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private UserService userService;

	@Autowired
	private ComDingService comDingService;

	@ModelAttribute
	public Live get(@RequestParam(required=false) String id) {
		Live entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = liveService.get(id);
		}
		if (entity == null){
			entity = new Live();
		}
		return entity;
	}

	/**
	 * 直播列表数据
	 */
//	@RequiresPermissions("test:video:live:list")
	@GetMapping("list")
	@ApiOperation("直播/会议列表(发起人)")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type",value = "直播1,会议2")
	})
	public AjaxJson list(Live live, HttpServletRequest request, HttpServletResponse response) {
		Page<Live> page = liveService.findListByCurrentPage(new Page<Live>(request, response), live);
		return AjaxJson.success().put("page",page);
	}

	@GetMapping("lists")
	@ApiOperation("直播/会议列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type",value = "直播1,会议2")
	})
	public AjaxJson lists(Live live, HttpServletRequest request, HttpServletResponse response) {
		Page<Live> page = liveService.findPage(new Page<Live>(request, response), live);
		return AjaxJson.success().put("page",page);
	}


	/**
	 * 根据Id获取直播数据
	 */
//	@RequiresPermissions(value={"test:video:live:view","test:video:live:add","test:video:live:edit"},logical=Logical.OR)
	@GetMapping("queryById")
	@ApiOperation("直播/会议详情")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",value = "主键")
	})
	public AjaxJson queryById(Live live) {
		return AjaxJson.success().put("live", live);
	}

	/**
	 * 保存直播
	 */
//	@RequiresPermissions(value={"test:video:live:add","test:video:live:edit"},logical=Logical.OR)
	@PostMapping("save")
	@ApiOperation("保存直播/会议")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "live",value = "会议信息"),
			@ApiImplicitParam(name = "users",value = "参与人逗号分开")
	})
	public AjaxJson save(Live live,String users, Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(live);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}
		if(StringUtils.isBlank(live.getType())){
			live.setType("2");
		}

		//新增或编辑表单保存
		liveService.save(live);//保存

		if(!StringUtils.isBlank(users)){
			List<User> userList = userService.findListByIds(users);
			for (User u:userList) {
				memberService.saveByUser(u,live);
			}
		}
		if("0".equals(live.getStep())){
			Map<String,Object> map = liveService.sendEvent(live);
			if(map!=null && map.get("code").equals("0")){
				ComDing comDing  = new ComDing();
				comDing.setDingId((String) map.get("event_id"));
				comDing.setDingType(dingType);
				comDing.setSourceType(sourceType);
				comDing.setSource(live.getId());
				comDingService.save(comDing);
			}
		}
		return AjaxJson.success("保存直播成功");
	}


	@GetMapping("start")
	@ApiOperation("开始会议/直播")
	@ApiImplicitParam(name = "id",value = "主键")
	public AjaxJson start(Live live) {
		live.setStartTime(TimeUtil.getCurrent());
		live.setStep("1");
		liveService.save(live);
		return AjaxJson.success().put("live", live);
	}

	@GetMapping("end")
	@ApiOperation("结束会议/直播")
	@ApiImplicitParam(name = "id",value = "主键")
	public AjaxJson end(Live live) {
		live.setEndTime(TimeUtil.getCurrent());
		live.setStep("2");
		liveService.save(live);
		return AjaxJson.success().put("live", live);
	}

	@GetMapping("cancel")
	@ApiOperation("取消会议/直播")
	@ApiImplicitParam(name = "id",value = "主键")
	public AjaxJson cancel(Live live) {
		live.setEndTime(TimeUtil.getCurrent());
		live.setStep("9");
		liveService.save(live);
		liveService.cancelEvent(live);
		return AjaxJson.success().put("live", live);
	}

	/**
	 * 批量删除直播
	 */
//	@RequiresPermissions("test:video:live:del")
	@DeleteMapping("delete")
	public AjaxJson delete(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Live l = new Live(id);
			liveService.delete(l);
			liveService.cancelEvent(l);
		}
		return AjaxJson.success("删除直播成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:video:live:export")
    @GetMapping("export")
    public AjaxJson exportFile(Live live, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "直播"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Live> page = liveService.findPage(new Page<Live>(request, response, -1), live);
    		new ExportExcel("直播", Live.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出直播记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:video:live:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Live> list = ei.getDataList(Live.class);
			for (Live live : list){
				try{
					liveService.save(live);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条直播记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条直播记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入直播失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入直播数据模板
	 */
	@RequiresPermissions("test:video:live:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "直播数据导入模板.xlsx";
    		List<Live> list = Lists.newArrayList();
    		new ExportExcel("直播数据", Live.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}