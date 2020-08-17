package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceAssertLogDO;
import org.alex.platform.pojo.InterfaceAssertLogDTO;
import org.alex.platform.pojo.InterfaceAssertLogVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceAssertLogMapper {
    void insertInterfaceAssertLog(InterfaceAssertLogDO interfaceAssertLogDO);

    List<InterfaceAssertLogVO> selectInterfaceAssertLogList(InterfaceAssertLogDTO interfaceAssertLogDTO);
}
