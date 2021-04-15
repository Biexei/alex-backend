package org.alex.platform.mapper;

import org.alex.platform.pojo.MockHitPolicyDO;
import org.alex.platform.pojo.MockHitPolicyDTO;
import org.alex.platform.pojo.MockHitPolicyVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockHitPolicyMapper {
    void insertMockHitPolicy(MockHitPolicyDO mockHitPolicyDO);

    void updateMockHitPolicy(MockHitPolicyDO mockHitPolicyDO);

    List<MockHitPolicyVO> selectMockHitPolicy(MockHitPolicyDTO mockHitPolicyDTO);

    MockHitPolicyVO selectMockHitPolicyById(Integer id);

    void deleteMockHitPolicyById(Integer id);

    List<MockHitPolicyVO> selectMockHitPolicyByApiId(Integer apiId);

    List<Integer> selectIdByApiId(Integer apiId);

    void deleteMockHitPolicyByApiId(Integer apiId);
}
