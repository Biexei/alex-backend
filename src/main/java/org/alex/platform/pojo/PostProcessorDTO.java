package org.alex.platform.pojo;

public class PostProcessorDTO {
    private Integer postProcessorId;
    private Integer caseId;
    private String name;
    private Byte type;

    public Integer getPostProcessorId() {
        return postProcessorId;
    }

    public void setPostProcessorId(Integer postProcessorId) {
        this.postProcessorId = postProcessorId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
