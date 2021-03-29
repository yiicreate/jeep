package com.jeeplus.modules.sys.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.plugin.PluginPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 插件管理Controller
 * @author jeeplus
 * @version 2019-5-15
 */
@RestController
@RequestMapping("/sys/plugin")
public class PluginController extends BaseController {
    @Autowired
    PluginPool pluginPool;


    @GetMapping("list")
    public AjaxJson data(Model model) {
        return AjaxJson.success().put("rows", pluginPool.getPlugins());
    }



}
