/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.modules.sys.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;;

/**
 * 区域MAPPER接口
 * @author jeeplus
 * @version 2017-05-16
 */
@Mapper
@Repository
public interface AreaMapper extends TreeMapper<Area> {
	
}
