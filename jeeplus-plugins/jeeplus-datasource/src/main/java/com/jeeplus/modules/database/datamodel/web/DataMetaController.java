/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.database.datamodel.entity.DataMeta;
import com.jeeplus.modules.database.datamodel.entity.DataSet;
import com.jeeplus.modules.database.datamodel.service.DataMetaService;
import com.jeeplus.modules.database.datamodel.service.DataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyVetoException;

/**
 * 数据资源Controller
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
@RestController
@RequestMapping(value = "/database/datamodel/dataMeta")
public class DataMetaController extends BaseController {

    @Autowired
    private DataMetaService dataMetaService;

    @Autowired
    private DataSetService dataSetService;


    @ModelAttribute
    public DataMeta get(@RequestParam(value = "id", required = false) String id) {
        DataMeta entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = dataMetaService.get(id);
        }
        if (entity == null) {
            entity = new DataMeta();
        }
        return entity;
    }

    @GetMapping("/queryNeedByDataSetId")
    public AjaxJson queryNeedByDataSetId(@RequestParam(value = "id", required = false) String id) throws PropertyVetoException {
        DataSet dataSet = dataSetService.get(id);
        DataMeta dataMeta = new DataMeta();
        dataMeta.setDataSet(dataSet);
        dataMeta.setIsNeed(true);
        return AjaxJson.success().put("metas", dataMetaService.findList(dataMeta));
    }


}
