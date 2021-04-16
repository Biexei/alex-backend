package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.List;

public class MockApiAndPolicyDO extends MockApiDO implements Serializable {
    private List<MockHitPolicyDO> policies;

    public List<MockHitPolicyDO> getPolicies() {
        return policies;
    }

    public void setPolicies(List<MockHitPolicyDO> policies) {
        this.policies = policies;
    }
}
