package org.alex.platform.pojo;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseRelyDataDO implements Serializable {
    private Integer relyId;
    @NotNull(message = "依赖用例不能为空")
    private Integer relyCaseId;
    @NotEmpty(message = "依赖名称不能为空")
    @Size(min = 1, max = 100, message = "依赖名称长度必须为[1,100]")
    @Pattern(regexp = "\\w+", message = "依赖名称必须为字母数字下划线")
    private String relyName;
    @NotEmpty(message = "依赖描述不能为空")
    @Size(max = 100, message = "依赖描述长度必须为[0,100]")
    private String relyDesc;
    @Max(value = 2, message = "提取类型最大不能超过2")
    @NotNull(message = "提取类型不能为空")
    private Byte contentType;
    @Size(max = 50, message = "提取表达式长度必须为[0,50]")
    @NotEmpty(message = "提取表达式不能为空")
    private String extractExpression;
    private Date createdTime;
    private Date updateTime;

    private Integer creatorId;
    private String creatorName;
    @NotNull(message = "请选择是否允许其他人删除")
    @Min(value = 0, message = "请选择是否允许其他人删除")
    @Max(value = 1, message = "请选择是否允许其他人删除")
    private Byte othersDeletable;
    @NotNull(message = "请选择是否允许其他人编辑")
    @Min(value = 0, message = "请选择是否允许其他人编辑")
    @Max(value = 1, message = "请选择是否允许其他人编辑")
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

    public Integer getRelyId() {
        return relyId;
    }

    public void setRelyId(Integer relyId) {
        this.relyId = relyId;
    }

    public Integer getRelyCaseId() {
        return relyCaseId;
    }

    public void setRelyCaseId(Integer relyCaseId) {
        this.relyCaseId = relyCaseId;
    }

    public String getRelyName() {
        return relyName;
    }

    public void setRelyName(String relyName) {
        this.relyName = relyName;
    }

    public String getRelyDesc() {
        return relyDesc;
    }

    public void setRelyDesc(String relyDesc) {
        this.relyDesc = relyDesc;
    }

    public Byte getContentType() {
        return contentType;
    }

    public void setContentType(Byte contentType) {
        this.contentType = contentType;
    }

    public String getExtractExpression() {
        return extractExpression;
    }

    public void setExtractExpression(String extractExpression) {
        this.extractExpression = extractExpression;
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
