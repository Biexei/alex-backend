package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.InterfaceSuiteLogDTO;
import org.alex.platform.pojo.InterfaceSuiteLogDO;
import org.alex.platform.pojo.InterfaceSuiteLogVO;


public interface InterfaceSuiteLogService {
    PageInfo<InterfaceSuiteLogVO> findIfSuiteLog(InterfaceSuiteLogDTO interfaceSuiteLogDTO, Integer pageNum, Integer pageSize);

    InterfaceSuiteLogVO findIfSuiteLogByNo(String suiteLogNo);

    InterfaceSuiteLogVO findIfSuiteLogById(Integer id);

    InterfaceSuiteLogDO saveIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO);
}
