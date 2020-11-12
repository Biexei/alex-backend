package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceProcessorLogDO;
import org.alex.platform.pojo.InterfaceProcessorLogDTO;
import org.alex.platform.pojo.InterfaceProcessorLogVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceProcessorLogMapper {
    InterfaceProcessorLogVO selectInterfaceProcessorLogById(Integer id);

    List<InterfaceProcessorLogVO> selectInterfaceProcessorLogList(InterfaceProcessorLogDTO interfaceProcessorLogDTO);

    Integer insertInterfaceProcessorLog(InterfaceProcessorLogDO interfaceProcessorLogDO);

    void updateInterfaceProcessorLog(InterfaceProcessorLogDO interfaceProcessorLogDO);
}
