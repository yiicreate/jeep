package com.jeeplus.modules.committee.listener;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.websocket.service.system.SystemInfoSocketHandler;
import com.jeeplus.modules.oa.entity.OaNotify;
import com.jeeplus.modules.oa.service.OaNotifyService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class PublishNoticeExecutionListener implements ExecutionListener {

    public SystemInfoSocketHandler systemInfoSocketHandler() {
        return new SystemInfoSocketHandler();
    }


    /**
     * @desc 是否公开发布消息
     * @param delegateExecution
     * @return void
     * @author tangxin
     * @date 2021/3/24
     */
    public void notify(DelegateExecution delegateExecution) {
        if("奖励审核".equals(delegateExecution.getCurrentFlowElement().getName()) &&
                "end".equals(delegateExecution.getEventName())){
            Map vars = delegateExecution.getVariables();
            if(vars.containsKey("open") && "true".equals(vars.get("open"))){
                StringBuilder title = new StringBuilder("奖励公告");
                StringBuilder detail = new StringBuilder();
                detail.append("流程标题:").append(vars.get("title")).append("\n")
                        .append("奖励人员:").append(UserUtils.get(vars.get("reward_man").toString()).getName()).append("\n")
                        .append("奖励原因:").append(vars.get("reason")).append("\n")
                        .append("奖励内容:").append(vars.get("content")).append("\n");

                sendOaNotice(title.toString(),detail.toString(),null,"2");
            }
        }

        if("处罚审核".equals(delegateExecution.getCurrentFlowElement().getName()) &&
                "end".equals(delegateExecution.getEventName())){
            Map vars = delegateExecution.getVariables();
            if(vars.containsKey("open") && "true".equals(vars.get("open"))){
                StringBuilder title = new StringBuilder("处罚公告");
                StringBuilder detail = new StringBuilder();
                detail.append("流程标题:").append(vars.get("title")).append("\n")
                        .append("处罚人员:").append(UserUtils.get(vars.get("punish_man").toString()).getName()).append("\n")
                        .append("处罚原因:").append(vars.get("reason")).append("\n")
                        .append("处罚内容:").append(vars.get("content")).append("\n");
                sendOaNotice(title.toString(),detail.toString(),null,"5");
            }
        }
    }

    public void sendOaNotice(String title,String detail,String receiverIds,String type){
        List<String> userIds = new ArrayList<>();
        if(StringUtils.isEmpty(receiverIds)){
            userIds = ((UserService)SpringContextHolder.getBean("userService"))
                    .findAllList(new User()).stream().map(User::getId).collect(Collectors.toList());
            receiverIds = String.join(",",userIds);
        } else {
            userIds = Arrays.asList(receiverIds.split(","));
        }
        OaNotify oaNotify = new OaNotify();
        oaNotify.setType(type);
        oaNotify.setTitle(title);
        oaNotify.setContent(detail);
        oaNotify.setStatus("1");
        oaNotify.setOaNotifyRecordIds(receiverIds);
        ((OaNotifyService)SpringContextHolder.getBean("oaNotifyService"))
                .save(oaNotify);
        for(String userId:userIds){
            systemInfoSocketHandler().sendMessageToUser(UserUtils.get(userId).getLoginName(), "收到新的公告");
        }
    }

}
