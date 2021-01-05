package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.*;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

public interface InterfaceCaseService {
//    @Transactional(rollbackFor = Exception.class)
    InterfaceCaseDO saveInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void saveInterfaceCaseAndAssertAndPostProcessorAndPreCase(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void modifyInterfaceCase(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void removeInterfaceCase(Integer interfaceCaseId) throws BusinessException;

    PageInfo<InterfaceCaseListVO> findInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO, Integer pageNum, Integer pageSize);

    InterfaceCaseInfoVO findInterfaceCaseByCaseId(Integer caseId);

    Integer executeInterfaceCase(ExecuteInterfaceCaseParam executeInterfaceCaseParam) throws BusinessException;

    String parseRelyData(String s, String chainNo, Integer suiteId, Byte isFailedRetry, String suiteLogDetailNo,
                         HashMap globalHeaders, HashMap globalParams, HashMap globalData, String casePreNo)
            throws BusinessException, ParseException, SqlException;

    String parsePostProcessor(String s, String suiteLogDetailNo, String casePreNo) throws ParseException;
}
