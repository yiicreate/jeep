/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.calendar.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.calendar.entity.MyCalendar;
import com.jeeplus.modules.calendar.mapper.MyCalendarMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 日历Service
 *
 * @author liugf
 * @version 2016-04-19
 */
@Service
@Transactional(readOnly = true)
public class MyCalendarService extends CrudService<MyCalendarMapper, MyCalendar> {

    public MyCalendar get(String id) {
        return super.get(id);
    }

    public List<MyCalendar> findList(MyCalendar myCalendar) {
        return super.findList(myCalendar);
    }

    public Page<MyCalendar> findPage(Page<MyCalendar> page, MyCalendar myCalendar) {
        return super.findPage(page, myCalendar);
    }

    @Transactional(readOnly = false)
    public void save(MyCalendar myCalendar) {
        super.save(myCalendar);
    }

    @Transactional(readOnly = false)
    public void delete(MyCalendar myCalendar) {
        super.delete(myCalendar);
    }

}