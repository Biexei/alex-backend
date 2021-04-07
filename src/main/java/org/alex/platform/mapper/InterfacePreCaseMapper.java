package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfacePreCaseDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfacePreCaseMapper {
    void insertInterfacePreCase(InterfacePreCaseDO interfacePreCaseDO);

    void updateInterfacePreCase(InterfacePreCaseDO interfacePreCaseDO);

    List<InterfacePreCaseDO> selectInterfacePreCaseByParentId(Integer parentCaseId);

    void deleteInterfacePreCaseById(Integer id);

    void deleteInterfacePreCaseByParentId(Integer parentCaseId);

    List<Integer> selectInterfacePreIdByParentId(Integer parentCaseId);

    List<Integer> selectInterfacePreCaseIdById(Integer id);

    List<Integer> selectInterfacePreCaseIdByParentId(Integer parentCaseId);
}
