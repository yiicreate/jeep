package com.jeeplus.modules.echarts.utils;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.database.datamodel.entity.DataMeta;
import com.jeeplus.modules.database.datamodel.entity.DataSet;
import com.jeeplus.modules.database.datamodel.service.DataSetService;
import com.jeeplus.modules.echarts.entity.ChartData;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class OptionUtil {

    public static ChartData getChartData(DataSet dataSet, String xNames, String yNames, HttpServletRequest request) throws Exception {
        DataSetService dataSetService = SpringContextHolder.getBean(DataSetService.class);
        DataMeta dataMeta = new DataMeta();
        dataMeta.setDataSet(dataSet);
        List<Map<String, Object>> list = dataSetService.queryForListById(dataSet.getId(), request);
        ChartData chartData = new ChartData();
        chartData.getColumns().addAll(Arrays.asList(xNames.split(",")));
        chartData.getColumns().addAll(Arrays.asList(yNames.split(",")));
        for (Map map : list) {
            Map nMap = new LinkedHashMap();
            for (String name : chartData.getColumns()) {
                nMap.put(name, map.get(name));
            }
            chartData.getRows().add(nMap);
        }

        return chartData;
    }

}
