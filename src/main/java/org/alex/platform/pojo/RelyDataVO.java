package org.alex.platform.pojo;

import java.io.Serializable;

public class RelyDataVO extends RelyDataDO implements Serializable {
    private String dbName;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
