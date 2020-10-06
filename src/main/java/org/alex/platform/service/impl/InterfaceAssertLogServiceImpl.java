package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.InterfaceAssertLogMapper;
import org.alex.platform.pojo.InterfaceAssertLogDO;
import org.alex.platform.pojo.InterfaceAssertLogDTO;
import org.alex.platform.pojo.InterfaceAssertLogVO;
import org.alex.platform.service.InterfaceAssertLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceAssertLogServiceImpl implements InterfaceAssertLogService {
    @Autowired
    InterfaceAssertLogMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceAssertLogServiceImpl.class);

    /**
     * 新增断言日志
     *
     * @param interfaceAssertLogDO interfaceAssertLogDO
     */
    @Override
    public void saveInterfaceAssertLog(InterfaceAssertLogDO interfaceAssertLogDO) {
        mapper.insertInterfaceAssertLog(interfaceAssertLogDO);
    }

    /**
     * 获取断言日志列表
     *
     * @param interfaceAssertLogDTO interfaceAssertLogDTO
     * @param pageNum               pageNum
     * @param pageSize              pageSize
     * @return PageInfo<InterfaceAssertLogVO>
     */
    @Override
    public PageInfo<InterfaceAssertLogVO> findInterfaceAssertLogList(InterfaceAssertLogDTO interfaceAssertLogDTO,
                                                                     Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(mapper.selectInterfaceAssertLogList(interfaceAssertLogDTO));
    }
}
