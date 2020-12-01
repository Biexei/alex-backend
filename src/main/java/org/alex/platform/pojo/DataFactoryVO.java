package org.alex.platform.pojo;

import java.io.Serializable;

public class DataFactoryVO extends DataFactoryDO implements Serializable {
    // 数据源名称、接口测试套件名称、UI测试套件名称
    private String elementName;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
