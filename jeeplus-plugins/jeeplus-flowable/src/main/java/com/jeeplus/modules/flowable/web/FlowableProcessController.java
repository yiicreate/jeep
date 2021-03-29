/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flowable.web;

import com.jeeplus.common.config.FlowDataSourceProcessEngineAutoConfiguration;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.flowable.service.FlowProcessService;
import com.jeeplus.modules.flowable.vo.ProcessStatus;
import com.jeeplus.modules.flowable.vo.ProcessVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 流程定义相关Controller
 *
 * @author jeeplus
 * @version 2016-11-03
 */
@RestController
@RequestMapping("/flowable/process")
public class FlowableProcessController extends BaseController {

    @Autowired
    private FlowProcessService flowProcessService;

    @Autowired
    private FlowDataSourceProcessEngineAutoConfiguration activitiConfig;

    /**
     * 流程定义列表
     */
    @GetMapping("list")
    public AjaxJson processListData(String category, HttpServletRequest request, HttpServletResponse response, Model model) {
        /*
         * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
         */
        Page<Map> page = flowProcessService.processList(new Page<Map>(request, response), category);
        return AjaxJson.success().put("page", page);
    }

    @GetMapping("exist")
    public AjaxJson exist(String key, HttpServletRequest request, HttpServletResponse response) {
        /*
         * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
         */
       ProcessDefinition processDefinition = flowProcessService.getProcessDefinitionByKey(key);
       if(processDefinition == null){
           return AjaxJson.success().put("exist","0");
       }else{
           return AjaxJson.success().put("exist","1");
       }
    }

    @GetMapping("runningData")
    public AjaxJson runningListData(String procInsId, String procDefKey, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Page<ProcessVo> page = flowProcessService.runningList(new Page<ProcessInstance>(request, response), procInsId, procDefKey);
        return AjaxJson.success().put("page", page);
    }


    /**
     * 已结束的实例
     */
    @GetMapping("historyListData")
    public AjaxJson historyListData(String procInsId, String procDefKey, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        Page<ProcessVo> page = flowProcessService.historyList(new Page<ProcessVo>(request, response), procInsId, procDefKey);
        return AjaxJson.success().put("page", page);
    }


    /**
     * 读取资源，通过部署ID
     *
     * @param response
     * @throws Exception
     */
    @GetMapping("resource/read")
    public void resourceRead(String procDefId, String proInsId, String resType, HttpServletResponse response) throws Exception {
        InputStream resourceAsStream = flowProcessService.resourceRead(procDefId, proInsId, resType);
        byte[] b = new byte[1024];
        int len = -1;
        if("xml".equals(resType)){
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            String fileName = flowProcessService.getProcessDefinition(procDefId).getResourceName();
            response.setHeader("Content-Disposition", URLEncoder.encode(fileName, "UTF8"));
        }
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    /**
     * 读取资源，通过部署ID
     *
     * @param response
     * @throws Exception
     */
    @GetMapping("getFlowChart")
    public Map getFlowChart(String processDefId,  HttpServletResponse response) throws Exception {
        return flowProcessService.getImageStream(processDefId);

    }

    /**
     * 设置流程分类
     */
    @RequiresPermissions("act:process:edit")
    @PostMapping("updateCategory")
    public AjaxJson updateCategory(String procDefId, String category, RedirectAttributes redirectAttributes) {
        flowProcessService.updateCategory(procDefId, category);
        return AjaxJson.success("流程分类设置成功!");
    }

    /**
     * 挂起、激活流程实例
     */
    @GetMapping("update/{state}")
    public AjaxJson updateState(@PathVariable("state") String state, String procDefId) {
        String message = flowProcessService.updateState(state, procDefId);
        return AjaxJson.success(message);
    }

    /**
     * 将部署的流程转换为模型
     *
     * @param procDefId
     * @return
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @PostMapping("convert/toModel")
    public AjaxJson convertToModel(String procDefId) throws UnsupportedEncodingException, XMLStreamException {
        org.flowable.engine.repository.Model modelData = flowProcessService.convertToModel(procDefId);
        return AjaxJson.success("转换模型成功，模型ID=" + modelData.getId());
    }


    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentIds 流程部署ID
     */
    @DeleteMapping("delete")
    public AjaxJson delete(String deploymentIds) {
        if(jeePlusProperites.isDemoMode()){
            return AjaxJson.error("演示模式，不允许操作!");
        }
        String idArray[] = deploymentIds.split(",");
        for (String id : idArray) {
            flowProcessService.deleteDeployment(id);
        }
        return AjaxJson.success("删除成功!");
    }

    /**
     * 删除流程实例
     *
     * @param ids 流程实例ID
     * @param reason    删除原因
     */
    @DeleteMapping("deleteProcIns")
    public AjaxJson deleteProcIns(String ids, String reason) {
        try {
            for (String id : ids.split(",")) {
                flowProcessService.deleteProcIns(id, reason);
            }
            return AjaxJson.success("流程实例删除成功!");
        } catch (FlowableObjectNotFoundException e) {
            return AjaxJson.success("操作失败，流程已经结束!");
        }

    }

    /**
     * 流程撤回
     *
     * @param id 流程实例ID
     */
    @PostMapping("revokeProcIns")
    public AjaxJson revokeProcIns(String id) {
        try {
            flowProcessService.callBackProcessInstanceById (id);

            return AjaxJson.success("流程撤销成功!");
        } catch (FlowableObjectNotFoundException e) {
            return AjaxJson.success("操作失败，流程已经结束!");
        }

    }

    /**
     * 流程终止
     *
     * @param id 流程实例ID
     */
    @PostMapping("stop")
    public AjaxJson stopProcIns(String id, String message) {
        try {
            flowProcessService.stopProcessInstanceById (id, ProcessStatus.STOP, message);
            return AjaxJson.success("终止流程成功!");
        } catch (FlowableObjectNotFoundException e) {
            return AjaxJson.success("操作失败，流程已经结束!");
        }

    }


    /**
     * 查询流程状态
     *
     */
    @GetMapping("queryProcessStatus")
    public AjaxJson queryProcessStatus(String procDefId, String procInsId) throws Exception {
        ProcessVo processVo = flowProcessService.queryProcessState (procDefId, procInsId);
        return AjaxJson.success ().put ("code", processVo.getCode ());

    }

}
