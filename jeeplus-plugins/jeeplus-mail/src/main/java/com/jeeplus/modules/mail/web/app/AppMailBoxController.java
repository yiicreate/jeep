/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mail.web.app;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.mail.entity.MailBox;
import com.jeeplus.modules.mail.entity.MailCompose;
import com.jeeplus.modules.mail.entity.MailTrash;
import com.jeeplus.modules.mail.service.MailBoxService;
import com.jeeplus.modules.mail.service.MailComposeService;
import com.jeeplus.modules.mail.service.MailTrashService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 收件箱Controller
 *
 * @author jeeplus
 * @version 2015-11-13
 */
@RestController
@RequestMapping("/app/mailBox")
public class AppMailBoxController extends BaseController {

    @Autowired
    private MailComposeService mailComposeService;

    @Autowired
    private MailBoxService mailBoxService;

    @Autowired
    private MailTrashService mailTrashService;

    @ModelAttribute
    public MailBox get(@RequestParam(required = false) String id) {
        MailBox entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = mailBoxService.get(id);
        }
        if (entity == null) {
            entity = new MailBox();
        }
        return entity;
    }

    @GetMapping("list")
    public AjaxJson list(MailBox mailBox, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = AjaxJson.success();
        mailBox.setReceiver(UserUtils.getUser());
        Page<MailBox> page = mailBoxService.findPage(new Page<MailBox>(request, response), mailBox);
        j.put("page", page);

        return j;
    }

    // 查询信箱邮件状态
    @GetMapping("queryStatus")
    public AjaxJson queryStatus() {
        AjaxJson j = AjaxJson.success();
        //查询未读的条数
        MailBox serachBox = new MailBox();
        serachBox.setReadstatus("0");
        serachBox.setReceiver(UserUtils.getUser());
        j.put("noReadCount", mailBoxService.getCount(serachBox));

        //查询总条数
        MailBox serachBox2 = new MailBox();
        serachBox2.setReceiver(UserUtils.getUser());
        j.put("mailBoxCount", mailBoxService.getCount(serachBox2));

        //查询已发送条数
        MailCompose serachBox3 = new MailCompose();
        serachBox3.setSender(UserUtils.getUser());
        serachBox3.setStatus("1");//已发送
        j.put("mailComposeCount", mailComposeService.getCount(serachBox3));

        //查询草稿箱条数
        MailCompose serachBox4 = new MailCompose();
        serachBox4.setSender(UserUtils.getUser());
        serachBox4.setStatus("0");//草稿
        j.put("mailDraftCount", mailComposeService.getCount(serachBox4));

        //查询垃圾箱数
        MailTrash mailTrash = new MailTrash ();
        mailTrash.setCreateBy (UserUtils.getUser());
        j.put("mailTrashCount", mailTrashService.getCount(mailTrash));
        return j;
    }


    @GetMapping("queryById")
    public AjaxJson queryById(MailBox mailBox, Model model) {
        AjaxJson j = AjaxJson.success();
        if (mailBox.getReadstatus().equals("0")) {//更改未读状态为已读状态
            mailBox.setReadstatus("1");//1表示已读
            mailBoxService.save(mailBox);
        }
        j.put("mailBox", mailBox);
        return j;
    }

    @PostMapping("save")
    public AjaxJson save(MailBox mailBox) {
        Date date = new Date(System.currentTimeMillis());
        mailBox.setSender(UserUtils.getUser());
        mailBox.setSendtime(date);
        mailBoxService.save(mailBox);
        return AjaxJson.success("保存站内信成功");
    }


    /**
     * 批量删除
     */
    @DeleteMapping("delete")
    public AjaxJson delete(String ids) {
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            MailBox mailBox = mailBoxService.get(id);
            MailTrash mailTrash = new MailTrash ();
            mailTrash.setMail (mailBox.getMail ());
            mailTrash.setReceiverIds (mailBox.getReceiverIds ());
            mailTrash.setSender (mailBox.getSender ());
            mailTrash.setSendtime (mailBox.getSendtime ());
            mailTrash.setStatus (String.valueOf (Integer.valueOf (mailBox.getReadstatus ())+2));
            mailTrashService.save (mailTrash);
            mailBoxService.delete(mailBox);
        }
        return AjaxJson.success("删除站内信成功!");
    }
}
