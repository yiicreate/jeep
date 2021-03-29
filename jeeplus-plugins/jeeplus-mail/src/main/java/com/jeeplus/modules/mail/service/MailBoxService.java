/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.service;


import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.mail.entity.MailBox;
import com.jeeplus.modules.mail.mapper.MailBoxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 收件箱Service
 *
 * @author jeeplus
 * @version 2015-11-13
 */
@Service
@Transactional(readOnly = false)
public class MailBoxService extends CrudService<MailBoxMapper, MailBox> {

    @Autowired
    private MailBoxMapper mailBoxMapper;

    public MailBox get(String id) {
        return super.get(id);
    }

    public List<MailBox> findList(MailBox mailBox) {
        return super.findList(mailBox);
    }

    public Page<MailBox> findPage(Page<MailBox> page, MailBox mailBox) {
        return super.findPage(page, mailBox);
    }

    @Transactional(readOnly = false)
    public void save(MailBox mailBox) {
        super.save(mailBox);
    }

    @Transactional(readOnly = false)
    public void delete(MailBox mailBox) {
        super.delete(mailBox);
    }

    public int getCount(MailBox mailBox) {
        return mailBoxMapper.getCount(mailBox);
    }

}
