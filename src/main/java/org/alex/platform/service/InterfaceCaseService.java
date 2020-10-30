package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.*;
import org.springframework.transaction.annotation.Transactional;

public interface InterfaceCaseService {
    @Transactional(rollbackFor = Exception.class)
    InterfaceCaseDO saveInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void saveInterfaceCaseAndAssertAndPostProcessor(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void modifyInterfaceCase(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void removeInterfaceCase(Integer interfaceCaseId) throws BusinessException;

    PageInfo<InterfaceCaseListVO> findInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO, Integer pageNum, Integer pageSize);

    InterfaceCaseInfoVO findInterfaceCaseByCaseId(Integer caseId);

    Integer executeInterfaceCase(Integer interfaceCaseId, String executor, String suiteLogNo, String chainNo, Integer suiteId, Byte isFailedRetry, String suiteLogDetailNo) throws ParseException, BusinessException, SqlException;

    String parseRelyData(String s, String chainNo, Integer suiteId, Byte isFailedRetry, String suiteLogDetailNo) throws BusinessException, ParseException, SqlException;
}
