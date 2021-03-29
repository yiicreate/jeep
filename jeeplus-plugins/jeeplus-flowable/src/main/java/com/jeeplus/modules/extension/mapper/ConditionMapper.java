/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.extension.entity.Condition;

/**
 * 流程表达式MAPPER接口
 * @author liugf
 * @version 2019-09-29
 */
@Mapper
@Repository
public interface ConditionMapper extends BaseMapper<Condition> {
	
}