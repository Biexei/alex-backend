package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceProcessorDO;
import org.alex.platform.pojo.InterfaceProcessorDTO;
import org.alex.platform.pojo.InterfaceProcessorVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InterfaceProcessorService {
    InterfaceProcessorVO findInterfaceProcessorByName(String name);

    InterfaceProcessorVO findInterfaceProcessorById(Integer processorId);

    List<Integer> findInterfaceProcessorIdByCaseId(Integer caseId);

    List<InterfaceProcessorVO> checkInterfaceProcessorName(Integer processorId, String name);

    List<InterfaceProcessorVO> findInterfaceProcessorList(InterfaceProcessorDTO interfaceProcessorDTO);

    @Transactional(rollbackFor = Exception.class)
    InterfaceProcessorDO saveInterfaceProcessor(InterfaceProcessorDO interfaceProcessorDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void modifyInterfaceProcessor(InterfaceProcessorDO interfaceProcessorDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void removeInterfaceProcessorById(Integer processorId);

    @Transactional(rollbackFor = Exception.class)
    void removeInterfaceProcessorByCaseId(Integer caseId);
}
