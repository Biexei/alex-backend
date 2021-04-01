package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDTO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public interface InterfaceSuiteCaseRefService {
    void saveSuiteCase(List<InterfaceSuiteCaseRefDO> interfaceSuiteCaseRefDOList);

    void saveSuiteCaseSingle(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO);

    void modifySuiteCase(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO);

    void removeSuiteCase(Integer incrementKey);

    void removeSuiteCaseByObject(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO);

    PageInfo<InterfaceSuiteCaseRefVO> findSuiteCaseList(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO,
                                                        Integer pageNum, Integer pageSize);

    List<InterfaceSuiteCaseRefVO> findAllSuiteCase(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO);

    String executeSuiteCaseById(Integer suiteId, String executor) throws BusinessException;

    Byte executeCaseInSuite(Integer suiteId, Integer caseId, String executor) throws BusinessException;

}
