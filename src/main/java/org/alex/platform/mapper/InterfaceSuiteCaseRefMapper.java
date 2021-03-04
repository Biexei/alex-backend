package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceSuiteCaseRefDO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDTO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface InterfaceSuiteCaseRefMapper {
    void insertSuiteCase(List<InterfaceSuiteCaseRefDO> interfaceSuiteCaseRefDOList);

    void insertSuiteCaseSingle(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO);

    void modifySuiteCase(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO);

    void deleteSuiteCase(Integer incrementKey);

    void deleteSuiteCaseByObject(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO);

    List<InterfaceSuiteCaseRefVO> selectSuiteCaseList(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO);

    List<InterfaceSuiteCaseRefDO> selectSuiteAllCase(Integer suiteId);
}
