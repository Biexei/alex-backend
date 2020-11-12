package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceProcessorDO;
import org.alex.platform.pojo.InterfaceProcessorDTO;
import org.alex.platform.pojo.InterfaceProcessorVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceProcessorMapper {
    InterfaceProcessorVO selectInterfaceProcessorByName(String name);

    List<Integer> selectInterfaceProcessorIdByCaseId(Integer caseId);

    InterfaceProcessorVO selectInterfaceProcessorById(Integer postProcessorId);

    List<InterfaceProcessorVO> checkInterfaceProcessorName(@Param("processorId") Integer processorId, @Param("name") String name);

    List<InterfaceProcessorVO> selectInterfaceProcessorList(InterfaceProcessorDTO interfaceProcessorDTO);

    Integer insertInterfaceProcessor(InterfaceProcessorDO interfaceProcessorDO);

    void updateInterfaceProcessor(InterfaceProcessorDO interfaceProcessorDO);

    void deleteInterfaceProcessorById(Integer processorId);

    void deleteInterfaceProcessorByCaseId(Integer caseId);

}
