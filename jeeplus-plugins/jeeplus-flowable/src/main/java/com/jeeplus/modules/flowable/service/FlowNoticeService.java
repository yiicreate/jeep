package com.jeeplus.modules.flowable.service;


import com.jeeplus.common.websocket.service.system.SystemInfoSocketHandler;
import com.jeeplus.core.service.BaseService;
import com.jeeplus.ding.io.DingWorkIo;
import com.jeeplus.ding.utils.DingUtil;
import com.jeeplus.ding.utils.TimeUtil;
import com.jeeplus.modules.flowable.entity.Flow;
import com.jeeplus.modules.mail.entity.Mail;
import com.jeeplus.modules.mail.entity.MailBox;
import com.jeeplus.modules.mail.service.MailBoxService;
import com.jeeplus.modules.mail.service.MailService;
import com.jeeplus.modules.oa.entity.OaNotify;
import com.jeeplus.modules.oa.service.OaNotifyService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

import static com.jeeplus.ding.config.NoticeUrl.URL_HY;


/**
 * 流程消息通知service
 *
 * @author tangxin
 * @version 2016-11-03
 */
@Service
@Transactional(readOnly = false)
public class FlowNoticeService extends BaseService {

    @Autowired
    private MailBoxService mailBoxService;
    @Autowired
    private MailService mailService;
    @Autowired
    private OaNotifyService oaNotifyService;
    @Autowired
    private DingUtil dingUtil;




    public SystemInfoSocketHandler systemInfoSocketHandler() {
        return new SystemInfoSocketHandler();
    }




    public void sendNotice(Flow flow, Map vars, Task task){
        //是否发送消息
        if(!vars.containsKey("sendNotice")) return;
        Boolean sendNotice = (boolean) vars.get("sendNotice");
        if(!sendNotice) return;
        //消息头
        StringBuilder title = new StringBuilder("新任务待办\"")
                                .append(task.getName()).append("\"!");
        //消息详情
        StringBuilder mailDetail = new StringBuilder();
        mailDetail.append("<p>流程标题:").append(vars.get("title")).append("</p>")
                .append("<p>任务名称:").append(task.getName()).append("</p>")
//                .append("上个任务节点:").append(flow.get).append("\n")
//                .append("任务提交人:").append(flow.get).append("\n")
                .append("<p>任务状态:").append(flow.getComment().getStatus()).append("</p>")
                .append("<p>审批信息:").append(flow.getComment().getMessage()).append("</p>");

        StringBuilder dingDetail = new StringBuilder();
        dingDetail.append("流程标题:").append(vars.get("title")).append("\n")
                .append("任务名称:").append(task.getName()).append("\n")
//                .append("上个任务节点:").append(flow.get).append("\n")
//                .append("任务提交人:").append(flow.get).append("\n")
                .append("任务状态:").append(flow.getComment().getStatus()).append("\n")
                .append("审批信息:").append(flow.getComment().getMessage()).append("\n");
        //通知人
        String assignee = flow.getAssignee();
//        sendMail(title.toString(),mailDetail.toString(),assignee);
        sendDingNotice(title.toString(),dingDetail.toString(),assignee);
        sendOaNotice(title.toString(),dingDetail.toString(),assignee);

    }

    public void sendDingNotice(String title,String detail,String receiverIds){
        String[] userIdArray = receiverIds.split(",");
        for(String userId:userIdArray){
            DingWorkIo workIo = new DingWorkIo(UserUtils.get(userId).getUserId(),"待办任务通知");
            workIo.setVoList(title,detail);
            workIo.setVoList("发布时间", TimeUtil.getCurrent());
            workIo.setUrl(URL_HY);
            dingUtil.sendWorkCord(workIo);
        }

    }

    public void sendOaNotice(String title,String detail,String receiverIds){
        String[] userIdArray = receiverIds.split(",");
        OaNotify oaNotify = new OaNotify();
        oaNotify.setType("4");
        oaNotify.setTitle(title);
        oaNotify.setContent(detail);
        oaNotify.setStatus("1");
        oaNotify.setOaNotifyRecordIds(receiverIds);
        oaNotifyService.save(oaNotify);
        for(String userId:userIdArray){
            systemInfoSocketHandler().sendMessageToUser(UserUtils.get(userId).getLoginName(), "收到新的通知");
        }
    }



    public void sendMail(String title,String detail,String receiverIds){
        String[] userIdArray = receiverIds.split(",");
        Mail mail = new Mail();
        mail.setTitle(title);
        mail.setContent(detail);
        mailService.saveOnlyMain(mail);

        for(String id:userIdArray){
            MailBox mailBox = new MailBox();
            mailBox.setReadstatus("0");
            mailBox.setReceiverIds(receiverIds);
            mailBox.setReceiver(UserUtils.get(id));
            mailBox.setSender(UserUtils.getUser());
            mailBox.setMail(mail);
            mailBox.setSendtime(new Date());
            mailBoxService.save(mailBox);
            //发送通知到客户端
            systemInfoSocketHandler().sendMessageToUser(UserUtils.get(id).getLoginName(), "收到一封新邮件");
        }
    }




}
