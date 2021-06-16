package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

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
    @Size(max = 1000, message = "用例描述长度必须小于等于1000")
    private String desc;
    @NotNull(message = "用例等级不能为空")
    @Max(value = 2, message = "用例等级必须为0~4")
    @Min(value = 0, message = "用例等级必须为0~4")
    private Byte level;
    @Size(max = 200, message = "接口文档地址长度必须小于等于200")
    private String doc;
    private String headers;
    private String params;
    private String formData;
    private String formDataEncoded;
    private String raw;
    private String rawType;
    @NotNull(message = "body类型不能为空")
    private Byte bodyType;
    @NotNull(message = "创建者不能为空")
    @Size(min = 1, max = 20, message = "创建者长度必须为1~20")
    @NotNull(message = "创建者不能为空")
    private String creater;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Byte source;
    private String importNo;

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

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public String getFormDataEncoded() {
        return formDataEncoded;
    }

    public void setFormDataEncoded(String formDataEncoded) {
        this.formDataEncoded = formDataEncoded;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
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

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public String getImportNo() {
        return importNo;
    }

    public void setImportNo(String importNo) {
        this.importNo = importNo;
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
                ", formData='" + formData + '\'' +
                ", formDataEncoded='" + formDataEncoded + '\'' +
                ", raw='" + raw + '\'' +
                ", rawType='" + rawType + '\'' +
                ", bodyType=" + bodyType +
                ", creater='" + creater + '\'' +
                ", createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                ", source=" + source +
                ", importNo='" + importNo + '\'' +
                '}';
    }
}
