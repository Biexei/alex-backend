package org.alex.platform.pojo;

import java.util.List;

public class MockApiAndPolicyDO {
    private MockApiDO mockApiDO;
    private List<MockHitPolicyDO> policies;

    public List<MockHitPolicyDO> getPolicies() {
        return policies;
    }

    public void setPolicies(List<MockHitPolicyDO> policies) {
        this.policies = policies;
    }

    public MockApiDO getMockApiDO() {
        return mockApiDO;
    }

    public void setMockApiDO(MockApiDO mockApiDO) {
        this.mockApiDO = mockApiDO;
    }
}
