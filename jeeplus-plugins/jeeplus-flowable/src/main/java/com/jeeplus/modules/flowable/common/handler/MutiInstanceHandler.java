package com.jeeplus.modules.flowable.common.handler;

import com.jeeplus.common.utils.Collections3;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.extension.entity.FlowAssignee;
import com.jeeplus.modules.extension.entity.TaskDefExtension;
import com.jeeplus.modules.extension.service.TaskDefExtensionService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Post;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.PostService;
import com.jeeplus.modules.sys.service.RoleService;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MutiInstanceHandler {

    /**
     * 获得当前节点的处理者列表
     * @param execution 当前执行实例
     * @return 处理者列表
     */
    public List<String> getList(DelegateExecution execution) {
        String taskDefId = execution.getCurrentFlowElement ().getId ();
        Process process = SpringContextHolder.getBean (RepositoryService.class).getBpmnModel (execution.getProcessDefinitionId ()).getMainProcess ();
        TaskDefExtension taskDefExtension = new TaskDefExtension ();
        taskDefExtension.setTaskDefId (taskDefId);
        taskDefExtension.setProcessDefId (process.getId ());
        List<TaskDefExtension> list = SpringContextHolder.getBean (TaskDefExtensionService.class).findList (taskDefExtension);
        HashSet<String> candidateUserIds = new HashSet<> ();
        if (list.size () > 0) {
            taskDefExtension = list.get (0);
            List<FlowAssignee> assigneeList = SpringContextHolder.getBean (TaskDefExtensionService.class).get (taskDefExtension.getId ()).getFlowAssigneeList ();
            for (FlowAssignee flowAssignee : assigneeList) {
                switch (flowAssignee.getType ()) {
                    case "user":
                        candidateUserIds.addAll (Arrays.asList (flowAssignee.getValue ().split (",")));
                        break;
                    case "post":
                        if (StringUtils.isNotBlank (flowAssignee.getValue ())) {
                            Post post = SpringContextHolder.getBean (PostService.class).get (flowAssignee.getValue ());
                            User user = new User ();
                            user.setPost (post);
                            List<User> userList = SpringContextHolder.getBean (UserService.class).findList (user);
                            candidateUserIds.addAll (Collections3.extractToList (userList, "id"));
                        }

                        break;
                    case "company":
                        if (StringUtils.isNotBlank (flowAssignee.getValue ())) {
                            Office office = SpringContextHolder.getBean (OfficeService.class).get (flowAssignee.getValue ());
                            User user = new User ();
                            user.setCompany (office);
                            List<User> userList = SpringContextHolder.getBean (UserService.class).findList (user);
                            candidateUserIds.addAll (Collections3.extractToList (userList, "id"));
                        }

                        break;
                    case "depart":
                        if (StringUtils.isNotBlank (flowAssignee.getValue ())) {
                            Office office = SpringContextHolder.getBean (OfficeService.class).get (flowAssignee.getValue ());
                            User user = new User ();
                            user.setOffice (office);
                            List<User> userList = SpringContextHolder.getBean (UserService.class).findList (user);
                            candidateUserIds.addAll (Collections3.extractToList (userList, "id"));
                        }

                        break;
                    case "role":
                        if (StringUtils.isNotBlank (flowAssignee.getValue ())) {
                            String[] roleIds = flowAssignee.getValue ().split (",");
                            for (String roleId : roleIds) {
                                Role role = SpringContextHolder.getBean (RoleService.class).get (roleId);
                                User user = new User (role);
                                List<User> userList = SpringContextHolder.getBean (UserService.class).findList (user);
                                candidateUserIds.addAll (Collections3.extractToList (userList, "id"));
                            }
                        }
                        break;
                    case "applyUserId":
                        candidateUserIds.add ("${applyUserId}");
                        break;
                    case "previousExecutor":
                        HistoricTaskInstance lastHisTask = SpringContextHolder.getBean (HistoryService.class).createHistoricTaskInstanceQuery ().processInstanceId (execution.getProcessInstanceId ()).finished ()
                                .includeProcessVariables ().orderByHistoricTaskInstanceEndTime ().desc ().list ().get (0);

                        candidateUserIds.add (lastHisTask.getAssignee ());
                        break;
                    case "currentUserId":
                        candidateUserIds.add (UserUtils.getUser ().getId ());
                        break;
                    case "sql":
                        Map userMap = SpringContextHolder.getBean (JdbcTemplate.class).queryForMap (flowAssignee.getValue ());
                        candidateUserIds.add (userMap.get ("id").toString ());
                        break;
                    case "custom":
                        //根据你的自定义标记，请自行实现
                        break;
                }
            }
        }

        return new ArrayList<> (candidateUserIds);

    }

    /**
     * 获取会签是否结束
     * @param execution 当前执行实例
     * @return 是否结束
     */
    public boolean getComplete(DelegateExecution execution) {
        Integer nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");
        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances");
        int agreeCount = 0, rejectCount = 0, abstainCount = 0;
        Map<String, Object> vars = execution.getVariables();
        for (String key : vars.keySet()) {
            //会签投票以SIGN_VOTE+TaskId标识
            //获得会签投票的统计结果
//            if (key.contains(FlowConst.SIGN_VOTE) && !key.equals(FlowConst.SIGN_VOTE_RESULT)) {
//                Integer value = (Integer) vars.get(key);
//                //统计同意、驳回、弃权票数
//                //省略代码若干......
//            }
        }
        //以下为一段简单的规则，可以按情况实现自己的会签规则
        if (!nrOfCompletedInstances.equals(nrOfInstances)) {
            //必须等所有的办理人都投票
            return false;
        } else {
            //会签全部完成时,使用默认规则结束
            if (rejectCount > 0) {
                //有反对票，则最终的会签结果为不通过
                //移除SIGN_VOTE+TaskId为标识的参数
//                removeSignVars(execution);
                //增加会签结果参数，以便之后流转使用
//                execution.setVariable(FlowConst.SIGN_VOTE_RESULT, false);
                //会签结束
                return true;
            } else {
                //没有反对票时，则最终的会签结果为通过
//                removeSignVars(execution);
//                execution.setVariable(FlowConst.SIGN_VOTE_RESULT, true);
                return true;
            }
        }
    }



}
