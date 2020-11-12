package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.List;

public class InterfaceCaseDTO extends InterfaceCaseDO implements Serializable {
    private List<InterfaceAssertDO> asserts;
    private List<InterfaceProcessorDO> postProcessors;

    public List<InterfaceProcessorDO> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(List<InterfaceProcessorDO> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public List<InterfaceAssertDO> getAsserts() {
        return asserts;
    }

    public void setAsserts(List<InterfaceAssertDO> asserts) {
        this.asserts = asserts;
    }
}
