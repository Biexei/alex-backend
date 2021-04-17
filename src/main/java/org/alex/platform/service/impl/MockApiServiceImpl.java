package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.MockApiMapper;
import org.alex.platform.mock.InjectionCenter;
import org.alex.platform.mock.MockServerPool;
import org.alex.platform.pojo.*;
import org.alex.platform.service.MockApiService;
import org.alex.platform.service.MockHitPolicyService;
import org.alex.platform.service.MockServerService;
import org.alex.platform.util.ValidUtil;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.Expectation;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MockApiServiceImpl implements MockApiService {
    @Autowired
    MockApiMapper mockApiMapper;
    @Autowired
    MockHitPolicyService mockHitPolicyService;
    @Autowired
    MockServerService mockServerService;
    @Autowired
    InjectionCenter injectionCenter;

    @Override
    public MockApiDO saveMockApi(MockApiDO mockApiDO) throws ValidException {
        checkMockApiDO(mockApiDO);
        Date date = new Date();
        mockApiDO.setCreatedTime(date);
        mockApiDO.setUpdateTime(date);
        Integer serverId = mockApiDO.getServerId();
        String url = mockApiDO.getUrl();
        String method = mockApiDO.getMethod();
        if (method != null) {
            mockApiDO.setMethod(method.toUpperCase());
        }
        if (!mockApiMapper.checkUrlUnion4Insert(serverId, url).isEmpty()) {
            throw new ValidException("该服务url重复");
        }
        mockApiMapper.insertMockApi(mockApiDO);
        return mockApiDO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMockApiAndPolicy(MockApiAndPolicyDO policyDO) throws BusinessException, ParseException, SqlException {
        // 入库
        MockApiDO mockApi = this.saveMockApi(policyDO);
        Integer apiId = mockApi.getApiId();
        List<MockHitPolicyDO> policies = policyDO.getPolicies();
        if (policies != null && !policies.isEmpty()) {
            for(MockHitPolicyDO policy : policies) {
                policy.setApiId(apiId);
                mockHitPolicyService.saveMockHitPolicy(policy);
            }
        }
        // mock server 添加该api
        injectionCenter.injectByDO(policyDO);
    }

    @Override
    public void modifyMockApi(MockApiDO mockApiDO) throws ValidException {
        String url = mockApiDO.getUrl();
        Integer serverId = mockApiDO.getServerId();
        Integer apiId = mockApiDO.getApiId();
        ValidUtil.notNUll(apiId, "参数错误");
        checkMockApiDO(mockApiDO);
        String method = mockApiDO.getMethod();
        if (method != null) {
            mockApiDO.setMethod(method.toUpperCase());
        }
        mockApiDO.setUpdateTime(new Date());
        if (!mockApiMapper.checkUrlUnion4Update(apiId, serverId, url).isEmpty()) {
            throw new ValidException("该服务url重复");
        }
        mockApiMapper.updateMockApi(mockApiDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyMockApiAndPolicy(MockApiAndPolicyDO policyDO) throws BusinessException, ParseException, SqlException {
        Integer apiId = policyDO.getApiId();
        // mock server 删除改之前的api
        injectionCenter.delete(apiId);

        this.modifyMockApi(policyDO);
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

        // mock server 启用
        injectionCenter.injectByDO(policyDO);
    }

    @Override
    public PageInfo<MockApiListVO> findMockApiList(MockApiDTO mockApiDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<MockApiListVO> pages = new PageInfo<>(mockApiMapper.selectMockApiList(mockApiDTO));
        List<MockApiListVO> result  = pages.getList().stream().peek(page -> {
            Integer port = page.getPort();
            Integer apiId = page.getApiId();
            String method = page.getMethod();
            String url = page.getUrl();
            List<MockHitPolicyVO> policies = mockHitPolicyService.findMockHitPolicyByApiId(apiId);
            boolean running = MockServerPool.isRunning(port);
            page.setPortRunning(running);
            try {
                HttpRequest httpRequest = injectionCenter.injectRequest(new HttpRequest(), method, url, policies);
                page.setApiRunning(MockServerPool.apiIsRunning(port, httpRequest, url));
            } catch (BusinessException e) {
                page.setApiRunning(false);
            }
        }).collect(Collectors.toList());
        pages.setList(result);
        return pages;
    }

    @Override
    public List<MockApiListVO> findMockApiByServerId(Integer serverId) {
        return mockApiMapper.selectMockApiByServerId(serverId);
    }

    @Override
    public MockApiInfoVO findMockApiById(Integer apiId) {
        return mockApiMapper.selectMockApiById(apiId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeMockApiById(Integer apiId) {
        // mock server 删除该api
        injectionCenter.delete(apiId);
        mockApiMapper.deleteMockApiById(apiId);
        mockHitPolicyService.removeMockHitPolicyByApiId(apiId);
    }

    @Override
    public void stopApi(Integer apiId) {
        injectionCenter.delete(apiId);
    }

    @Override
    public void restartApi(Integer apiId) throws BusinessException, ParseException, SqlException {
        injectionCenter.delete(apiId);
        injectionCenter.injectById(apiId);
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
        ValidUtil.notNUll(url, "请输入url");
        ValidUtil.length(url, 1, 200, "url长度需为[0, 200]");
        ValidUtil.notNUll(method, "请输入请求方式");
        ValidUtil.length(method, 1, 20, "请求方式长度需为[1, 20]");
        ValidUtil.notNUll(responseCode, "请输入响应状态码");
        if (responseHeaders != null) {
            ValidUtil.length(responseHeaders,0, 2000, "响应头长度需为[1, 2000]");
        }
        ValidUtil.notNUll(responseHeadersEnableRely, "请选择是否解析响应头依赖");
        ValidUtil.size(responseHeadersEnableRely, 0, 1, "是否解析响应头依赖参数错误");
        ValidUtil.notNUll(responseBodyEnableRely, "请选择是否解析响应体依赖");
        ValidUtil.size(responseBodyEnableRely, 0, 1, "是否解析响应体依赖参数错误");
        ValidUtil.notNUll(responseBodyType, "请选择响应体类型");
        ValidUtil.size(responseHeadersEnableRely, 0, 1, "响应体类型参数错误");
    }
}
