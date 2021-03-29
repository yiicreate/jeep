/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flowable.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.extension.entity.NodeSetting;
import com.jeeplus.modules.extension.service.NodeSettingService;
import com.jeeplus.modules.flowable.entity.Flow;
import com.jeeplus.modules.flowable.entity.TaskComment;
import com.jeeplus.modules.flowable.service.FlowNoticeService;
import com.jeeplus.modules.flowable.service.FlowTaskService;
import com.jeeplus.modules.flowable.vo.HisTaskVo;
import com.jeeplus.modules.flowable.vo.ProcessVo;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 流程个人任务相关Controller
 *
 * @author jeeplus
 * @version 2016-11-03
 */
@RestController
@RequestMapping("/flowable/task")
public class FlowableTaskController extends BaseController {

    @Autowired
    private FlowTaskService flowTaskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FlowNoticeService flowNoticeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private NodeSettingService nodeSettingService;



    @GetMapping("todo")
    public AjaxJson todoListData(Flow flow, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<ProcessVo> page = flowTaskService.todoList(new Page<ProcessVo>(request, response), flow);
        return AjaxJson.success().put("page", page);
    }

    /**
     * 获取已办任务
     *
     * @return
     */
    @GetMapping("historic")
    public AjaxJson historicListData(Flow flow, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<HisTaskVo> page = flowTaskService.historicList(new Page<HisTaskVo>(request, response), flow);
        return AjaxJson.success().put("page", page);
    }

    /**
     * 获取我的申请列表
     *
     * @return
     */
    @GetMapping("myApplyed")
    public AjaxJson myApplyedListData(Flow flow, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Page<ProcessVo> page = flowTaskService.getMyStartedProcIns(UserUtils.getUser(), new Page<ProcessVo>(request, response), flow);
        return AjaxJson.success().put("page", page);
    }


    /**
     * 获取任务流转历史列表
     *
     */
    @GetMapping("historicTaskList")
    public AjaxJson historicTaskList(Flow flow) throws Exception {
        List<Flow> historicTaskList = flowTaskService.historicTaskList (flow.getProcInsId());
        return AjaxJson.success().put("historicTaskList", historicTaskList);
    }


    /**
     * 获取流程表单
     */
    @GetMapping("getTaskDef")
    public AjaxJson getTaskDef(Flow flow) {
        // 获取流程XML上的表单KEY
        String formKey = flowTaskService.getFormKey(flow.getProcDefId(), flow.getTaskDefKey());

        NodeSetting typeNode = nodeSettingService.queryByKey (flow.getProcDefKey(), flow.getTaskDefKey(), "formType");
        NodeSetting ReadOnlyNode = nodeSettingService.queryByKey (flow.getProcDefKey(), flow.getTaskDefKey(), "formReadOnly");
        String formType = "1";
        boolean formReadOnly = false;
        if(typeNode != null){
           formType = typeNode.getValue ();
           formReadOnly = "true".equals(ReadOnlyNode.getValue ());
        }else{
           if(StringUtils.isBlank(formKey)){
               formType = "1";
           }else if(formKey.indexOf("/")>=0 || formKey.length() != 32){
               formType = "2";
           }
        }

        // 获取流程实例对象
        if (flow.getProcInsId() != null) {
            if (flowTaskService.getProcIns(flow.getProcInsId()) != null) {
                flow.setProcIns(flowTaskService.getProcIns(flow.getProcInsId()));
            } else {
                flow.setFinishedProcIns(flowTaskService.getFinishedProcIns(flow.getProcInsId()));
            }
        }

        flow.setFormUrl(formKey);
        flow.setFormReadOnly(formReadOnly);
        flow.setFormType(formType);
        return AjaxJson.success().put("flow", flow);
    }



    /**
     * 启动流程
     */
    @PostMapping("start")
    public AjaxJson start(Flow flow) throws Exception {
        String procInsId = flowTaskService.startProcess(flow.getProcDefKey(), flow.getBusinessTable(), flow.getBusinessId(), flow.getTitle());

        //指定下一步处理人
        if(StringUtils.isNotBlank(flow.getAssignee ())){
            Task task = taskService.createTaskQuery().processInstanceId(procInsId).active().singleResult();
            if(task != null){
                taskService.setAssignee(task.getId(), flow.getAssignee ());
            }
        }
        return AjaxJson.success("启动成功!").put("procInsId", procInsId);
    }

