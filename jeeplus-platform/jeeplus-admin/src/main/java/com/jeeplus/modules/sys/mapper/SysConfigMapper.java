/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.sys.entity.SysConfig;

/**
 * 系统配置MAPPER接口
 * @author 刘高峰
 * @version 2018-10-18
 */
@Mapper
@Repository
public interface SysConfigMapper extends BaseMapper<SysConfig> {

}
