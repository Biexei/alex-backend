package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.List;

public class InterfaceCaseExecuteLogVO extends InterfaceCaseExecuteLogDO implements Serializable {
    private List<InterfaceAssertLogListVO> assertList;

    public List<InterfaceAssertLogListVO> getAssertList() {
        return assertList;
    }

    public void setAssertList(List<InterfaceAssertLogListVO> assertList) {
        this.assertList = assertList;
    }
}
