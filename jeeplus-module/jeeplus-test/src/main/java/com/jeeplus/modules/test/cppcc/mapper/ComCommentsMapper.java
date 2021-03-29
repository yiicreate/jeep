/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.cppcc.mapper;

import com.jeeplus.modules.test.cppcc.entity.ComComments;
import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 政协通讯评论详情MAPPER接口
 * @author lc
 * @version 2021-03-15
 */
@Mapper
@Repository
public interface ComCommentsMapper extends BaseMapper<ComComments> {
	
}