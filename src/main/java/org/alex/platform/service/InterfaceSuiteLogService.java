package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.InterfaceSuiteLogDTO;
import org.alex.platform.pojo.InterfaceSuiteLogDO;
import org.alex.platform.pojo.InterfaceSuiteLogVO;
import org.alex.platform.pojo.InterfaceSuiteSummaryDTO;

import java.util.ArrayList;
import java.util.HashMap;


public interface InterfaceSuiteLogService {
    PageInfo<InterfaceSuiteLogVO> findIfSuiteLog(InterfaceSuiteLogDTO interfaceSuiteLogDTO, Integer pageNum, Integer pageSize);

    InterfaceSuiteLogVO findIfSuiteLogByNo(String suiteLogNo);

    InterfaceSuiteLogVO findIfSuiteLogById(Integer id);

    InterfaceSuiteLogDO saveIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO);

    void modifyIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO);

    ArrayList<HashMap<String, Object>> findSuiteLogSummary(String suiteLogNo);

    HashMap<String, Object> findSuiteReportAssert(String suiteLogNo);
}
