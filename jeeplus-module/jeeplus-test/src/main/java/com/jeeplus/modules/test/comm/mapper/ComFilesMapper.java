/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.comm.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.test.comm.entity.ComFiles;

import java.util.List;

/**
 * 公共MAPPER接口
 * @author lh
 * @version 2021-03-12
 */
@Mapper
@Repository
public interface ComFilesMapper extends BaseMapper<ComFiles> {
    public List<ComFiles> findListBySourceAOwnerId(ComFiles comFiles);

    public int deleteBySource(ComFiles comFiles);
}