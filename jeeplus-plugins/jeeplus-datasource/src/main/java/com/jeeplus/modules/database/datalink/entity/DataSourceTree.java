package com.jeeplus.modules.database.datalink.entity;

import com.google.common.collect.Lists;

import java.util.List;

public class DataSourceTree {
    private String id;
    private String label;
    private String parentId;
    private DataSourceTree parent;
    private String enName;
    private String type;
    private String dbType;
    private boolean disabled = false;
    private List<DataSourceTree> children = Lists.newArrayList();

    public DataSourceTree(String id, String label, String parentId, String enName, String type){
        this.id = id;
        this.label = label;
        this.parentId = parentId;
        this.enName = enName;
        this.type = type;
    }

    public DataSourceTree(String id, String label, String parentId, String enName, String type, String dbType
    ){
        this.id = id;
        this.label = label;
        this.parentId = parentId;
        this.enName = enName;
        this.type = type;
        this.dbType = dbType;
    }

    public DataSourceTree(String id, String label, String parentId, String enName, String type, boolean disabled){
        this.id = id;
        this.label = label;
        this.parentId = parentId;
        this.enName = enName;
        this.type = type;
        this.disabled = disabled;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DataSourceTree getParent() {
        return parent;
    }

    public void setParent(DataSourceTree parent) {
        this.parent = parent;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataSourceTree> getChildren() {
        return children;
    }

    public void setChildren(List<DataSourceTree> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
