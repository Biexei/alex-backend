package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class MockApiInfoVO {
    private Integer apiId;
    private String desc;
    private String url;
    private String method;
    private Integer responseCode;
    private String responseHeaders;
    private String responseBody;
    private Integer responseDelay;
    private Byte status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Byte responseHeadersEnableRely;
    private Byte responseBodyEnableRely;
    private Integer creatorId;
    private String creatorName;
    private Byte responseBodyType;

    private List<MockHitPolicyVO> policies;

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public Integer getResponseDelay() {
        return responseDelay;
    }

    public void setResponseDelay(Integer responseDelay) {
        this.responseDelay = responseDelay;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getResponseHeadersEnableRely() {
        return responseHeadersEnableRely;
    }

    public void setResponseHeadersEnableRely(Byte responseHeadersEnableRely) {
        this.responseHeadersEnableRely = responseHeadersEnableRely;
    }

    public Byte getResponseBodyEnableRely() {
        return responseBodyEnableRely;
    }

    public void setResponseBodyEnableRely(Byte responseBodyEnableRely) {
        this.responseBodyEnableRely = responseBodyEnableRely;
    }

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

    public Byte getResponseBodyType() {
        return responseBodyType;
    }

    public void setResponseBodyType(Byte responseBodyType) {
        this.responseBodyType = responseBodyType;
    }

    public List<MockHitPolicyVO> getPolicies() {
        return policies;
    }

    public void setPolicies(List<MockHitPolicyVO> policies) {
        this.policies = policies;
    }
}
