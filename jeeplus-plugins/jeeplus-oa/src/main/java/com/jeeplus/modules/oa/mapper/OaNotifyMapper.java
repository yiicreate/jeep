/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.modules.oa.entity.OaNotify;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 通知通告MAPPER接口
 *
 * @author jeeplus
 * @version 2017-05-16
 */
@Mapper
@Repository
public interface OaNotifyMapper extends BaseMapper<OaNotify> {

    /**
     * 获取通知数目
     *
     * @param oaNotify
     * @return
     */
    public Long findCount(OaNotify oaNotify);

}