package org.alex.platform.service.impl;

import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.MockHitPolicyMapper;
import org.alex.platform.pojo.MockHitPolicyDO;
import org.alex.platform.pojo.MockHitPolicyDTO;
import org.alex.platform.pojo.MockHitPolicyVO;
import org.alex.platform.service.MockHitPolicyService;
import org.alex.platform.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockHitPolicyServiceImpl implements MockHitPolicyService {
    @Autowired
    MockHitPolicyMapper policyMapper;

    @Override
    public void saveMockHitPolicy(MockHitPolicyDO mockHitPolicyDO) throws ValidException {
        this.checkDO(mockHitPolicyDO);
        Byte matchScope = mockHitPolicyDO.getMatchScope();
        if (matchScope == 1) {
            mockHitPolicyDO.setName(null);
        }
        policyMapper.insertMockHitPolicy(mockHitPolicyDO);
    }

    @Override
    public void modifyMockHitPolicy(MockHitPolicyDO mockHitPolicyDO) throws ValidException {
        this.checkDO(mockHitPolicyDO);
        Byte matchScope = mockHitPolicyDO.getMatchScope();
        if (matchScope == 1) {
            mockHitPolicyDO.setName(null);
        }
        policyMapper.updateMockHitPolicy(mockHitPolicyDO);
    }

    @Override
    public List<MockHitPolicyVO> findMockHitPolicy(MockHitPolicyDTO mockHitPolicyDTO) {
        return policyMapper.selectMockHitPolicy(mockHitPolicyDTO);
    }

    @Override
    public MockHitPolicyVO findMockHitPolicyById(Integer id) {
        return policyMapper.selectMockHitPolicyById(id);
    }

    @Override
    public void removeMockHitPolicyById(Integer id) {
        policyMapper.deleteMockHitPolicyById(id);
    }

    @Override
    public List<MockHitPolicyVO> findMockHitPolicyByApiId(Integer apiId) {
        return policyMapper.selectMockHitPolicyByApiId(apiId);
    }

    @Override
    public void removeMockHitPolicyByApiId(Integer apiId) {
        policyMapper.deleteMockHitPolicyByApiId(apiId);
    }

    @Override
    public List<Integer> findIdByApiId(Integer apiId) {
        return policyMapper.selectIdByApiId(apiId);
    }

    private void checkDO(MockHitPolicyDO mockHitPolicyDO) throws ValidException {
        Integer apiId = mockHitPolicyDO.getApiId();
        Byte matchScope = mockHitPolicyDO.getMatchScope();
        Byte matchType = mockHitPolicyDO.getMatchType();
        String value = mockHitPolicyDO.getValue();
        Byte status = mockHitPolicyDO.getStatus();

        ValidUtil.notNUll(apiId, "接口编号不能为空");
        ValidUtil.notNUll(matchScope, "作用域不能为空");
        ValidUtil.size(matchScope, 0, 3, "作用域参数错误");
        ValidUtil.notNUll(matchType, "作用类型不能为空");
        ValidUtil.size(matchType, 0, 5, "作用类型参数错误");
        ValidUtil.notNUll(status, "状态不能为空");
        ValidUtil.size(status, 0, 1, "状态参数错误");
    }
}
