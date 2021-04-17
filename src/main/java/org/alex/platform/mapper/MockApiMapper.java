package org.alex.platform.mapper;

import org.alex.platform.pojo.MockApiDO;
import org.alex.platform.pojo.MockApiDTO;
import org.alex.platform.pojo.MockApiInfoVO;
import org.alex.platform.pojo.MockApiListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockApiMapper {
    Integer insertMockApi(MockApiDO mockApiDO);

    void updateMockApi(MockApiDO mockApiDO);

    List<MockApiListVO> selectMockApiList(MockApiDTO MockApiDTO);

    List<MockApiListVO> selectMockApiByServerId(Integer serverId);

    MockApiInfoVO selectMockApiById(Integer apiId);

    void deleteMockApiById(Integer apiId);

    List<MockApiListVO> checkUrlUnion4Insert(@Param("serverId") Integer serverId, @Param("url") String url);

    List<MockApiListVO> checkUrlUnion4Update(@Param("apiId") Integer apiId, @Param("serverId") Integer serverId, @Param("url") String url);

}
