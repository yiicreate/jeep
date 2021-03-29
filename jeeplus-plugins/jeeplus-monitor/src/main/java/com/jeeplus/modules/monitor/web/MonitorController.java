package com.jeeplus.modules.monitor.web;


import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.monitor.server.ServerOS;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统监控Controller
 * @author liugf
 * @version 2019-12-01
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController extends BaseController {
	@GetMapping("/server/info")
	public AjaxJson info(Model model) throws Exception {

		return  AjaxJson.success().put("ServerOS", ServerOS.getInfo());
	}

}
