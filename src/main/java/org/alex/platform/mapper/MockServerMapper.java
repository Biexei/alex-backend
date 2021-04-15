package org.alex.platform.mapper;

import org.alex.platform.pojo.MockServerDO;
import org.alex.platform.pojo.MockServerDTO;
import org.alex.platform.pojo.MockServerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockServerMapper {
    void insertMockServer(MockServerDO mockServerDO);

    void updateMockServer(MockServerDO mockServerDO);

    List<MockServerVO> selectMockServer(MockServerDTO mockServerDTO);

    MockServerVO selectMockServerById(Integer serverId);

    void deleteMockServer(Integer serverId);

    MockServerVO checkPortUnique(@Param("serverId") Integer serverId, @Param("port") Integer port);
}
