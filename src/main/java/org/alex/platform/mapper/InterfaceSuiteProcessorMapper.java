package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceSuiteProcessorDO;
import org.alex.platform.pojo.InterfaceSuiteProcessorDTO;
import org.alex.platform.pojo.InterfaceSuiteProcessorVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceSuiteProcessorMapper {
    Integer insertInterfaceSuiteProcessor(InterfaceSuiteProcessorDO interfaceSuiteProcessorDO);

    void updateInterfaceSuiteProcessor(InterfaceSuiteProcessorDO interfaceSuiteProcessorDO);

    List<InterfaceSuiteProcessorVO> selectInterfaceSuiteProcessorList(InterfaceSuiteProcessorDTO interfaceSuiteProcessorDTO);

    InterfaceSuiteProcessorVO selectInterfaceSuiteProcessorById(Integer id);

    List<InterfaceSuiteProcessorVO> selectInterfaceSuiteProcessorBySuiteId(Integer suiteId);

    void deleteInterfaceSuiteProcessorById(Integer id);

    void deleteInterfaceSuiteProcessorBySuiteId(Integer suiteId);
}
