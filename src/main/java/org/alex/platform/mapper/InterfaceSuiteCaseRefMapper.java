package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceSuiteCaseRefDO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDTO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceSuiteCaseRefMapper {
    void insertSuiteCase(List<InterfaceSuiteCaseRefDO> interfaceSuiteCaseRefDOList);

    void deleteSuiteCase(Integer incrementKey);

    List<InterfaceSuiteCaseRefVO> selectSuiteCaseList(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO);
}
