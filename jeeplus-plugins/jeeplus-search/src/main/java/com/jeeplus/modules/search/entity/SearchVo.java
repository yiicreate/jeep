package com.jeeplus.modules.search.entity;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import lombok.Data;

import java.util.Date;
/**
 * 模糊搜索Entity
 * @author lc
 * @version 2021-03-15
 */
@Data
public class SearchVo extends DataEntity<SearchVo> {
    private static final long serialVersionUID = 1L;
    private String name;		// 名称
    private String pathId;		// 路径
    private Integer type;		// 类别，0标识流程设计，1标识动态表单

    public SearchVo() {
        super();
    }
}
