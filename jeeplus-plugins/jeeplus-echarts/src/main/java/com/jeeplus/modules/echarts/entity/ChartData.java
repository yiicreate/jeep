package com.jeeplus.modules.echarts.entity;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class ChartData {

    private List<String> columns = Lists.newArrayList();
    private List<Map> rows = Lists.newArrayList();

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Map> getRows() {
        return rows;
    }

    public void setRows(List<Map> rows) {
        this.rows = rows;
    }
}
