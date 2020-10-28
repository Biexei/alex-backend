package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceAssertDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InterfaceAssertService {
    @Transactional
    void saveAssert(InterfaceAssertDO interfaceAssertDO) throws BusinessException;

    void modifyAssert(InterfaceAssertDO interfaceAssertDO) throws BusinessException;

    void removeAssertByCaseId(Integer caseId);

    void removeAssertByAssertId(Integer assertId);
}
