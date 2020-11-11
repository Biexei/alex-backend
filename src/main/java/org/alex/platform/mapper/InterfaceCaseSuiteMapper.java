package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceCaseSuiteDO;
import org.alex.platform.pojo.InterfaceCaseSuiteDTO;
import org.alex.platform.pojo.InterfaceCaseSuiteVO;
import org.alex.platform.pojo.InterfaceSuiteInfoVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceCaseSuiteMapper {
    Integer insertInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    void updateInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    void deleteInterfaceCaseSuiteById(Integer suiteId);

    InterfaceCaseSuiteVO selectInterfaceCaseSuiteById(Integer suiteId);

    InterfaceSuiteInfoVO selectInterfaceCaseSuiteInfoById(Integer suiteId);

    List<InterfaceCaseSuiteVO> selectInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO);
}
