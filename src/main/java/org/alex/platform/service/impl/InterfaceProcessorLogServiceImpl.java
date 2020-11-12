package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.InterfaceProcessorLogMapper;
import org.alex.platform.pojo.InterfaceProcessorLogDO;
import org.alex.platform.pojo.InterfaceProcessorLogDTO;
import org.alex.platform.pojo.InterfaceProcessorLogVO;
import org.alex.platform.service.InterfaceProcessorLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InterfaceProcessorLogServiceImpl implements InterfaceProcessorLogService {
    @Autowired
    private InterfaceProcessorLogMapper interfaceProcessorLogMapper;

    private static final Logger LOG = LoggerFactory.getLogger(InterfaceProcessorLogServiceImpl.class);

    /**
     * 获取调用后置处理器详情
     *
     * @param id 后置处理器调用日志编号
     * @return PostProcessorLogVO
     */
    @Override
    public InterfaceProcessorLogVO findInterfaceProcessorLogById(Integer id) {
        return interfaceProcessorLogMapper.selectInterfaceProcessorLogById(id);
    }

    /**
     * 获取后置处理器调用日志列表
     *
     * @param interfaceProcessorLogDTO postProcessorLogDTO
     * @param pageNum             pageNum
     * @param pageSize            pageSize
     * @return PageInfo<PostProcessorLogVO>
     */
    @Override
    public PageInfo<InterfaceProcessorLogVO> findInterfaceProcessorLogList(InterfaceProcessorLogDTO interfaceProcessorLogDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(interfaceProcessorLogMapper.selectInterfaceProcessorLogList(interfaceProcessorLogDTO));
    }

    /**
     * 获取后置处理器调用日志列表不分页
     *
     * @param interfaceProcessorLogDTO postProcessorLogDTO
     * @return List<PostProcessorLogVO>
     */
    @Override
    public List<InterfaceProcessorLogVO> findInterfaceProcessorLogListAll(InterfaceProcessorLogDTO interfaceProcessorLogDTO) {
        return interfaceProcessorLogMapper.selectInterfaceProcessorLogList(interfaceProcessorLogDTO);
    }

    /**
     * 新增后置调用处理器调用日志，并返回自增对象
     *
     * @param interfaceProcessorLogDO postProcessorLogDO
     * @return 自增对象
     */
    @Override
    public InterfaceProcessorLogDO saveInterfaceProcessorLog(InterfaceProcessorLogDO interfaceProcessorLogDO) {
        interfaceProcessorLogDO.setCreatedTime(new Date());
        interfaceProcessorLogMapper.insertInterfaceProcessorLog(interfaceProcessorLogDO);
        LOG.info("后置处理器日志新增成功");
        return interfaceProcessorLogDO;
    }
}
