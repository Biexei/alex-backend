package org.alex.platform.pojo;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseDO implements Serializable {
    @NotNull(message = "模块编号不能为空")
    private Integer moduleId;
    @NotNull(message = "项目编号不能为空")
    private Integer projectId;
    private Integer caseId;
    @NotEmpty(message = "url不能为空")
    @Size(max = 200, message = "url长度必须小于等于200")
    private String url;
    @Max(value = 4, message = "请求方式必须为0~4")
    @Min(value = 0, message = "请求方式必须为0~4")
    @NotNull(message = "请求方式不能为空")
    private Byte method;
    @Size(max = 100, message = "用例描述长度必须小于等于100")
    private String desc;
    @NotNull(message = "用例等级不能为空")
    @Max(value = 2, message = "用例等级必须为0~4")
    @Min(value = 0, message = "用例等级必须为0~4")
    private Byte level;
    @Size(max = 200, message = "接口文档地址长度必须小于等于200")
    private String doc;
    @Size(max = 1000, message = "请求头长度必须小于等于1000")
    private String headers;
    @Size(max = 1000, message = "请求参数长度必须小于等于1000")
    private String params;
    @Size(max = 1000, message = "请求表单数据长度必须小于等于1000")
    private String data;
    @Size(max = 1000, message = "请求json数据长度必须小于等于1000")
    private String json;
    @NotNull(message = "创建者不能为空")
    @Size(min = 1, max = 20, message = "帐号名称长度必须为1~20")
    @NotNull(message = "创建者不能为空")
    private String creater;
    private Date createdTime;
    private Date updateTime;

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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

    public Byte getMethod() {
        return method;
    }

    public void setMethod(Byte method) {
        this.method = method;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "InterfaceCaseDO{" +
                "moduleId=" + moduleId +
                ", projectId=" + projectId +
                ", caseId=" + caseId +
                ", url='" + url + '\'' +
                ", method=" + method +
                ", desc='" + desc + '\'' +
                ", level=" + level +
                ", doc='" + doc + '\'' +
                ", headers='" + headers + '\'' +
                ", params='" + params + '\'' +
                ", data='" + data + '\'' +
                ", json='" + json + '\'' +
                ", creater='" + creater + '\'' +
                ", createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
