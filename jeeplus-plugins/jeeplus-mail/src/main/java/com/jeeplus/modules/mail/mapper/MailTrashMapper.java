/**
 * Copyright © 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.mapper;

import com.jeeplus.modules.mail.entity.MailBox;
import org.springframework.stereotype.Repository;
import com.jeeplus.core.persistence.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.jeeplus.modules.mail.entity.MailTrash;

/**
 * 邮件MAPPER接口
 * @author 刘高峰
 * @version 2020-08-28
 */
@Mapper
@Repository
public interface MailTrashMapper extends BaseMapper<MailTrash> {
    public int getCount(MailTrash entity);
}
