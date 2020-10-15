package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceCaseSuiteDO;
import org.alex.platform.pojo.InterfaceCaseSuiteDTO;
import org.alex.platform.pojo.InterfaceCaseSuiteVO;

import java.util.HashMap;


public interface InterfaceCaseSuiteService {
    InterfaceCaseSuiteDO saveInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    void modifyInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    void removeInterfaceCaseSuiteById(Integer suiteId) throws BusinessException;

    InterfaceCaseSuiteVO findInterfaceCaseSuiteById(Integer suiteId);

    PageInfo<InterfaceCaseSuiteVO> findInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO, Integer pageNum, Integer pageSize);

    HashMap<String, Integer> copyInterfaceCaseSuiteById(Integer suiteId, String creator);
}
