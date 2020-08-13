package org.alex.platform.service;

import org.alex.platform.pojo.InterfaceAssertLogDO;
import org.alex.platform.pojo.InterfaceAssertLogDTO;
import org.alex.platform.pojo.InterfaceAssertLogVO;

import java.util.List;

public interface InterfaceAssertLogService {
    void saveInterfaceAssertLog(InterfaceAssertLogDO interfaceAssertLogDO);
    List<InterfaceAssertLogVO> findInterfaceAssertLogList(InterfaceAssertLogDTO interfaceAssertLogDTO);
}
