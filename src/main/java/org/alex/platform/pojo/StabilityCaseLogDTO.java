package org.alex.platform.pojo;

public class StabilityCaseLogDTO {
    private Integer stabilityTestId;
    private String stabilityTestDesc;
    private Integer stabilityTestLogId;
    private String stabilityTestLogNo;
    private Byte status;
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

    public String getStabilityTestDesc() {
        return stabilityTestDesc;
    }

    public void setStabilityTestDesc(String stabilityTestDesc) {
        this.stabilityTestDesc = stabilityTestDesc;
    }

    public Integer getStabilityTestLogId() {
        return stabilityTestLogId;
    }

    public void setStabilityTestLogId(Integer stabilityTestLogId) {
        this.stabilityTestLogId = stabilityTestLogId;
    }

    public String getStabilityTestLogNo() {
        return stabilityTestLogNo;
    }

    public void setStabilityTestLogNo(String stabilityTestLogNo) {
        this.stabilityTestLogNo = stabilityTestLogNo;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
