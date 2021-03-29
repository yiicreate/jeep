package com.jeeplus.modules.form.dto;

import lombok.Data;

@Data
public class Relation {
    private String primaryKey;
    private String foreignKey;
    private String childTableName;
    private String type;
}
