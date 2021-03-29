/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.database.datamodel.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.database.datalink.jdbc.DBPool;
import com.jeeplus.modules.database.datamodel.entity.DataMeta;
import com.jeeplus.modules.database.datamodel.entity.DataParam;
import com.jeeplus.modules.database.datamodel.entity.DataSet;
import com.jeeplus.modules.database.datamodel.mapper.DataSetMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 数据模型Service
 *
 * @author 刘高峰
 * @version 2018-08-07
 */
@Service
@Transactional(readOnly = true)
public class DataSetService extends CrudService<DataSetMapper, DataSet> {
    @Autowired
    private DataParamService dataParamService;
    @Autowired
    private DataMetaService dataMetaService;

    public DataSet get(String id) {
        DataSet dataSet = super.get(id);
        return dataSet;
    }

    public DataSet detail(String id) {
        DataSet dataSet = super.get(id);
        if (dataSet == null) {
            return null;
        }
        DataMeta dataMeta = new DataMeta();
        dataMeta.setDataSet(dataSet);
        dataSet.setColumnList(dataMetaService.findList(dataMeta));

        DataParam dataParam = new DataParam();
        dataParam.setDataSet(dataSet);
        dataSet.setParams(dataParamService.findList(dataParam));
        return dataSet;
    }


    public List<DataSet> findList(DataSet dataSet) {
        return super.findList(dataSet);
    }

    public Page<DataSet> findPage(Page page, DataSet dataSet) {
        return super.findPage(page, dataSet);
    }

    @Transactional(readOnly = false)
    public void save(DataSet dataSet) {
        super.save(dataSet);
        dataMetaService.deleteByDsId(dataSet.getId());
        dataParamService.deleteByDataSetId(dataSet.getId());
        for (DataMeta modelMeta : dataSet.getColumnList()) {
            modelMeta.setDataSet(dataSet);
            dataMetaService.save(modelMeta);

        }
        for (DataParam params : dataSet.getParams()) {
            params.setDataSet(dataSet);
            dataParamService.save(params);
        }
    }

    @Transactional(readOnly = false)
    public void delete(DataSet dataSet) {
        super.delete(dataSet);
        dataParamService.deleteByDataSetId(dataSet.getId());
        dataMetaService.deleteByDsId(dataSet.getId());
    }


    public List<Map<String, Object>> queryForListById(String id, HttpServletRequest request) throws Exception {
        Map paramsMap = dataParamService.getParamsForMap(get(id));
        if(request != null){
            Enumeration<String> names = request.getParameterNames();
            while(names.hasMoreElements()){
                String param = names.nextElement().toString();
                paramsMap.put(param, request.getParameter(param));
            }
        }
        String sql = mergeSql(get(id).getSqlcmd(), paramsMap);
        return DBPool.getInstance().getDataSource(get(id).getDb().getEnName()).queryForList(sql);

    }

    public String mergeSql(String sql, String[] field, String[] value) {
        //转换成MAP
        Map<String, String> paramsMap = new HashMap<>();
        if (field != null) {
            for (int i = 0; i < field.length; i++) {
                paramsMap.put(field[i], value[i]);
            }
        }
        return mergeSql(sql, paramsMap);
    }

    public String mergeSql(String sql, Map<String, String> paramsMap) {
        sql = StringEscapeUtils.unescapeHtml4(sql);
        for (String key : paramsMap.keySet()) {
            sql = sql.replace("{#"+key+"#}", paramsMap.get(key));
        }
        return sql;
    }


    public JSONArray toJSON(List<Map<String, Object>> list) {
        JSONArray data = new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject obj = new JSONObject();
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value != null)
                    obj.element(key, value.toString());
                else
                    obj.element(key, "");
            }

            data.add(obj);
        }
        return data;
    }

    public String toXML(List<Map<String, Object>> data) {
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<list>\n");
        for (Map<String, Object> map : data) {
            xml.append("  <data>\n");
            Iterator<String> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                Object val = map.get(key);

                xml.append("    <prop key=\"" + key + "\">\n      <![CDATA[" + val + "]]>\n    </prop>\n");
            }
            xml.append("  </data>\n");
        }
        xml.append("</list>\n");
        return xml.toString();
    }

    public String toHTML(List<Map<String, Object>> data) {
        StringBuffer html = new StringBuffer("<table class='table table-bordered table-condensed'>");
        StringBuffer head = new StringBuffer("<thead><tr>");
        StringBuffer body = new StringBuffer("<tbody><tr>");

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (i == 0) {
                    head.append("<th>").append(entry.getKey()).append("</th>");
                }
                body.append("<td>").append(entry.getValue()).append("</td>");
            }
            body.append("</tr><tr>");
        }

        head.append("</tr></thead>");
        body.append("</tr></tbody>");
        html.append(head).append(body).append("</table>");
        return html.toString();
    }

}
