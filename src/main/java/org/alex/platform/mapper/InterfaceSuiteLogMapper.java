package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceSuiteLogDO;
import org.alex.platform.pojo.InterfaceSuiteLogDTO;
import org.alex.platform.pojo.InterfaceSuiteLogVO;
import org.alex.platform.pojo.InterfaceSuiteSummaryDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface InterfaceSuiteLogMapper {
    ArrayList<InterfaceSuiteLogVO> selectIfSuiteLog(InterfaceSuiteLogDTO interfaceSuiteLogDTO);

    InterfaceSuiteLogVO selectIfSuiteLogByNo(String suiteLogNo);

    InterfaceSuiteLogVO selectIfSuiteLogById(Integer id);

    Integer insertIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO);

    Integer updateIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO);

    ArrayList<HashMap<String, Object>> selectSuiteLogProjectModule(String suiteLogNo);

    ArrayList<HashMap<String, Object>> selectSuiteLogSummary(InterfaceSuiteSummaryDTO interfaceSuiteSummaryDTO);
}
