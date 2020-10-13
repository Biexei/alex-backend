package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseExecuteLogDO implements Serializable {
    private Integer id;
    private Integer caseId;
    private String caseUrl;
    private String caseDesc;
    private String requestHeaders;
    private String requestParams;
    private String requestData;
    private String requestJson;
    private Integer responseCode;
    private String responseHeaders;
    private String responseBody;
    private Long runTime;
    private String executer;
    private Byte status;
    private Date createdTime;
    private String errorMessage;
    private String suiteLogNo;
    private String chain;

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

    public String getCaseUrl() {
        return caseUrl;
    }

    public void setCaseUrl(String caseUrl) {
        this.caseUrl = caseUrl;
    }

    public Integer getId() {
        return id;
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

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
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
        return "InterfaceCaseExecuteLogDO{" +
                "id=" + id +
                ", caseId=" + caseId +
                ", caseUrl='" + caseUrl + '\'' +
                ", caseDesc='" + caseDesc + '\'' +
                ", requestHeaders='" + requestHeaders + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", requestData='" + requestData + '\'' +
                ", requestJson='" + requestJson + '\'' +
                ", responseCode=" + responseCode +
                ", responseHeaders='" + responseHeaders + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", runTime=" + runTime +
                ", executer='" + executer + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
