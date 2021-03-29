/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web.app;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 区域Controller
 *
 * @author jeeplus
 * @version 2016-5-15
 */
@RestController
@RequestMapping("/app/sys/area")
public class AppAreaController extends BaseController {

    @Autowired
    private AreaService areaService;


    /**
     * 获取区域JSON数据。
     *
     * @param extId    排除的ID
     * @return
     */
    @GetMapping("treeData")
    public AjaxJson treeData(@RequestParam(required = false) String extId) {
        List<Area> list = areaService.findAll();
        List rootTree = getAreaTree(list, extId);
        return AjaxJson.success().put("treeData", rootTree);
    }

    private List<Area> getAreaTree(List<Area> list, String extId) {
        List<Area> areas = Lists.newArrayList();
        List<Area> rootTrees = areaService.getChildren("0");
        for (Area root : rootTrees) {
            if (StringUtils.isBlank(extId) ||  !extId.equals(root.getId())) {
                areas.add(getChildOfTree(root, list, extId));
            }
        }
        return areas;
    }

    private Area getChildOfTree(Area area, List<Area> areaList, String extId) {
        area.setChildren(null);
        for (Area child : areaList) {
            if (StringUtils.isBlank(extId) ||  (!extId.equals(child.getId()) && child.getParentIds().indexOf("," + extId + ",") == -1)) {
                if (child.getParentId().equals(area.getId())) {
                    if(area.getChildren() == null){
                        area.setChildren(Lists.newArrayList());
                    }
                    area.getChildren().add(getChildOfTree(child, areaList, extId));
                }
            }
        }
        return area;
    }

}
