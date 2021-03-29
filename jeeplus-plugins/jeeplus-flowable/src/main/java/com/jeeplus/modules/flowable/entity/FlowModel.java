package com.jeeplus.modules.flowable.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;
import java.util.Map;

/**
 * 流程模型Entity
 * @author liugaofeng
 * @version 2018-08-02
 */
public class FlowModel extends DataEntity<FlowModel> {

	private static final long serialVersionUID = 1L;
    protected String name;
    protected String key;
    protected String description;
    protected Date created;
    protected Date lastUpdated;
    private String createdBy;
    private String lastUpdatedBy;
    protected int version;
    protected String modelEditorJson;
    protected String comment;
    protected Integer modelType;
    protected String tenantId;
    private byte[] thumbnail;
    private Map procDef;    // 流程定义对象

    public Map getProcDef() {
        return procDef;
    }

    public void setProcDef(Map procDef) {
        this.procDef = procDef;
    }

    public FlowModel() {
    	super();
        this.created = new Date();
    }

    public FlowModel(String id) {
    	super(id);
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getModelEditorJson() {
        return modelEditorJson;
    }

    public void setModelEditorJson(String modelEditorJson) {
        this.modelEditorJson = modelEditorJson;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

}


