package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceCaseSuiteDO;
import org.alex.platform.pojo.InterfaceCaseSuiteDTO;
import org.alex.platform.pojo.InterfaceCaseSuiteVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceCaseSuiteMapper {
    void insertInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    void updateInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    void deleteInterfaceCaseSuiteById(Integer suiteId);

    InterfaceCaseSuiteVO selectInterfaceCaseSuiteById(Integer suiteId);

    List<InterfaceCaseSuiteVO> selectInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO);
}
