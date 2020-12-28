package org.alex.platform.pojo.template;

import org.alex.platform.pojo.InterfaceAssertDO;
import org.alex.platform.pojo.InterfaceCaseDO;

import java.util.List;

public class ImportCaseTemplate extends InterfaceCaseDO {
    private List<InterfaceAssertDO> asserts;

    public List<InterfaceAssertDO> getAsserts() {
        return asserts;
    }

    public void setAsserts(List<InterfaceAssertDO> asserts) {
        this.asserts = asserts;
    }
}
