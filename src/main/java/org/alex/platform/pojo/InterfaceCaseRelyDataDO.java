package org.alex.platform.pojo;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseRelyDataDO implements Serializable {
    private Integer relyId;
    @NotNull(message = "relyCaseId不能为空")
    private Integer relyCaseId;
    @NotEmpty
    @Size(min = 1, max = 100, message = "relyName长度必须为[1,100]")
    @Pattern(regexp = "[a-zA-Z]+", message = "relyName必须为英文")
    private String relyName;
    @Size(max = 100, message = "relyDesc长度必须为[0,100]")
    private String relyDesc;
    @Max(value = 2, message = "contentType最大不能超过2")
    @NotNull(message = "contentType不能为空")
    private Byte contentType;
    @Size(max = 50, message = "extractExpression长度必须为[0,50]")
    @NotEmpty(message = "extractExpression不能为空")
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
