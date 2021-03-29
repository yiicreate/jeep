/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.service;

import java.util.List;

import com.jeeplus.modules.mail.entity.MailCompose;
import com.jeeplus.modules.mail.mapper.MailComposeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 发件箱Service
 *
 * @author jeeplus
 * @version 2015-11-13
 */
@Service
@Transactional(readOnly = true)
public class MailComposeService extends CrudService<MailComposeMapper, MailCompose> {
    @Autowired
    private MailComposeMapper mailComposeMapper;

    public MailCompose get(String id) {
        return super.get(id);
    }

    public List<MailCompose> findList(MailCompose mailCompose) {
        return super.findList(mailCompose);
    }

    public Page<MailCompose> findPage(Page<MailCompose> page, MailCompose mailCompose) {
        return super.findPage(page, mailCompose);
    }

    @Transactional(readOnly = false)
    public void save(MailCompose mailCompose) {
        super.save(mailCompose);
    }

    @Transactional(readOnly = false)
    public void delete(MailCompose mailCompose) {
        super.delete(mailCompose);
    }

    public int getCount(MailCompose mailCompose) {
        return mailComposeMapper.getCount(mailCompose);
    }

}
