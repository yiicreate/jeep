/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.modules.database.datamodel.entity.DataParam;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据模型参数MAPPER接口
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
@Mapper
@Repository
public interface DataParamMapper extends BaseMapper<DataParam> {

    public int deleteByDataSetId(String id);

}
