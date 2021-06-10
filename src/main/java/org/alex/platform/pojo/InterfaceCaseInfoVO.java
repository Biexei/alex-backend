package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InterfaceCaseInfoVO implements Serializable {
    private Integer projectId;
    private Integer moduleId;
    private Integer caseId;
    private String url;
    private Byte method;
    private String desc;
    private Byte level;
    private String doc;
    private String headers;
    private String params;
    private String formData;
    private String formDataEncoded;
    private String raw;
    private String rawType;
    private Byte bodyType;
    private String creater;
    private Date createdTime;
    private Byte source;
    private String importNo;
    private List<InterfaceAssertVO> asserts;
    private List<InterfaceProcessorDO> postProcessors;
    private List<InterfacePreCaseVO> preCases;

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

    public List<InterfaceAssertVO> getAsserts() {
        return asserts;
    }

    public void setAsserts(List<InterfaceAssertVO> asserts) {
        this.asserts = asserts;
    }

    public List<InterfaceProcessorDO> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(List<InterfaceProcessorDO> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public List<InterfacePreCaseVO> getPreCases() {
        return preCases;
    }

    public void setPreCases(List<InterfacePreCaseVO> preCases) {
        this.preCases = preCases;
    }

    @Override
    public String toString() {
        return "InterfaceCaseInfoVO{" +
                "projectId=" + projectId +
                ", moduleId=" + moduleId +
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
                ", source=" + source +
                ", importNo='" + importNo + '\'' +
                ", asserts=" + asserts +
                ", postProcessors=" + postProcessors +
                ", preCases=" + preCases +
                '}';
    }
}
