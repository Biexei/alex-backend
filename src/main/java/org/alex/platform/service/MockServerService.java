package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.MockServerDO;
import org.alex.platform.pojo.MockServerDTO;
import org.alex.platform.pojo.MockServerVO;

import java.util.List;

public interface MockServerService {
    void saveMockServer(MockServerDO mockServerDO) throws ValidException;

    void modifyMockServer(MockServerDO mockServerDO) throws ValidException;

    List<MockServerVO> findAllMockServer(MockServerDTO mockServerDTO);

    PageInfo<MockServerVO> findMockServer(MockServerDTO mockServerDTO, Integer pageNum, Integer pageSize);

    MockServerVO findMockServerById(Integer serverId);

    void removeMockServer(Integer serverId) throws BusinessException;

    MockServerVO checkPortUnique(Integer serverId, Integer port);

    void stopMockServer(Integer serverId);

    void startMockServer(Integer serverId) throws BusinessException;

    void forceStartMockServer(Integer serverId) throws BusinessException;

    void stopAllMockServer();

    void startAllMockServer() throws BusinessException;

}