    /**
     * 签收任务
     */
    @PostMapping("claim")
    public AjaxJson claim(Flow flow) {
        String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId());
        flowTaskService.claim(flow.getTaskId(), userId);
        return AjaxJson.success("签收成功!");
    }

    /**
     * 完成任务
     */
    @PostMapping( value = "complete")
    public AjaxJson complete(Flow flow) {
        flowTaskService.complete(flow, flow.getVars().getVariableMap());
        return AjaxJson.success("完成任务!");
    }

    /**
     * 读取流程历史数据，用于渲染流程图
     */
    @GetMapping("getFlowChart")
    public Map getFlowChart(String processInstanceId) throws Exception {
        return flowTaskService.getDiagram(processInstanceId);
    }

    /**
     * 删除任务
     *
     * @param taskId 流程实例ID
     * @param reason 删除原因
     */
    @DeleteMapping("deleteTask")
    public AjaxJson deleteTask(String taskId, String reason) {
        if (StringUtils.isBlank(reason)) {
            return AjaxJson.error("请填写删除原因");
        } else {
            flowTaskService.deleteTask(taskId, reason);
            return AjaxJson.success("删除任务成功，任务ID=" + taskId);
        }
    }

    /**
     * 加签
     */
    @PostMapping("addSignTask")
    public AjaxJson addSignTask(String taskId, String userIds, String comment, Boolean flag) throws Exception {
        flowTaskService.addSignTask (taskId, Arrays.asList (userIds.split (",")), comment, flag);
        return AjaxJson.success ("加签成功!");
    };


    /**
     * 审批
     *
     * @param flow
     */
    @PostMapping("audit")
    public AjaxJson auditTask(HttpServletRequest request, Flow flow) {
        Map<String, Object> vars = Maps.newHashMap();
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            if(entry.getKey().startsWith("vars.")){
                String key = entry.getKey().substring(5);
                String value = entry.getValue()[0];
                if("true".equals(value) || "false".equals(value)){
                    vars.put(key, Boolean.valueOf(value).booleanValue());
                }else{
                    vars.put(key, value);
                }
            }
        }

        flowTaskService.auditSave(flow, vars);
        //指定下一步处理人
        if(StringUtils.isNotBlank(flow.getAssignee ())){
           Task task = taskService.createTaskQuery().processInstanceId(flow.getProcInsId()).active().singleResult();
           if(task != null){
               taskService.setAssignee(task.getId(), flow.getAssignee ());
           }
            flowNoticeService.sendNotice(flow, vars,task);
        }
        return AjaxJson.success("提交成功").put("procInsId", flow.getProcInsId ());
    }

    /**
     * 取回已经执行的任务，只有在下一任务节点未执行或者未签收时才能取回
     */
    @PostMapping("callback")
    public AjaxJson callback(@Param("preTaskId") String preTaskId,
                             @Param("currentTaskId") String currentTaskId,
                             @Param("processInstanceId") String processInstanceId,
                             @Param("preTaskDefKey") String preTaskDefKey,
                             @Param("currentTaskDefKey") String currentTaskDefKey) {
        try {
            // 取得流程实例
            ProcessInstance instance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (instance == null) {
                return AjaxJson.error("流程已经结束");
            }

            //在已办任务列表中清除该任务信息
            historyService.deleteHistoricTaskInstance(preTaskId);

            List currTasks = Lists.newArrayList();
            currTasks.add(currentTaskDefKey);
            //回退到上一节点
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(instance.getId())
                    .moveActivityIdsToSingleActivityId(currTasks, preTaskDefKey).changeState();
            historyService.deleteHistoricTaskInstance(currentTaskId);
            return AjaxJson.success("取回成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxJson.error("流程取回失败，未知错误.");
        }
    }



    /**
     * 委托任务
     *
     * @param taskId 任务ID
     */
    @PostMapping("delegate")
    public AjaxJson delegate(String taskId, String userId) {
        if (StringUtils.isBlank(taskId) || StringUtils.isBlank(userId)) {
            return AjaxJson.error("参数异常");
        }
        taskService.setOwner (taskId, UserUtils.getUser ().getId ());// 委托人为任务的拥有者
        taskService.delegateTask(taskId, userId);
        return AjaxJson.success("委托成功");
    }

    /**
     * 取消签收任务
     *
     * @param taskId 任务ID
     */
    @PostMapping("unclaim")
    public AjaxJson unclaim(String taskId) {
        taskService.unclaim(taskId);
        return AjaxJson.success("取消签收成功");
    }

    /**
     * 转派任务
     *
     * @param taskId   任务ID
     * @param userId 接收人
     */
    @PostMapping("transfer")
    public AjaxJson transferTask(String taskId, String userId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(taskId)) {
            return AjaxJson.error("转派失败, 参数异常");
        }
        // 设置当前流程任务办理人
        Authentication.setAuthenticatedUserId(UserUtils.getUser().getId());
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.setAssignee(taskId, userId);
        return AjaxJson.success("转派成功!");
    }

    /**
     * 获取可退回任务节点
     * @param taskId
     * @return
     */
    @PostMapping(value = "/backNodes")
    public AjaxJson backNodes(@RequestParam String taskId) {
        List<Flow> nodes = flowTaskService.getBackNodes(taskId);
        return AjaxJson.success ().put ("backNodes", nodes);
    }

    /**
     * 驳回任务到指定节点
     */
    @PostMapping(value = "/back")
    public AjaxJson back(String backTaskDefKey, String taskId, TaskComment comment) {
        flowTaskService.backTask(backTaskDefKey, taskId, comment);
        return AjaxJson.success ("驳回成功!");
    }


}
