package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.*;

import java.util.List;

public interface MockApiService {
    MockApiDO insertMockApi(MockApiDO mockApiDO) throws ValidException;

    void insertMockApiAndPolicy(MockApiAndPolicyDO policyDO) throws ValidException;

    void modifyMockApi(MockApiDO mockApiDO) throws ValidException;

    void modifyMockApiAndPolicy(MockApiAndPolicyDO policyDO) throws ValidException;

    PageInfo<MockApiListVO> findMockApiList(MockApiDTO MockApiDTO, Integer pageNum, Integer pageSize);

    List<MockApiListVO> findMockApiByServerId(Integer serverId);

    MockApiInfoVO findMockApiById(Integer apiId);

    void removeMockApiById(Integer apiId);
}
