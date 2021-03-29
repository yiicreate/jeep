/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.websocket.service.system.SystemInfoSocketHandler;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.mail.entity.Mail;
import com.jeeplus.modules.mail.entity.MailBox;
import com.jeeplus.modules.mail.entity.MailCompose;
import com.jeeplus.modules.mail.entity.MailTrash;
import com.jeeplus.modules.mail.service.MailBoxService;
import com.jeeplus.modules.mail.service.MailComposeService;
import com.jeeplus.modules.mail.service.MailService;
import com.jeeplus.modules.mail.service.MailTrashService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 发件箱Controller
 *
 * @author jeeplus
 * @version 2015-11-13
 */
@RestController
@RequestMapping("/mailCompose")
public class MailComposeController extends BaseController {

    @Autowired
    private MailComposeService mailComposeService;

    @Autowired
    private MailBoxService mailBoxService;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailTrashService mailTrashService;

    public SystemInfoSocketHandler systemInfoSocketHandler() {
        return new SystemInfoSocketHandler();
    }

    @ModelAttribute
    public MailCompose get(@RequestParam(required = false) String id) {
        MailCompose entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = mailComposeService.get(id);
        }
        if (entity == null) {
            entity = new MailCompose();
        }
        return entity;
    }


    @GetMapping("list")
    public AjaxJson list(MailCompose mailCompose, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<MailCompose> page = mailComposeService.findPage(new Page<>(request, response), mailCompose);
        return AjaxJson.success().put("page", page);
    }

    @RequestMapping("queryById")//打开已发送信件
    public AjaxJson queryById(MailCompose mailCompose) {
        return AjaxJson.success().put("mailCompose", mailCompose);
    }


    @PostMapping("save")
    public AjaxJson save(MailCompose mailCompose, Model model, HttpServletRequest request, HttpServletResponse response) {
        mailService.saveOnlyMain(mailCompose.getMail());
        Date date = new Date(System.currentTimeMillis());
        mailCompose.setSender(UserUtils.getUser());
        mailCompose.setSendtime(date);

        //保存草稿
        if (mailCompose.getStatus().equals("0")) {
            mailComposeService.save(mailCompose);//0 显示在草稿箱，1 显示在已发送需同时保存到收信人的收件箱。

            return AjaxJson.success("保存成功!");
        }

        //发送邮件
        for (User receiver : mailCompose.getReceiverList()) {


            if (mailCompose.getStatus().equals("1"))//已发送，同时保存到收信人的收件箱
            {
                mailComposeService.save(mailCompose);//0 显示在草稿箱，1 显示在已发送需同时保存到收信人的收件箱。

                MailBox mailBox = new MailBox();
                mailBox.setReadstatus("0");
                mailBox.setReceiverIds(mailCompose.getReceiverIds());
                mailBox.setReceiver(receiver);
                mailBox.setSender(UserUtils.getUser());
                mailBox.setMail(mailCompose.getMail());
                mailBox.setSendtime(date);
                mailBoxService.save(mailBox);
                //发送通知到客户端
                systemInfoSocketHandler().sendMessageToUser(UserUtils.get(receiver.getId()).getLoginName(), "收到一封新邮件");
            }
        }


        return AjaxJson.success("发送成功!");
    }


    /**
     * 批量删除已发送
     */
    @DeleteMapping("delete")
    public AjaxJson deleteAllCompose(String ids, Model model) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            MailCompose mailCompose =  mailComposeService.get(id);
            MailTrash mailTrash = new MailTrash ();
            mailTrash.setMail (mailCompose.getMail ());
            mailTrash.setReceiverIds (mailCompose.getReceiverIds ());
            mailTrash.setSender (mailCompose.getSender ());
            mailTrash.setSendtime (mailCompose.getSendtime ());
            mailTrash.setStatus ( mailCompose.getStatus ());
            mailTrashService.save (mailTrash);
            mailComposeService.delete(mailCompose);
        }
        return AjaxJson.success("删除站内信成功");

    }


}
