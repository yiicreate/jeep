/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datalink.service;

import java.util.List;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.database.datalink.entity.DataSource;
import com.jeeplus.modules.sys.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.database.datalink.mapper.DataSourceMapper;
import org.springframework.web.util.HtmlUtils;

/**
 * 数据库连接Service
 *
 * @author 刘高峰
 * @version 2018-08-06
 */
@Service
@Transactional(readOnly = true)
public class DataSourceService extends CrudService<DataSourceMapper, DataSource> {


    public DataSource get(String id) {
        return super.get(id);
    }

    public List<DataSource> findList(DataSource dataSource) {
        return super.findList(dataSource);
    }

    public Page<DataSource> findPage(Page page, DataSource dataSource) {
        return super.findPage(page, dataSource);
    }

    @Transactional(readOnly = false)
    public void save(DataSource dataSource) {
        super.save(dataSource);
    }

    @Transactional(readOnly = false)
    public void delete(DataSource dataSource) {
        super.delete(dataSource);
    }

    /**
     * 转换为JDBC连接
     *
     * @param type
     * @param host
     * @param port
     * @param dbName
     * @return
     */
    public String toUrl(String type, String host, int port, String dbName) {
        String template = DictUtils.getDictValue(type, "db_jdbc_url", "mysql");
        if (template != null) {
            template = HtmlUtils.htmlUnescape(template);
            return template.replace("${host}", host).replace("${port}", port + "").replace("${db}", dbName);
        }
        return null;
    }

    /**
     * 获取JDBC驱动
     *
     * @param type
     * @return
     */
    public String getDriver(String type) {

       return DictUtils.getDictValue(type, "db_driver", "mysql");

    }

}
