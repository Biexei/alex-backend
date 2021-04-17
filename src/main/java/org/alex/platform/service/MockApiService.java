package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.*;

import java.util.List;

public interface MockApiService {
    MockApiDO saveMockApi(MockApiDO mockApiDO) throws ValidException;

    void saveMockApiAndPolicy(MockApiAndPolicyDO policyDO) throws BusinessException, ParseException, SqlException;

    void modifyMockApi(MockApiDO mockApiDO) throws ValidException;

    void modifyMockApiAndPolicy(MockApiAndPolicyDO policyDO) throws BusinessException, ParseException, SqlException;

    PageInfo<MockApiListVO> findMockApiList(MockApiDTO mockApiDTO, Integer pageNum, Integer pageSize);

    List<MockApiListVO> findMockApiByServerId(Integer serverId);

    MockApiInfoVO findMockApiById(Integer apiId);

    void removeMockApiById(Integer apiId);

    void stopApi(Integer apiId);

    void restartApi(Integer apiId) throws BusinessException, ParseException, SqlException;
}
