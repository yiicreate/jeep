/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.calendar.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.modules.calendar.entity.MyCalendar;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 日历MAPPER接口
 *
 * @author liugf
 * @version 2016-04-19
 */
@Mapper
@Repository
public interface MyCalendarMapper extends BaseMapper<MyCalendar> {

}