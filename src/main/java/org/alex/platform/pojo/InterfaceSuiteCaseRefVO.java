package org.alex.platform.pojo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class InterfaceSuiteCaseRefVO implements Serializable {
    private Integer id;
    private Integer suiteId;
    private String suiteName;
    private String suiteDesc;
    private Integer caseId;
    private String caseDesc;
    private Integer level;
    private String url;
    private Integer order;
    private Byte caseStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(Integer suiteId) {
        this.suiteId = suiteId;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getSuiteDesc() {
        return suiteDesc;
    }

    public void setSuiteDesc(String suiteDesc) {
        this.suiteDesc = suiteDesc;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Byte getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Byte caseStatus) {
        this.caseStatus = caseStatus;
    }
}
