package org.alex.platform.pojo;

public class StabilityCaseDTO {
    private Integer stabilityTestId;
    private String desc;
    private Byte protocol;
    private  Integer caseId;
    private String creatorName;
    private Byte executeType;
    private Byte runEnv;

    public Byte getRunEnv() {
        return runEnv;
    }

    public void setRunEnv(Byte runEnv) {
        this.runEnv = runEnv;
    }

    public Integer getStabilityTestId() {
        return stabilityTestId;
    }

    public void setStabilityTestId(Integer stabilityTestId) {
        this.stabilityTestId = stabilityTestId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Byte getProtocol() {
        return protocol;
    }

    public void setProtocol(Byte protocol) {
        this.protocol = protocol;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Byte getExecuteType() {
        return executeType;
    }

    public void setExecuteType(Byte executeType) {
        this.executeType = executeType;
    }
}
