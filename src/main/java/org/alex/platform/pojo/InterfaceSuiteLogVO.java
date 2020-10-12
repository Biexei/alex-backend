package org.alex.platform.pojo;

public class InterfaceSuiteLogVO extends InterfaceSuiteLogDO{
    private String suiteName;
    private String suiteDesc;

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
}
