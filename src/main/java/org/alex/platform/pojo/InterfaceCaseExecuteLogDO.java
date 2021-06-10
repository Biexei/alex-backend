package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseExecuteLogDO implements Serializable {
    private Integer id;
    private Integer caseId;
    private String caseUrl;
    private String caseDesc;
    private Byte caseMethod;
    private String requestHeaders;
    private String requestParams;
    private String requestBody;
    private Integer responseCode;
    private String responseHeaders;
    private String responseBody;
    private Long runTime;
    private String executer;
    private Byte status;
    private Date createdTime;
    private String errorMessage;
    private String suiteLogNo;
    private String suiteLogDetailNo;
    private String chain;
    private Byte isFailedRetry;
    private String rawRequestHeaders;
    private String rawRequestParams;
    private String rawRequestBody;
    private Byte source;
    private String rawType;
    private Byte bodyType;

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

    public String getCaseUrl() {
        return caseUrl;
    }

    public void setCaseUrl(String caseUrl) {
        this.caseUrl = caseUrl;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public Byte getCaseMethod() {
        return caseMethod;
    }

    public void setCaseMethod(Byte caseMethod) {
        this.caseMethod = caseMethod;
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

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
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

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
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

    public String getSuiteLogNo() {
        return suiteLogNo;
    }

    public void setSuiteLogNo(String suiteLogNo) {
        this.suiteLogNo = suiteLogNo;
    }

    public String getSuiteLogDetailNo() {
        return suiteLogDetailNo;
    }

    public void setSuiteLogDetailNo(String suiteLogDetailNo) {
        this.suiteLogDetailNo = suiteLogDetailNo;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public Byte getIsFailedRetry() {
        return isFailedRetry;
    }

    public void setIsFailedRetry(Byte isFailedRetry) {
        this.isFailedRetry = isFailedRetry;
    }

    public String getRawRequestHeaders() {
        return rawRequestHeaders;
    }

    public void setRawRequestHeaders(String rawRequestHeaders) {
        this.rawRequestHeaders = rawRequestHeaders;
    }

    public String getRawRequestParams() {
        return rawRequestParams;
    }

    public void setRawRequestParams(String rawRequestParams) {
        this.rawRequestParams = rawRequestParams;
    }

    public String getRawRequestBody() {
        return rawRequestBody;
    }

    public void setRawRequestBody(String rawRequestBody) {
        this.rawRequestBody = rawRequestBody;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public String getRawType() {
        return rawType;
    }

    public void setRawType(String rawType) {
        this.rawType = rawType;
    }

    public Byte getBodyType() {
        return bodyType;
    }

    public void setBodyType(Byte bodyType) {
        this.bodyType = bodyType;
    }

    @Override
    public String toString() {
        return "InterfaceCaseExecuteLogDO{" +
                "id=" + id +
                ", caseId=" + caseId +
                ", caseUrl='" + caseUrl + '\'' +
                ", caseDesc='" + caseDesc + '\'' +
                ", caseMethod=" + caseMethod +
                ", requestHeaders='" + requestHeaders + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", responseCode=" + responseCode +
                ", responseHeaders='" + responseHeaders + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", runTime=" + runTime +
                ", executer='" + executer + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", errorMessage='" + errorMessage + '\'' +
                ", suiteLogNo='" + suiteLogNo + '\'' +
                ", suiteLogDetailNo='" + suiteLogDetailNo + '\'' +
                ", chain='" + chain + '\'' +
                ", isFailedRetry=" + isFailedRetry +
                ", rawRequestHeaders='" + rawRequestHeaders + '\'' +
                ", rawRequestParams='" + rawRequestParams + '\'' +
                ", rawRequestBody='" + rawRequestBody + '\'' +
                ", source=" + source +
                ", rawType='" + rawType + '\'' +
                ", bodyType=" + bodyType +
                '}';
    }
}
