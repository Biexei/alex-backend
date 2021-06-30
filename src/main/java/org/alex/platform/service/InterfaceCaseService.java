package org.alex.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.*;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

public interface InterfaceCaseService {
    InterfaceCaseDO saveInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void saveInterfaceCaseAndAssertAndPostProcessorAndPreCase(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void modifyInterfaceCase(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void removeInterfaceCase(Integer interfaceCaseId) throws BusinessException;

    PageInfo<InterfaceCaseListVO> findInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO, Integer pageNum, Integer pageSize);

    ArrayList<InterfaceCaseListVO> findAllInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO);

    InterfaceCaseInfoVO findInterfaceCaseByCaseId(Integer caseId);

    Integer executeInterfaceCase(ExecuteInterfaceCaseParam executeInterfaceCaseParam) throws BusinessException;

    JSONArray caseTree(Integer level, Integer id);

}
