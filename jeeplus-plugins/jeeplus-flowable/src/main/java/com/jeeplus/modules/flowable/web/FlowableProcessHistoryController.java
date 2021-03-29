/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flowable.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.extension.service.FlowCopyService;
import com.jeeplus.modules.flowable.service.FlowProcessService;
import com.jeeplus.modules.flowable.vo.ProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 流程定义相关Controller
 *
 * @author jeeplus
 * @version 2016-11-03
 */
@RestController
@RequestMapping("/flowable/process/history")
public class FlowableProcessHistoryController extends BaseController {

    @Autowired
    private FlowProcessService flowProcessService;

    @Autowired
    private FlowCopyService flowCopyService;

    /**
     * 已结束的实例
     */
    @GetMapping("historyList")
    public AjaxJson historyListData(String procInsId, String procDefKey, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Page<ProcessVo> page = flowProcessService.historyList(new Page<ProcessVo>(request, response), procInsId, procDefKey);
        return AjaxJson.success().put("page", page);
    }

    /**
     * 删除历史流程实例
     *
     * @param procInsId 流程实例ID
     */
    @DeleteMapping("deleteProcIns")
    public AjaxJson deleteProcIns(String procInsId) {
        flowProcessService.delHistoryProcInsById(procInsId);
        flowCopyService.deleteByProcInsId(procInsId);
        return AjaxJson.success("删除成功，流程实例ID=" + procInsId);
    }

    /**
     * 删除历史流程实例
     *
     * @param procInsIds 流程实例ID
     */
    @DeleteMapping("deleteAllProcIns")
    public AjaxJson deleteAllProcIns(String procInsIds) {
        String[] procInsIdArra = procInsIds.split(",");
        for (String procInsId : procInsIdArra) {
            flowProcessService.delHistoryProcInsById(procInsId);
            flowCopyService.deleteByProcInsId(procInsId);
        }
        return AjaxJson.success("删除成功，流程实例ID=" + procInsIds);
    }

}
