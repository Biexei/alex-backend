package org.alex.platform.pojo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class InterfaceSuiteCaseRefDO implements Serializable {
    private Integer id;
    @NotNull(message = "用例编号不能为空")
    private Integer caseId;
    @NotNull(message = "套件编号不能为空")
    private Integer suiteId;
    @NotNull(message = "用例状态不能为空")
    private Byte caseStatus;
    private Integer order;

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

    public Integer getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(Integer suiteId) {
        this.suiteId = suiteId;
    }

    public Byte getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Byte caseStatus) {
        this.caseStatus = caseStatus;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
