package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfacePreCaseDO;

import java.util.List;

public interface InterfacePreCaseService {
    void saveInterfacePreCase(InterfacePreCaseDO interfacePreCaseDO) throws BusinessException;

    void modifyInterfacePreCase(InterfacePreCaseDO interfacePreCaseDO) throws BusinessException;

    List<InterfacePreCaseDO> findInterfacePreCaseByParentId(Integer parentCaseId);

    void removeInterfacePreCaseById(Integer id);

    void removeInterfacePreCaseByParentId(Integer parentCaseId);

    List<Integer> findInterfacePreIdByParentId(Integer parentCaseId);

    List<Integer> findInterfacePreCaseIdByParentId(Integer parentCaseId);

    List<Integer> recursionPreCase(List<Integer> returnResult, Integer parentCaseId);
}
