package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceAssertDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InterfaceAssertService {
//    @Transactional(rollbackFor = Exception.class)
    void saveAssert(InterfaceAssertDO interfaceAssertDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void modifyAssert(InterfaceAssertDO interfaceAssertDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void removeAssertByCaseId(Integer caseId);

    @Transactional(rollbackFor = Exception.class)
    void removeAssertByAssertId(Integer assertId);
}
