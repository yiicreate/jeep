/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.web.app;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.websocket.service.system.SystemInfoSocketHandler;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.oa.entity.OaNotify;
import com.jeeplus.modules.oa.entity.OaNotifyRecord;
import com.jeeplus.modules.oa.service.OaNotifyService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通知通告Controller
 *
 * @author jeeplus
 * @version 2017-05-16
 */
@RestController
@RequestMapping("/app/notify/oaNotify")
public class AppOaNotifyController extends BaseController {

    @Autowired
    private OaNotifyService oaNotifyService;

    @ModelAttribute
    public OaNotify get(@RequestParam(required = false) String id) {
        OaNotify entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = oaNotifyService.get(id);
        }
        if (entity == null) {
            entity = new OaNotify();
        }
        return entity;
    }
    /**
     * 通告列表数据
     */
    @GetMapping("list")
    public AjaxJson data(OaNotify oaNotify, boolean isSelf, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (isSelf) {
            oaNotify.setSelf(true);
        }
        Page<OaNotify> page = oaNotifyService.findPage(new Page<OaNotify>(request, response), oaNotify);
        return AjaxJson.success().put("page", page);
    }

    /**
     * 查看，增加，编辑报告表单页面
     */
    @GetMapping("queryById")
    public AjaxJson queryById(OaNotify oaNotify, boolean isSelf, Model model) {
        if (StringUtils.isNotBlank(oaNotify.getId())) {
            if (isSelf) {
                oaNotifyService.updateReadFlag(oaNotify);
                oaNotify = oaNotifyService.get(oaNotify.getId());
            }
            oaNotify = oaNotifyService.getRecordList(oaNotify);
        }
        return AjaxJson.success().put("isSelf", isSelf).put("oaNotify", oaNotify);
    }

    @PostMapping("save")
    public AjaxJson save(OaNotify oaNotify, Model model, RedirectAttributes redirectAttributes) {
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(oaNotify);
        if (StringUtils.isNotBlank(errMsg)) {
            return AjaxJson.error(errMsg);
        }
        // 如果是修改，则状态为已发布，则不能再进行操作
        if (StringUtils.isNotBlank(oaNotify.getId())) {
            OaNotify e = oaNotifyService.get(oaNotify.getId());
            if ("1".equals(e.getStatus())) {
                return AjaxJson.error("已发布，不能操作！");
            }
        }
        oaNotifyService.save(oaNotify);
        if ("1".equals(oaNotify.getStatus())) {
            List<OaNotifyRecord> list = oaNotify.getOaNotifyRecordList();
            for (OaNotifyRecord o : list) {
                //发送通知到客户端
                ServletContext context = SpringContextHolder
                        .getBean(ServletContext.class);
                new SystemInfoSocketHandler().sendMessageToUser(UserUtils.get(o.getUser().getId()).getLoginName(), "收到一条新通知，请到我的通知查看！");
            }
        }
        return AjaxJson.success("保存通知'" + oaNotify.getTitle() + "'成功");
    }

    @DeleteMapping("delete")
    public AjaxJson delete(OaNotify oaNotify, RedirectAttributes redirectAttributes) {
        oaNotifyService.delete(oaNotify);
        return AjaxJson.success("删除通知成功");
    }


    /**
     * 获取我的通知数目
     */
    @GetMapping("self/data")
    public AjaxJson selfData(OaNotify oaNotify, boolean isSelf, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (isSelf) {
            oaNotify.setSelf(true);
        }
        Page<OaNotify> page = oaNotifyService.findPage(new Page<OaNotify>(request, response), oaNotify);
        return AjaxJson.success().put("page", page);
    }

}
