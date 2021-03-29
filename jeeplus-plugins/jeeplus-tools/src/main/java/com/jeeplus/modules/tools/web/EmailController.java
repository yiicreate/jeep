/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tools.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.sys.entity.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeeplus.common.mail.MailSendUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.service.SysConfigService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送外部邮件
 *
 * @author lgf
 * @version 2016-01-07
 */
@RestController
@RequestMapping("/tools/email")
public class EmailController extends BaseController {

    @Autowired
    private SysConfigService systemConfigService;


    /**
     * 发送邮件
     */
    @RequestMapping("send")
    public AjaxJson send(String emailAddress, String title, String content) throws Exception {
        SysConfig config = systemConfigService.get("1");
        String[] addresses = emailAddress.split(";");
        String result = "";
        for (String address : addresses) {
            boolean isSuccess = MailSendUtils.sendEmail(config.getSmtp(), config.getPort(), config.getMailName(), config.getMailPassword(), address, title, content, "2");
            if (isSuccess) {
                result += address + ":<font color='green'>发送成功!</font><br/>";
            } else {
                result += address + ":<font color='red'>发送失败!</font><br/>";
            }
        }
        return AjaxJson.success(result);
    }

}
