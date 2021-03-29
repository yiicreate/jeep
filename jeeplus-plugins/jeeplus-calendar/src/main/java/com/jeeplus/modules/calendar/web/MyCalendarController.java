/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.calendar.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.calendar.entity.MyCalendar;
import com.jeeplus.modules.calendar.service.MyCalendarService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 日历Controller
 *
 * @author liugf
 * @version 2016-04-19
 */
@RestController
@RequestMapping("/myCalendar")
public class MyCalendarController extends BaseController {

    @Autowired
    private MyCalendarService myCalendarService;

    @ModelAttribute
    public MyCalendar get(@RequestParam(required = false) String id) {
        MyCalendar entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = myCalendarService.get(id);
        }
        if (entity == null) {
            entity = new MyCalendar();
        }
        return entity;
    }

    /**
     * 查看，增加，编辑日历信息表单页面
     */
    @GetMapping("queryById")
    public AjaxJson queryById(MyCalendar myCalendar) {
        return AjaxJson.success().put("myCalendar", myCalendar);
    }

    @RequiresPermissions("user")
    @GetMapping("findList")
    protected AjaxJson doPost(MyCalendar myCalendar){
        myCalendar.setUser(UserUtils.getUser());
        List<MyCalendar> list = myCalendarService.findList(myCalendar);
        return AjaxJson.success().put("list", list);
    }


    /**
     * 编辑日历
     *
     * @throws Exception
     */
    @RequiresPermissions("user")
    @PostMapping("save")
    public AjaxJson save(MyCalendar myCalendar) throws Exception {
        myCalendar.setUser(UserUtils.getUser());
        myCalendarService.save(myCalendar);//保存
        return AjaxJson.success("保存成功!");
    }

    /**
     * 删除日历
     */
    @RequiresPermissions("user")
    @DeleteMapping("del")
    public AjaxJson del(MyCalendar myCalendar) {
        myCalendarService.delete(myCalendar);
        return AjaxJson.success("删除成功!");

    }

    /**
     * 縮放日歷
     */
    @RequiresPermissions("user")
    @PostMapping("resize")
    public AjaxJson resize(MyCalendar myCalendar, HttpServletRequest request,
                           HttpServletResponse response, Model model) {
        Integer daydiff = Integer.parseInt(request.getParameter("daydiff")) * 24 * 60 * 60;
        Integer minudiff = Integer.parseInt(request.getParameter("minudiff")) / 1000;
        Date start = myCalendar.getStart();
        long lstart = start.getTime() / 1000;
        Date end = myCalendar.getEnd();
        Integer difftime = daydiff + minudiff;
        if (end == null) {
            myCalendar.setEnd(DateUtils.parseDate(DateUtils.long2string(lstart + difftime)));
            myCalendar.setUser(UserUtils.getUser());
            myCalendarService.save(myCalendar);
        } else {
            long lend = end.getTime() / 1000;
            myCalendar.setEnd(DateUtils.parseDate(DateUtils.long2string(lend + difftime)));
            myCalendar.setUser(UserUtils.getUser());
            myCalendarService.save(myCalendar);
        }
        return AjaxJson.success("保存成功！");
    }

    /**
     * 拖拽日历
     */
    @RequiresPermissions("user")
    @PostMapping("drag")
    public AjaxJson drag(MyCalendar myCalendar, HttpServletRequest request,
                         HttpServletResponse response, Model model) {
        Integer daydiff = Integer.parseInt(request.getParameter("daydiff")) * 24 * 60 * 60;
        Integer minudiff = Integer.parseInt(request.getParameter("minudiff")) / 1000;
        Date start = myCalendar.getStart();
        long lstart = start.getTime() / 1000;
        Date end = myCalendar.getEnd();
        Integer difftime = daydiff + minudiff;
        if (end == null) {
            myCalendar.setStart(DateUtils.parseDate(DateUtils.long2string(lstart + difftime)));
            myCalendar.setUser(UserUtils.getUser());
            myCalendarService.save(myCalendar);
        } else {
            long lend = end.getTime() / 1000;
            myCalendar.setStart(DateUtils.parseDate(DateUtils.long2string(lstart + difftime)));
            myCalendar.setEnd(DateUtils.parseDate(DateUtils.long2string(lend + difftime)));
            myCalendar.setUser(UserUtils.getUser());
            myCalendarService.save(myCalendar);
        }
        return AjaxJson.success("保存成功");
    }

}
