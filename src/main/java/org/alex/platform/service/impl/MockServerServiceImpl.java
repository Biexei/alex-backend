package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.MockServerMapper;
import org.alex.platform.mock.MockServerPool;
import org.alex.platform.pojo.MockApiListVO;
import org.alex.platform.pojo.MockServerDO;
import org.alex.platform.pojo.MockServerDTO;
import org.alex.platform.pojo.MockServerVO;
import org.alex.platform.service.MockApiService;
import org.alex.platform.service.MockServerService;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MockServerServiceImpl implements MockServerService {
    @Autowired
    MockServerMapper mockServerMapper;
    @Autowired
    MockApiService mockApiService;

    private static final Logger LOG = LoggerFactory.getLogger(MockServerServiceImpl.class);

    /**
     * 保存mock server
     * @param mockServerDO MockServerDO
     */
    @Override
    public void saveMockServer(MockServerDO mockServerDO) throws ValidException {
        checkDO(mockServerDO);
        Date date = new Date();
        mockServerDO.setCreatedTime(date);
        mockServerDO.setUpdateTime(date);
        MockServerDTO dto = new MockServerDTO();
        dto.setPort(mockServerDO.getPort());
        List<MockServerVO> vos = mockServerMapper.selectMockServer(dto);
        if (vos == null || vos.isEmpty()) {
            mockServerMapper.insertMockServer(mockServerDO);
        } else {
            throw new ValidException("该节点已存在");
        }
    }

    /**
     * 修改mock server
     * @param mockServerDO MockServerDO
     */
    @Override
    public void modifyMockServer(MockServerDO mockServerDO) throws ValidException {
        checkDO(mockServerDO);
        mockServerDO.setUpdateTime(new Date());
        Integer serverId = mockServerDO.getServerId();
        Integer port = mockServerDO.getPort();
        if (mockServerMapper.checkPortUnique(serverId, port) == null) {
            mockServerMapper.updateMockServer(mockServerDO);
            // 关闭未修改前的端口
            MockServerVO vo = mockServerMapper.selectMockServerById(serverId);
            MockServerPool.stop(vo.getPort());
            // 关闭修改后的端口
            MockServerPool.stop(port);
        } else {
            throw new ValidException("该节点已存在");
        }
    }

    /**
     * 查看所有的mock server
     * @param mockServerDTO MockServerDTO
     * @return 所有的mock server
     */
    @Override
    public List<MockServerVO> findAllMockServer(MockServerDTO mockServerDTO) {
        List<MockServerVO> vos = mockServerMapper.selectMockServer(mockServerDTO);
        return vos.stream().peek(vo -> {
            Integer port = vo.getPort();
            byte status = (byte) (MockServerPool.isRunning(port) ? 0 : 1);
            vo.setStatus(status);
        }).collect(Collectors.toList());
    }

    /**
     * 分页查看所有的mock server
     * @param mockServerDTO MockServerDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return 分页查看所有的mock server
     */
    @Override
    public PageInfo<MockServerVO> findMockServer(MockServerDTO mockServerDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<MockServerVO> pages = new PageInfo<>(mockServerMapper.selectMockServer(mockServerDTO));
        List<MockServerVO> result  = pages.getList().stream().peek(page -> {
            Integer port = page.getPort();
            byte status = (byte) (MockServerPool.isRunning(port) ? 0 : 1);
            page.setStatus(status);
        }).collect(Collectors.toList());
        pages.setList(result);
        return pages;
    }

    /**
     * 查看mock server基本信息
     * @param serverId  serverId
     * @return MockServerVO
     */
    @Override
    public MockServerVO findMockServerById(Integer serverId) {
        MockServerVO vo = mockServerMapper.selectMockServerById(serverId);
        Integer port = vo.getPort();
        byte status = (byte) (MockServerPool.isRunning(port) ? 0 : 1);
        vo.setStatus(status);
        return vo;
    }

    /**
     * 删除mock server
     * @param serverId serverId
     */
    @Override
    public void removeMockServer(Integer serverId) throws BusinessException {
        MockServerVO mockServerVO = mockServerMapper.selectMockServerById(serverId);
        List<MockApiListVO> mockApi = mockApiService.findMockApiByServerId(serverId);
        if (!mockApi.isEmpty()) {
            throw new BusinessException("请先删除其关联的mock api");
        }
        Integer port = mockServerVO.getPort();
        // 关闭mock server
        MockServerPool.stop(port);
        mockServerMapper.deleteMockServer(serverId);
        LOG.info("删除并停用mock serverId: {}", serverId);
    }

    /**
     * 检查port的唯一性 null则代表未被占用
     * @param serverId serverId
     * @param port port
     * @return MockServerVO
     */
    @Override
    public MockServerVO checkPortUnique(Integer serverId, Integer port) {
        return mockServerMapper.checkPortUnique(serverId, port);
    }

    /**
     * 停止mock server
     * @param serverId serverId
     */
    @Override
    public void stopMockServer(Integer serverId) {
        MockServerVO mockServerVO = mockServerMapper.selectMockServerById(serverId);
        Integer port = mockServerVO.getPort();
        // 关闭mock server
        MockServerPool.stop(port);
        LOG.info("成功停用mock serverId: {}", serverId);
    }

    /**
     * 启动mock server
     * @param serverId serverId
     */
    @Override
    public void startMockServer(Integer serverId) throws BusinessException {
        MockServerVO mockServerVO = mockServerMapper.selectMockServerById(serverId);
        Integer port = mockServerVO.getPort();
        String remoteHost = mockServerVO.getRemoteHost();
        Integer remotePort = mockServerVO.getRemotePort();
        // 启动mock server
        if (remoteHost == null || "".equals(remoteHost) || remotePort == null) {
            MockServerPool.start(port);
        } else {
            MockServerPool.start(remoteHost, remotePort, port);
        }
        LOG.info("成功启用mock serverId: {}", serverId);
    }

    /**
     * 强制启用mock server
     * @param serverId serverId
     */
    @Override
    public void forceStartMockServer(Integer serverId) throws BusinessException {
        MockServerVO mockServerVO = mockServerMapper.selectMockServerById(serverId);
        Integer port = mockServerVO.getPort();
        String remoteHost = mockServerVO.getRemoteHost();
        Integer remotePort = mockServerVO.getRemotePort();
        // 强制启动mock server
        if (remoteHost == null || "".equals(remoteHost) || remotePort == null) {
            MockServerPool.get(port);
        } else {
            MockServerPool.get(remoteHost, remotePort, port);
        }
        LOG.info("成功强制启用mock serverId: {}", serverId);
    }

    /**
     * 关闭所有mock server
     */
    @Override
    public void stopAllMockServer() {
        MockServerPool.stopAllMockSever();
    }

    /**
     * 启用所有mock server
     */
    @Override
    public void startAllMockServer() throws BusinessException {
        List<MockServerVO> vos = mockServerMapper.selectMockServer(null);
        for (MockServerVO vo : vos) {
            Integer port = vo.getPort();
            String remoteHost = vo.getRemoteHost();
            Integer remotePort = vo.getRemotePort();
            if (remoteHost == null || "".equals(remoteHost) || remotePort == null) {
                MockServerPool.get(port);
            } else {
                MockServerPool.get(remoteHost, remotePort, port);
            }
        }
        LOG.info("成功启用全部mock server");
    }

    public void checkDO(MockServerDO mockServerDO) throws ValidException {
        Integer port = mockServerDO.getPort();
        ValidUtil.notNUll(port, "端口号不能为空");
    }
}

