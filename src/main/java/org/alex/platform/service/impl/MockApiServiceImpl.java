package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.MockApiMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.MockApiService;
import org.alex.platform.service.MockHitPolicyService;
import org.alex.platform.service.MockServerService;
import org.alex.platform.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MockApiServiceImpl implements MockApiService {
    @Autowired
    MockApiMapper mockApiMapper;
    @Autowired
    MockHitPolicyService mockHitPolicyService;
    @Autowired
    MockServerService mockServerService;

    @Override
    public MockApiDO insertMockApi(MockApiDO mockApiDO) throws ValidException {
        checkMockApiDO(mockApiDO);
        Date date = new Date();
        mockApiDO.setCreatedTime(date);
        mockApiDO.setUpdateTime(date);
        return mockApiDO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertMockApiAndPolicy(MockApiAndPolicyDO policyDO) throws ValidException {
        MockApiDO mockApiDO = policyDO.getMockApiDO();
        MockApiDO mockApi = this.insertMockApi(mockApiDO);
        Integer apiId = mockApi.getApiId();
        List<MockHitPolicyDO> policies = policyDO.getPolicies();
        if (policies != null && !policies.isEmpty()) {
            for(MockHitPolicyDO policy : policies) {
                policy.setApiId(apiId);
                mockHitPolicyService.saveMockHitPolicy(policy);
            }
        }
    }

    @Override
    public void modifyMockApi(MockApiDO mockApiDO) throws ValidException {
        Integer apiId = mockApiDO.getApiId();
        ValidUtil.notNUll(apiId, "参数错误");
        checkMockApiDO(mockApiDO);
        mockApiDO.setUpdateTime(new Date());
        mockApiMapper.updateMockApi(mockApiDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyMockApiAndPolicy(MockApiAndPolicyDO policyDO) throws ValidException {
        MockApiDO mockApiDO = policyDO.getMockApiDO();
        Integer apiId = mockApiDO.getApiId();
        this.modifyMockApi(mockApiDO);
        List<MockHitPolicyDO> policies = policyDO.getPolicies();
        if (policies == null || policies.isEmpty()) {
            mockHitPolicyService.removeMockHitPolicyByApiId(apiId);
        } else {
            List<Integer> policyIdList = mockHitPolicyService.findIdByApiId(apiId);
            for (MockHitPolicyDO policy : policies) {
                policy.setApiId(apiId);
                Integer id = policy.getId();
                mockHitPolicyService.modifyMockHitPolicy(policy);
                if (id == null) {
                    mockHitPolicyService.saveMockHitPolicy(policy);
                } else {
                    // 移出此次新增前的id队列
                    for (int i = policyIdList.size() - 1; i >= 0; i--) {
                        if (policyIdList.get(i).equals(id)) {
                            policyIdList.remove(i);
                        }
                    }
                }
            }
            for (Integer id : policyIdList) {
                mockHitPolicyService.removeMockHitPolicyById(id);
            }
        }
    }

    @Override
    public PageInfo<MockApiListVO> findMockApiList(MockApiDTO mockApiDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(mockApiMapper.selectMockApiList(mockApiDTO));
    }

    @Override
    public List<MockApiListVO> findMockApiByServerId(Integer serverId) {
        return mockApiMapper.selectMockApiByServerId(serverId);
    }

    @Override
    public MockApiInfoVO findMockApiById(Integer apiId) {
        return mockApiMapper.selectMockApiById(apiId);
    }

    @Override
    public void removeMockApiById(Integer apiId) {
        mockApiMapper.deleteMockApiById(apiId);
    }

    private void checkMockApiDO(MockApiDO mockApiDO) throws ValidException {
        Integer serverId = mockApiDO.getServerId();
        String url = mockApiDO.getUrl();
        String method = mockApiDO.getMethod();
        Integer responseCode = mockApiDO.getResponseCode();
        String responseHeaders = mockApiDO.getResponseHeaders();
        Byte responseBodyEnableRely = mockApiDO.getResponseBodyEnableRely();
        Byte responseHeadersEnableRely = mockApiDO.getResponseHeadersEnableRely();
        Byte responseBodyType = mockApiDO.getResponseBodyType();
        ValidUtil.notNUll(serverId, "请选择服务编号");
        ValidUtil.notNUll(mockServerService.findMockServerById(serverId), "请选择服务编号");
        ValidUtil.notNUll(url, "请输入URL");
        ValidUtil.length(url, 1, 200, "URL长度需为[0, 200]");
        ValidUtil.notNUll(method, "请输入请求方式");
        ValidUtil.notEmpty(method, "请输入请求方式");
        ValidUtil.length(url, 1, 20, "URL长度需为[1, 20]");
        ValidUtil.notNUll(responseCode, "请输入响应状态码");
        if (responseHeaders != null) {
            ValidUtil.length(responseHeaders,0, 2000, "响应头长度需为[1, 2000]");
        }
        ValidUtil.notNUll(responseBodyEnableRely, "请选择是否解析响应体依赖");
        ValidUtil.size(responseBodyEnableRely, 0, 1, "是否解析响应体依赖参数错误");
        ValidUtil.notNUll(responseHeadersEnableRely, "请选择是否解析响应头依赖");
        ValidUtil.size(responseHeadersEnableRely, 0, 1, "是否解析响应头依赖参数错误");
        ValidUtil.notNUll(responseBodyType, "请选择响应体类型");
        ValidUtil.size(responseHeadersEnableRely, 0, 1, "响应体类型参数错误");
    }
}
