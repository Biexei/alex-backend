package org.alex.platform.pojo;

import java.io.Serializable;

public class InterfaceCaseRelyDataVO extends InterfaceCaseRelyDataDO implements Serializable {
    private String caseUrl;
    private String caseDesc;

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
}
