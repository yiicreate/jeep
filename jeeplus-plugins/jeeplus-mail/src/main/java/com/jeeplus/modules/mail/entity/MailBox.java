/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.entity;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.Collections3;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.service.UserService;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;


/**
 * 收件箱Entity
 *
 * @author jeeplus
 * @version 2015-11-15
 */
public class MailBox extends DataEntity<MailBox> {

    private static final long serialVersionUID = 1L;
    private String readstatus;        // 状态 0 未读 1 已读
    private User sender;        // 发件人
    private String receiverIds; //全部收件人Id
    private User receiver;        // 当前收件人
    private Date sendtime;        // 发送时间
    private Mail mail;        // 邮件外键 父类

    public MailBox() {
        super();
    }

    public MailBox(String id) {
        super(id);
    }

    public MailBox(Mail mail) {
        this.mail = mail;
    }

    @Length(min = 0, max = 45, message = "状态 0 未读 1 已读长度必须介于 0 和 45 之间")
    public String getReadstatus() {
        return readstatus;
    }

    public void setReadstatus(String readstatus) {
        this.readstatus = readstatus;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }


    public String getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(String receiverIds) {
        this.receiverIds = receiverIds;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    @Length(min = 0, max = 64, message = "邮件外键长度必须介于 0 和 64 之间")
    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    /**
     * 获取收件人用户Name
     *
     * @return
     */
    public String getReceiverNames() {
        if(StringUtils.isBlank(receiverIds)){
            return "";
        }
        List receiverList = Lists.newArrayList();
        for (String id : StringUtils.split(receiverIds, ",")) {

            receiverList.add(SpringContextHolder.getBean(UserService.class).get(id));
        }
        return Collections3.extractToString(receiverList, "name", ",");
    }

    /**
     * 获取收件人用户
     *
     * @return
     */

    public List<User> getReceiverList() {
        List receiverList = Lists.newArrayList();
        if(StringUtils.isBlank(receiverIds)){
            return receiverList;
        }
        for (String id : StringUtils.split(receiverIds, ",")) {

            receiverList.add(new User(id));
        }
        return receiverList;
    }

}
