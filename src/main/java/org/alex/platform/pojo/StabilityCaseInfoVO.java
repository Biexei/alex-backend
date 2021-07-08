package org.alex.platform.pojo;

import java.io.Serializable;

public class StabilityCaseInfoVO extends StabilityCaseDO implements Serializable {
    private String caseDesc;

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }
}
