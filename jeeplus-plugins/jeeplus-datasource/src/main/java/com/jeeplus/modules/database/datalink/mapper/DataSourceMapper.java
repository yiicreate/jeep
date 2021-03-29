/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datalink.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.database.datalink.entity.DataSource;

/**
 * 数据库连接MAPPER接口
 *
 * @author 刘高峰
 * @version 2018-08-06
 */
@Mapper
@Repository
public interface DataSourceMapper extends BaseMapper<DataSource> {

}
