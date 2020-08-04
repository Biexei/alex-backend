package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.List;

public class InterfaceCaseDTO extends InterfaceCaseDO implements Serializable {
    private List<InterfaceAssertDO> asserts;

    public List<InterfaceAssertDO> getAsserts() {
        return asserts;
    }

    public void setAsserts(List<InterfaceAssertDO> asserts) {
        this.asserts = asserts;
    }
}
