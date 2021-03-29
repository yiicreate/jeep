/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.echarts.service;


import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.echarts.entity.Echarts;
import com.jeeplus.modules.echarts.mapper.EchartsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 图表组件Service
 *
 * @author 刘高峰
 * @version 2018-08-13
 */
@Service
@Transactional(readOnly = true)
public class EchartsService extends CrudService<EchartsMapper, Echarts> {

    public Echarts get(String id) {
        return super.get(id);
    }

    public List<Echarts> findList(Echarts echarts) {
        return super.findList(echarts);
    }

    public Page<Echarts> findPage(Page page, Echarts echarts) {
        return super.findPage(page, echarts);
    }

    @Transactional(readOnly = false)
    public void save(Echarts echarts) {
        super.save(echarts);
    }

    @Transactional(readOnly = false)
    public void delete(Echarts echarts) {
        super.delete(echarts);
    }

}
