/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.modules.sys.entity.DictType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 数据字典MAPPER接口
 * @author lgf
 * @version 2017-01-16
 */
@Mapper
@Repository
public interface DictTypeMapper extends BaseMapper<DictType> {

    public List<DictType> getDict();
}
