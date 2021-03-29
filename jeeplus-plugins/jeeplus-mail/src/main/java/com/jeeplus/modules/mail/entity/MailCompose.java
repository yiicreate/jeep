/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.entity;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.sys.service.UserService;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.Collections3;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;


/**
 * 发件箱Entity
 *
 * @author jeeplus
 * @version 2015-11-15
 */
public class MailCompose extends DataEntity<MailCompose> {

    private static final long serialVersionUID = 1L;
    private String status;        // 状态 0 草稿 1 已发送
    private User sender;        // 发送者
    private String receiverIds; //收信人ID
    private Date sendtime;        // 发送时间
    private Mail mail;        // 邮件id 父类

    public MailCompose() {
        super();
    }

    public MailCompose(String id) {
        super(id);
    }

    public MailCompose(Mail mail) {
        this.mail = mail;
    }

    @Length(min = 0, max = 45, message = "状态 0 草稿 1 已发送长度必须介于 0 和 45 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    @Length(min = 0, max = 64, message = "邮件id长度必须介于 0 和 64 之间")
    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }


    /**
     * 获取收件人用户ID
     *
     * @return
     */
    public String getReceiverIds() {
        return this.receiverIds;
    }

    /**
     * 设置收件人用户ID
     *
     * @return
     */
    public void setReceiverIds(String receiverIds) {
        this.receiverIds = receiverIds;

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
