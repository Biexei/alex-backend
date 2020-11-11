package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.List;

public class InterfaceSuiteInfoDTO extends InterfaceCaseSuiteDO implements Serializable {
    private List<InterfaceSuiteProcessorDO> suiteProcessors;

    public List<InterfaceSuiteProcessorDO> getSuiteProcessors() {
        return suiteProcessors;
    }

    public void setSuiteProcessors(List<InterfaceSuiteProcessorDO> suiteProcessors) {
        this.suiteProcessors = suiteProcessors;
    }
}
