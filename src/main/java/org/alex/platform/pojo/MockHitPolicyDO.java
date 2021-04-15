package org.alex.platform.pojo;

import java.io.Serializable;

public class MockHitPolicyDO implements Serializable {
    private Integer id;
    private Integer apiId;
    private Byte matchScope;
    private Byte matchType;
    private String name;
    private String value;
    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Byte getMatchScope() {
        return matchScope;
    }

    public void setMatchScope(Byte matchScope) {
        this.matchScope = matchScope;
    }

    public Byte getMatchType() {
        return matchType;
    }

    public void setMatchType(Byte matchType) {
        this.matchType = matchType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
