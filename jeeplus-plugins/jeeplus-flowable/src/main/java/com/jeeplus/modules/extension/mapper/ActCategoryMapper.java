/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.TreeMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.extension.entity.ActCategory;

/**
 * 流程分类MAPPER接口
 * @author 刘高峰
 * @version 2019-10-09
 */
@Mapper
@Repository
public interface ActCategoryMapper extends TreeMapper<ActCategory> {
	
}