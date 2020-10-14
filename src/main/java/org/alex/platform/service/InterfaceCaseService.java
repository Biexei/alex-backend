package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.*;
import org.springframework.transaction.annotation.Transactional;

public interface InterfaceCaseService {
    @Transactional
    InterfaceCaseDO saveInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException;

    @Transactional
    void saveInterfaceCaseAndAssert(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException;

    @Transactional
    void modifyInterfaceCase(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException;

    void removeInterfaceCase(Integer interfaceCaseId) throws BusinessException;

    PageInfo<InterfaceCaseListVO> findInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO, Integer pageNum, Integer pageSize);

    InterfaceCaseInfoVO findInterfaceCaseByCaseId(Integer caseId);

    Integer executeInterfaceCase(Integer interfaceCaseId, String executor, String suiteLogNo, String chainNo, Integer suiteId) throws ParseException, BusinessException, SqlException;

    String parseRelyData(String s, String chainNo, Integer suiteId) throws BusinessException, ParseException, SqlException;
}
