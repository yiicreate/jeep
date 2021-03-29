/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.echarts.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.database.datamodel.entity.DataMeta;
import com.jeeplus.modules.database.datamodel.entity.DataSet;
import com.jeeplus.modules.database.datamodel.service.DataMetaService;
import com.jeeplus.modules.database.datamodel.service.DataSetService;
import com.jeeplus.modules.echarts.entity.Echarts;
import com.jeeplus.modules.echarts.service.EchartsService;
import com.jeeplus.modules.echarts.utils.OptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;


/**
 * 图表组件Controller
 *
 * @author 刘高峰
 * @version 2018-08-13
 */
@RestController
@RequestMapping(value = "/echarts")
public class EchartsController extends BaseController {


    @Autowired
    private DataMetaService dataMetaService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private EchartsService echartsService;

    @ModelAttribute
    public Echarts get(@RequestParam(value = "id", required = false) String id) {
        Echarts entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = echartsService.get(id);
        }
        if (entity == null) {
            entity = new Echarts();
        }
        return entity;
    }


    @GetMapping(value = "queryById")
    public AjaxJson preview(Echarts echarts) {
        return AjaxJson.success().put("echarts", echarts);
    }

    /**
     * 图表组件列表数据
     */
    @GetMapping(value = "list")
    public AjaxJson data(Echarts echarts, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Echarts> page = echartsService.findPage(new Page<Echarts>(request, response), echarts);
        return AjaxJson.success().put("page", page);
    }

    /**
     * 查看，增加，编辑图表组件表单页面
     * params:
     * mode: add, edit, view, 代表三种模式的页面
     */
    @GetMapping(value = "queryDesignById")
    public AjaxJson queryDesignById(Echarts echarts, Model model, HttpServletResponse response)throws Exception {

        DataMeta dataMeta = new DataMeta();
        dataMeta.setDataSet(echarts.getDataSet());
        List<DataMeta> xColumnList = Lists.newArrayList();
        List<DataMeta> yColumnList = Lists.newArrayList();
        List<DataMeta> columnList = dataMetaService.findList(dataMeta);
        for(String xName: echarts.getXNames().split(",")){
            for (DataMeta column : columnList) {
                if(xName.equals(column.getName())){
                    xColumnList.add(column);
                }
            }
        }
        for(String yName: echarts.getYNames().split(",")){
            for (DataMeta column : columnList) {
                if(yName.equals(column.getName())){
                    yColumnList.add(column);
                }
            }
        }
        return AjaxJson.success().put("xColumnList", xColumnList).put("yColumnList", yColumnList).put("echarts", echarts);
    }

    /**
     * 保存图表组件
     */
    @PostMapping(value = "save")
    public AjaxJson save(Echarts echarts) throws Exception {
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(echarts);
        if (StringUtils.isNotBlank(errMsg)) {
            return AjaxJson.error(errMsg);
        }
        //新增或编辑表单保存
        echartsService.save(echarts);//保存
        return AjaxJson.success("保存图表组件成功").put("echarts", echarts);
    }


    @RequestMapping(value = "createMenu")
    public String createMenu(@RequestParam(value = "id", required = false)String id, Model model) throws Exception{
        model.addAttribute("id", id);
        return "modules/echarts/createMenuForm";
    }

    /**
     * 批量删除图表组件
     */
    @DeleteMapping(value = "delete")
    public AjaxJson delete(@RequestParam(value = "ids", required = false) String ids) {
        AjaxJson j = new AjaxJson ();
        StringBuffer msg = new StringBuffer ();
        boolean flag = false;
        String idArray[] = ids.split (",");
        for (String id : idArray) {
            flag = true;
            msg.append ("删除图表组件 [" + echartsService.get (id).getName () + "] 成功。<br/>");
            echartsService.delete (echartsService.get (id));
        }
        j.setSuccess (flag);
        j.setMsg (msg.toString ());

        return j;
    }

    /**
     * 获取组件option
     */
    @GetMapping(value = "getChartData/{id}")
    public AjaxJson getChartData(@PathVariable("id")String id, HttpServletRequest request) throws Exception {
        Echarts echarts = get(id);
        DataSet dataSet = echarts.getDataSet();
        String xNames = echarts.getXNames();
        String yNames = echarts.getYNames();
        return AjaxJson.success().put("echarts", echarts).put("chartData", OptionUtil.getChartData(dataSet, xNames, yNames, request));
    }
    /**
     * 获取组件option
     */
    @GetMapping(value = "mergeChartData")
    public AjaxJson mergeChartData(String dataSetId, String xNames, String yNames, HttpServletRequest request) throws Exception {
        DataSet dataSet = dataSetService.get(dataSetId);
        return AjaxJson.success().put("chartData", OptionUtil.getChartData(dataSet, xNames, yNames, request));
    }

}
