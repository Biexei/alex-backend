package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.InterfaceSuiteLogMapper;
import org.alex.platform.pojo.InterfaceSuiteLogDO;
import org.alex.platform.pojo.InterfaceSuiteLogDTO;
import org.alex.platform.pojo.InterfaceSuiteLogVO;
import org.alex.platform.service.InterfaceSuiteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterfaceSuiteLogServiceImpl implements InterfaceSuiteLogService {
    @Autowired
    InterfaceSuiteLogMapper interfaceSuiteLogMapper;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceSuiteLogServiceImpl.class);

    /**
     * 查询测试套件执行日志列表
     *
     * @param interfaceSuiteLogDTO interfaceSuiteLogDTO
     * @param pageNum                  pageNum
     * @param pageSize                 pageSize
     * @return PageInfo
     */
    @Override
    public PageInfo<InterfaceSuiteLogVO> findIfSuiteLog(InterfaceSuiteLogDTO interfaceSuiteLogDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(interfaceSuiteLogMapper.selectIfSuiteLog(interfaceSuiteLogDTO));
    }

    /**
     * 根据测试套件执行日志编号查询
     *
     * @param suiteLogNo suiteLogNo
     * @return InterfaceSuiteLogVO
     */
    @Override
    public InterfaceSuiteLogVO findIfSuiteLogByNo(String suiteLogNo) {
        return interfaceSuiteLogMapper.selectIfSuiteLogByNo(suiteLogNo);
    }

    /**
     * 根据测试套件执行日志ID查询
     *
     * @param id id
     * @return InterfaceSuiteLogVO
     */
    @Override
    public InterfaceSuiteLogVO findIfSuiteLogById(Integer id) {
        return interfaceSuiteLogMapper.selectIfSuiteLogById(id);
    }

    /**
     * 新增测试套件执行日志
     *
     * @param interfaceSuiteLogDO interfaceSuiteLogDO
     * @return InterfaceSuiteLogDO
     */
    @Override
    public InterfaceSuiteLogDO saveIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO) {
        interfaceSuiteLogMapper.insertIfSuiteLog(interfaceSuiteLogDO);
        return interfaceSuiteLogDO;
    }
}
