package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


public interface InterfaceCaseSuiteService {
    InterfaceCaseSuiteDO saveInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    @Transactional(rollbackFor = Exception.class)
    void saveInterfaceCaseSuiteAndProcessor(InterfaceSuiteInfoDTO interfaceSuiteInfoDTO) throws ValidException;

    @Transactional(rollbackFor = Exception.class)
    void modifyInterfaceCaseSuite(InterfaceSuiteInfoDTO interfaceSuiteInfoDTO) throws ValidException;

    @Transactional(rollbackFor = Exception.class)
    void removeInterfaceCaseSuiteById(Integer suiteId) throws BusinessException;

    InterfaceCaseSuiteVO findInterfaceCaseSuiteById(Integer suiteId);

    InterfaceSuiteInfoVO findInterfaceCaseSuiteInfoById(Integer suiteId);

    PageInfo<InterfaceCaseSuiteVO> findInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO, Integer pageNum, Integer pageSize);

    List<InterfaceCaseSuiteVO> findInterfaceCaseSuiteAll(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO);

    @Transactional(rollbackFor = Exception.class)
    HashMap<String, Integer> copyInterfaceCaseSuiteById(Integer suiteId, String creator);
}
