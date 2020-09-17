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
    @Pattern(regexp = "[a-zA-Z]+", message = "依赖名称必须为英文")
    private String relyName;
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
