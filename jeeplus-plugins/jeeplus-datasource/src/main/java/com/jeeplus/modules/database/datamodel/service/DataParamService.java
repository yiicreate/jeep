/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.database.datamodel.entity.DataParam;
import com.jeeplus.modules.database.datamodel.entity.DataSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.database.datamodel.mapper.DataParamMapper;

/**
 * 数据模型参数Service
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
@Service
@Transactional(readOnly = true)
public class DataParamService extends CrudService<DataParamMapper, DataParam> {


    public DataParam get(String id) {
        return super.get(id);
    }

    public List<DataParam> findList(DataParam dataParam) {
        return super.findList(dataParam);
    }

    public Page<DataParam> findPage(Page page, DataParam dataParam) {
        return super.findPage(page, dataParam);
    }

    @Transactional(readOnly = false)
    public void save(DataParam dataParam) {
        super.save(dataParam);
    }

    @Transactional(readOnly = false)
    public void delete(DataParam dataParam) {
        super.delete(dataParam);
    }

    @Transactional(readOnly = false)
    public void deleteByDataSetId(String id) {
        mapper.deleteByDataSetId(id);
    }

    ;

    public Map<String, String> getParamsForMap(DataSet dataSet) {
        Map<String, String> paramsMap = new HashMap<>();
        DataParam dataParam = new DataParam();
        dataParam.setDataSet(dataSet);
        //获取参数列表
        List<DataParam> paramList = super.findList(dataParam);
        if (paramList.size() > 0) {
            for (DataParam param : paramList) {
                paramsMap.put(param.getField(), param.getDefaultValue());
            }
        }
        return paramsMap;
    }

}
