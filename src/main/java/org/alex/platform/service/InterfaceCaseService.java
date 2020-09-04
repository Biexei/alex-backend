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

    void modifyInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException;

    void removeInterfaceCase(Integer interfaceCaseId);

    PageInfo<InterfaceCaseListVO> findInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO, Integer pageNum, Integer pageSize);

    InterfaceCaseInfoVO findInterfaceCaseByCaseId(Integer caseId);

    Integer executeInterfaceCase(Integer interfaceCaseId) throws ParseException, BusinessException, SqlException;

    String parseRelyData(String s) throws BusinessException, ParseException, SqlException;
}
