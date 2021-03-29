/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.digitals.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.digitals.entity.News;

/**
 * 数字化办公-新闻宣传MAPPER接口
 * @author lh
 * @version 2021-03-12
 */
@Mapper
@Repository
public interface NewsMapper extends BaseMapper<News> {

}