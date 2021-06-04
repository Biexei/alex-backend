package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class RelyDataDO implements Serializable {
    private Integer id;
    @NotNull(message = "依赖名称不允许为空")
    @NotEmpty(message = "依赖名称不允许为空")
    @Size(min = 1, max = 100, message = "依赖名称长度必须为[1,100]")
    @Pattern(regexp = "\\w+", message = "依赖名称必须为字母数字下划线")
    private String name;
    @NotNull(message = "固定值/方法声明/SQL不允许为空")
    @NotEmpty(message = "固定值/方法声明/SQL不允许为空")
    private String value;
    @NotNull(message = "依赖描述不允许为空")
    @NotEmpty(message = "依赖描述不允许为空")
    @Size(min = 1, max = 200, message = "依赖描述长度必须为[1,200]")
    private String desc;
    @NotNull(message = "类型不允许为空")
    private Byte type;
    private Integer datasourceId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Byte analysisRely;
    private Byte enableReturn;

    private Integer creatorId;
    private String creatorName;
    private Byte othersDeletable;
    private Byte othersModifiable;

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Byte getOthersDeletable() {
        return othersDeletable;
    }

    public void setOthersDeletable(Byte othersDeletable) {
        this.othersDeletable = othersDeletable;
    }

    public Byte getOthersModifiable() {
        return othersModifiable;
    }

    public void setOthersModifiable(Byte othersModifiable) {
        this.othersModifiable = othersModifiable;
    }

    public Byte getAnalysisRely() {
        return analysisRely;
    }

    public void setAnalysisRely(Byte analysisRely) {
        this.analysisRely = analysisRely;
    }

    public Byte getEnableReturn() {
        return enableReturn;
    }

    public void setEnableReturn(Byte enableReturn) {
        this.enableReturn = enableReturn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Integer datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
