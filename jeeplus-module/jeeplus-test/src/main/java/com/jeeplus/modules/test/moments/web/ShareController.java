/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.moments.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSON;
import com.jeeplus.ding.io.DingNotice;
import com.jeeplus.ding.io.DingNoticeIo;
import com.jeeplus.ding.utils.DingUtil;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.test.comm.entity.ComFiles;
import com.jeeplus.modules.test.comm.service.ComFilesService;
import com.jeeplus.modules.test.moments.service.RemindService;
import com.jeeplus.modules.test.moments.vo.ShareVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import com.jeeplus.modules.test.moments.entity.Share;
import com.jeeplus.modules.test.moments.service.ShareService;

import static com.jeeplus.modules.test.comm.config.FilesType.SHARE_FILE;

/**
 * 委员圈Controller
 * @author lh
 * @version 2021-03-10
 */
@RestController
@RequestMapping(value = "/test/moments/share")
@Api(tags = "委员圈")
public class ShareController extends BaseController {

	@Autowired
	private ShareService shareService;

	@Autowired
	private ComFilesService comFilesService;

	@Autowired
	private UserService userService;

	@Autowired
	private RemindService remindService;

	@Autowired
	private DingUtil dingUtil;

	@Autowired
	private ShareVo shareVo;
	@ModelAttribute
	public Share get(@RequestParam(required=false) String id) {
		Share entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = shareService.get(id);
		}
		if (entity == null){
			entity = new Share();
		}
		return entity;
	}

	/**
	 * 委员圈列表数据
	 */
	@GetMapping("list")
	public AjaxJson list(Share share, HttpServletRequest request, HttpServletResponse response) {
		Page<Share> page = shareService.findPage(new Page<Share>(request, response), share);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 委员圈列表数据
	 */
	@GetMapping("lists")
	@ApiOperation(value = "委员圈列表")
	public AjaxJson lists(Share share, HttpServletRequest request, HttpServletResponse response) {
		Page<Share> page = shareService.findPageBySup(new Page<Share>(request, response), share);
		return AjaxJson.success().put("page",page);
	}

	/**
	 * 根据Id获取委员圈数据
	 */
	@GetMapping("queryById")
	public AjaxJson queryById(Share share) {
		return AjaxJson.success().put("share", shareService.getAndSup(share.getId()));
	}

	/**
	 * 保存委员圈
	 */
	@PostMapping("save")
	@ApiOperation(value = "新建委员圈")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "content", value = "内容",required = true),
			@ApiImplicitParam(name = "files",value = "附件，[{\"url\":\"/userfiles\\u0001程序附件委员圈暂离.jpg\",\"type\":\"image\"},{\"url\":\"/userfiles\\u0001程序附件委员圈VID_20200818_175922.mp4\",\"type\":\"vedio\"}]"),
			@ApiImplicitParam(name = "users",value = "@人员多个逗号分开",required = true)
	})
	public AjaxJson save(Share share,  @RequestParam("files") String files,String users,Model model) throws Exception{
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(share);
		if (StringUtils.isNotBlank(errMsg)){
			return AjaxJson.error(errMsg);
		}

		List<ComFiles> comFilesList = JSON.parseArray(files, ComFiles.class);
		//新增或编辑表单保存
		shareService.save(share);//保存

		List<String> paths = new ArrayList<>();
		for (int i= 0;i<comFilesList.size();i++){
			ComFiles sf = comFilesList.get(i);
			sf.setOwnerId(share.getId());
			sf.setSource(SHARE_FILE);
			comFilesService.save(sf);
			paths.add(sf.getUrl());
		}

		if(!StringUtils.isBlank(users)){
			List<User> userList = userService.findListByIds(users);
			String user = "";
			for (User u:userList) {
				remindService.saveList(u,share.getId());
				if(!StringUtils.isBlank(u.getUserId())){
					user += u.getUserId()+",";
				}
			}
			DingNoticeIo dingNoticeIo = new DingNoticeIo();
			dingNoticeIo.setContentList(shareVo.shareNotice(share,comFilesList));
			dingNoticeIo.setUsers(user.substring(0,user.length()-1));
			dingUtil.sendWorkNotice(dingNoticeIo);
		}

		return AjaxJson.success("保存委员圈成功");
	}

	/**
	 * 批量删除委员圈
	 */
	@DeleteMapping("delete")
	@ApiOperation(value = "删除委员圈")
	public AjaxJson delete(String ids) {
        Share share = shareService.get(ids);
        if(!share.getCurrentUser().getId().equals(share.getCreateBy().getId()) ){
            return AjaxJson.error("没有删除权限");
        }
        shareService.delete(share);
		return AjaxJson.success("删除委员圈成功");
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:moments:share:export")
    @GetMapping("export")
    public AjaxJson exportFile(Share share, HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "委员圈"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Share> page = shareService.findPage(new Page<Share>(request, response, -1), share);
    		new ExportExcel("委员圈", Share.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error("导出委员圈记录失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:moments:share:import")
    @PostMapping("import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Share> list = ei.getDataList(Share.class);
			for (Share share : list){
				try{
					shareService.save(share);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条委员圈记录。");
			}
			return AjaxJson.success( "已成功导入 "+successNum+" 条委员圈记录"+failureMsg);
		} catch (Exception e) {
			return AjaxJson.error("导入委员圈失败！失败信息："+e.getMessage());
		}
    }

	/**
	 * 下载导入委员圈数据模板
	 */
	@RequiresPermissions("test:moments:share:import")
    @GetMapping("import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		try {
            String fileName = "委员圈数据导入模板.xlsx";
    		List<Share> list = Lists.newArrayList();
    		new ExportExcel("委员圈数据", Share.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			return AjaxJson.error( "导入模板下载失败！失败信息："+e.getMessage());
		}
    }


}