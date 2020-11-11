package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.InterfaceSuiteProcessorDO;
import org.alex.platform.pojo.InterfaceSuiteProcessorDTO;
import org.alex.platform.pojo.InterfaceSuiteProcessorVO;

import java.util.List;

public interface InterfaceSuiteProcessorService {
    InterfaceSuiteProcessorDO saveInterfaceSuiteProcessor(InterfaceSuiteProcessorDO interfaceSuiteProcessorDO) throws ValidException;

    void modifyInterfaceSuiteProcessor(InterfaceSuiteProcessorDO interfaceSuiteProcessorDO) throws ValidException;

    List<InterfaceSuiteProcessorVO> findAllInterfaceSuiteProcessorList(InterfaceSuiteProcessorDTO interfaceSuiteProcessorDTO);

    PageInfo<InterfaceSuiteProcessorVO> findInterfaceSuiteProcessorList(InterfaceSuiteProcessorDTO interfaceSuiteProcessorDTO, Integer pageNum, Integer pageSize);

    InterfaceSuiteProcessorVO findInterfaceSuiteProcessorById(Integer id);

    List<InterfaceSuiteProcessorVO> findInterfaceSuiteProcessorBySuiteId(Integer suiteId);

    void removeInterfaceSuiteProcessorById(Integer id);

    void removeInterfaceSuiteProcessorBySuiteId(Integer suiteId);
}
