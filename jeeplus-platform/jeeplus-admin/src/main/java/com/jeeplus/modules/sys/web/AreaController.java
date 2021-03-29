/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.service.AreaService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域Controller
 *
 * @author jeeplus
 * @version 2016-5-15
 */
@RestController
@RequestMapping("/sys/area")
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;

    @ModelAttribute("area")
    public Area get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return areaService.get(id);
        } else {
            return new Area();
        }
    }

    @RequiresPermissions("sys:area:list")
    @GetMapping("list")
    public AjaxJson list(Area area) {
        return AjaxJson.success().put("list", areaService.findAll());
    }

    @RequiresPermissions(value = {"sys:area:view", "sys:area:add", "sys:area:edit"}, logical = Logical.OR)
    @GetMapping("queryById")
    public AjaxJson queryById(Area area) {
        return AjaxJson.success().put("area", area);
    }

    @RequiresPermissions(value = {"sys:area:add", "sys:area:edit"}, logical = Logical.OR)
    @PostMapping("save")
    public AjaxJson save(Area area, Model model) {
        if (jeePlusProperites.isDemoMode()) {
            return AjaxJson.error("演示模式，不允许操作！");
        }

        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(area);
        if (StringUtils.isNotBlank(errMsg)) {
            return AjaxJson.error(errMsg);
        }
        areaService.save(area);
        return AjaxJson.success("保存成功！").put("area", area);
    }

    @RequiresPermissions("sys:area:del")
    @DeleteMapping("delete")
    public AjaxJson delete(Area area) {
        AjaxJson j = new AjaxJson();
        if (jeePlusProperites.isDemoMode()) {
            return AjaxJson.error("演示模式，不允许操作！");
        }
        areaService.delete(area);
        return AjaxJson.success("删除区域成功！");
    }


    /**
     * 获取区域JSON数据。
     *
     * @param extId    排除的ID
     * @return
     */
    @RequiresPermissions("user")
    @GetMapping("treeData")
    public AjaxJson treeData(@RequestParam(required = false) String extId) {
        List<Area> list = areaService.findAll();
        List rootTree = areaService.formatListToTree (new Area ("0"),list, extId );
        return AjaxJson.success().put("treeData", rootTree);
    }

}
