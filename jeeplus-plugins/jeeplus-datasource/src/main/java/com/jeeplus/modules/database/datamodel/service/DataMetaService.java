/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.service;

import java.util.List;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.database.datamodel.entity.DataMeta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.database.datamodel.mapper.DataMetaMapper;

/**
 * 数据资源Service
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
@Service
@Transactional(readOnly = true)
public class DataMetaService extends CrudService<DataMetaMapper, DataMeta> {

    public DataMeta get(String id) {
        return super.get(id);
    }

    public List<DataMeta> findList(DataMeta dataMeta) {
        return super.findList(dataMeta);
    }

    public Page findPage(Page page, DataMeta dataMeta) {
        return super.findPage(page, dataMeta);
    }

    @Transactional(readOnly = false)
    public void save(DataMeta dataMeta) {
        super.save(dataMeta);
    }

    @Transactional(readOnly = false)
    public void delete(DataMeta dataMeta) {
        super.delete(dataMeta);
    }

    @Transactional(readOnly = false)
    public void deleteByDsId(String id) {
        mapper.deleteByDataSetId(id);
    }


}
