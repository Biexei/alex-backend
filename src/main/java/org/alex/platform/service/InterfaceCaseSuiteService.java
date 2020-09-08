package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.InterfaceCaseSuiteDO;
import org.alex.platform.pojo.InterfaceCaseSuiteDTO;
import org.alex.platform.pojo.InterfaceCaseSuiteVO;


public interface InterfaceCaseSuiteService {
    void saveInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    void modifyInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO);

    void removeInterfaceCaseSuiteById(Integer suiteId);

    InterfaceCaseSuiteVO findInterfaceCaseSuiteById(Integer suiteId);

    PageInfo<InterfaceCaseSuiteVO> findInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO, Integer pageNum, Integer pageSize);
}
