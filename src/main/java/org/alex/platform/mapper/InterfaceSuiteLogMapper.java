package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceSuiteLogDO;
import org.alex.platform.pojo.InterfaceSuiteLogDTO;
import org.alex.platform.pojo.InterfaceSuiteLogVO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface InterfaceSuiteLogMapper {
    ArrayList<InterfaceSuiteLogVO> selectIfSuiteLog(InterfaceSuiteLogDTO interfaceSuiteLogDTO);

    InterfaceSuiteLogVO selectIfSuiteLogByNo(String suiteLogNo);

    InterfaceSuiteLogVO selectIfSuiteLogById(Integer id);

    Integer insertIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO);
}
