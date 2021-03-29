/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.extension.entity.FlowButton;

/**
 * 按钮设置MAPPER接口
 * @author 刘高峰
 * @version 2020-12-23
 */
@Mapper
@Repository
public interface FlowButtonMapper extends BaseMapper<FlowButton> {
	
}