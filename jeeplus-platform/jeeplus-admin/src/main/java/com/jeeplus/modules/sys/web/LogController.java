/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Log;
import com.jeeplus.modules.sys.service.LogService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志Controller
 * @author jeeplus
 * @version 2016-6-2
 */
@RestController
@RequestMapping("/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;

	@RequiresPermissions("sys:log:list")
	@GetMapping("list")
	public AjaxJson data(Log log, HttpServletRequest request, HttpServletResponse response) {
        Page<Log> page = logService.findPage(new Page<Log>(request, response), log);
		return AjaxJson.success().put("page", page);
	}

	@RequiresPermissions("user")
	@GetMapping("data/mine")
	public AjaxJson mine(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
		log.setCreateBy(UserUtils.getUser());
		Page<Log> page = logService.findPage(new Page<Log>(request, response), log);
		return AjaxJson.success().put("page", page);
	}


	/**
	 * 批量删除
	 */
	@RequiresPermissions("sys:log:del")
	@DeleteMapping("delete")
	public AjaxJson deleteAll(String ids) {
		logService.batchDelete(ids.split(","));
		return AjaxJson.success("删除日志成功！");
	}

	/**
	 * 批量删除
	 */
	@RequiresPermissions("sys:log:del")
	@DeleteMapping("empty")
	public AjaxJson empty() {
		AjaxJson j = new AjaxJson();
		logService.empty();
		return AjaxJson.success("清空日志成功!");
	}
}
