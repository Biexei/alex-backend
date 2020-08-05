package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseExecuteLogListVO implements Serializable {
    private Integer id;
    private Integer caseId;
    private String caseDesc;
    private String responseBody;
    private String executer;
    private Byte status;
    private Date createdTime;
    private String errorMessage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getExecuter() {
        return executer;
    }

    public void setExecuter(String executer) {
        this.executer = executer;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "InterfaceCaseExecuteLogListVO{" +
                "id=" + id +
                ", caseId=" + caseId +
                ", caseDesc='" + caseDesc + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", executer='" + executer + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
