/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.activiti.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.activiti.entity.TestActivitiLeave;

/**
 * 请假申请MAPPER接口
 * @author liugf
 * @version 2021-01-05
 */
@Mapper
@Repository
public interface TestActivitiLeaveMapper extends BaseMapper<TestActivitiLeave> {
	
}