/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.extension.mapper;

import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.extension.entity.FormDefinitionJson;

/**
 * 流程表单MAPPER接口
 * @author 刘高峰
 * @version 2020-02-02
 */
@Mapper
@Repository
public interface FormDefinitionJsonMapper extends BaseMapper<FormDefinitionJson> {


    public int deleteByFormDefinitionId(FormDefinitionJson formDefinitionJson);

    public int getMaxVersion(FormDefinitionJson formDefinitionJson);

    public void updatePrimary(FormDefinitionJson formDefinitionJson);
}
