package org.alex.platform.pojo;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseRelyDataDO implements Serializable {
    private Integer relyId;
    @NotNull
    private Integer relyCaseId;
    @NotEmpty
    @Size(min = 1, max = 20)
    private String relyName;
    @Size(max = 20)
    private String relyDesc;
    @Max(3)
    @NotNull
    private Byte contentType;
    @Size(max = 50)
    @NotEmpty
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
