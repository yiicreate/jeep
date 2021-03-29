/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.echarts.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.modules.echarts.entity.Echarts;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图表组件MAPPER接口
 *
 * @author 刘高峰
 * @version 2018-08-13
 */
@Mapper
@Repository
public interface EchartsMapper extends BaseMapper<Echarts> {

}
