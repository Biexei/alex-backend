package org.alex.platform.pojo;

import java.util.Date;
import java.util.List;

public class InterfaceCaseInfoVO {
    private Integer projectId;
    private Integer moduleId;
    private Integer caseId;
    private String url;
    private String desc;
    private Byte level;
    private String doc;
    private String headers;
    private String params;
    private String data;
    private String json;
    private String creater;
    private Date createdTime;
    private List<InterfaceAssertVO> asserts;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<InterfaceAssertVO> getAsserts() {
        return asserts;
    }

    public void setAsserts(List<InterfaceAssertVO> asserts) {
        this.asserts = asserts;
    }

    @Override
    public String toString() {
        return "InterfaceCaseInfoVO{" +
                "projectId=" + projectId +
                ", moduleId=" + moduleId +
                ", caseId=" + caseId +
                ", url='" + url + '\'' +
                ", desc='" + desc + '\'' +
                ", level=" + level +
                ", doc='" + doc + '\'' +
                ", headers='" + headers + '\'' +
                ", params='" + params + '\'' +
                ", data='" + data + '\'' +
                ", json='" + json + '\'' +
                ", creater='" + creater + '\'' +
                ", createdTime=" + createdTime +
                ", asserts=" + asserts +
                '}';
    }
}
