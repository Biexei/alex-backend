package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseExecuteLogListVO implements Serializable {
    private Integer id;
    private Integer caseId;
    private String caseDesc;
    private Byte caseMethod;
    private Integer projectId;
    private String projectName;
    private Integer moduleId;
    private String moduleName;
    private String responseBody;
    private String responseHeaders;
    private String executer;
    private Long runTime;
    private Byte status;
    private Date createdTime;
    private String errorMessage;
    private String suiteLogNo;
    private String suiteLogDetailNo;
    private String chain;
    private Byte isFailedRetry;
    private Integer responseCode;
    private Byte source;

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public Byte getCaseMethod() {
        return caseMethod;
    }

    public void setCaseMethod(Byte caseMethod) {
        this.caseMethod = caseMethod;
    }

    public String getSuiteLogDetailNo() {
        return suiteLogDetailNo;
    }

    public void setSuiteLogDetailNo(String suiteLogDetailNo) {
        this.suiteLogDetailNo = suiteLogDetailNo;
    }

    public Byte getIsFailedRetry() {
        return isFailedRetry;
    }

    public void setIsFailedRetry(Byte isFailedRetry) {
        this.isFailedRetry = isFailedRetry;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getSuiteLogNo() {
        return suiteLogNo;
    }

    public void setSuiteLogNo(String suiteLogNo) {
        this.suiteLogNo = suiteLogNo;
    }

    public Integer getId() {
        return id;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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
                ", caseMethod=" + caseMethod +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", moduleId=" + moduleId +
                ", moduleName='" + moduleName + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", executer='" + executer + '\'' +
                ", runTime=" + runTime +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", errorMessage='" + errorMessage + '\'' +
                ", suiteLogNo='" + suiteLogNo + '\'' +
                ", suiteLogDetailNo='" + suiteLogDetailNo + '\'' +
                ", chain='" + chain + '\'' +
                ", isFailedRetry=" + isFailedRetry +
                ", responseCode=" + responseCode +
                '}';
    }
}
