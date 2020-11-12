package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.InterfaceProcessorLogDO;
import org.alex.platform.pojo.InterfaceProcessorLogDTO;
import org.alex.platform.pojo.InterfaceProcessorLogVO;

import java.util.List;

public interface InterfaceProcessorLogService {
    InterfaceProcessorLogVO findInterfaceProcessorLogById(Integer id);

    PageInfo<InterfaceProcessorLogVO> findInterfaceProcessorLogList(InterfaceProcessorLogDTO interfaceProcessorLogDTO, Integer pageNum, Integer pageSize);

    List<InterfaceProcessorLogVO> findInterfaceProcessorLogListAll(InterfaceProcessorLogDTO interfaceProcessorLogDTO);

    InterfaceProcessorLogDO saveInterfaceProcessorLog(InterfaceProcessorLogDO interfaceProcessorLogDO);
}
