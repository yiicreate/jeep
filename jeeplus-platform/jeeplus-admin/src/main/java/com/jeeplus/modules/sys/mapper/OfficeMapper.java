/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import java.util.List;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.modules.sys.entity.Office;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;;

/**
 * 机构MAPPER接口
 * @author jeeplus
 * @version 2017-05-16
 */
@Mapper
@Repository
public interface OfficeMapper extends TreeMapper<Office> {
	
	public Office getByCode(String code);

}
