package org.alex.platform.service;

import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.MockHitPolicyDO;
import org.alex.platform.pojo.MockHitPolicyDTO;
import org.alex.platform.pojo.MockHitPolicyVO;

import java.util.List;

public interface MockHitPolicyService {
    void saveMockHitPolicy(MockHitPolicyDO mockHitPolicyDO) throws ValidException;

    void modifyMockHitPolicy(MockHitPolicyDO mockHitPolicyDO) throws ValidException;

    List<MockHitPolicyVO> findMockHitPolicy(MockHitPolicyDTO mockHitPolicyDTO);

    MockHitPolicyVO findMockHitPolicyById(Integer id);

    void removeMockHitPolicyById(Integer id);

    List<MockHitPolicyVO> findMockHitPolicyByApiId(Integer apiId);

    void removeMockHitPolicyByApiId(Integer apiId);

    List<Integer> findIdByApiId(Integer apiId);
}
