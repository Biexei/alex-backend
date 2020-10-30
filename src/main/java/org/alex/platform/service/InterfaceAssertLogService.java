package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.InterfaceAssertLogDO;
import org.alex.platform.pojo.InterfaceAssertLogDTO;
import org.alex.platform.pojo.InterfaceAssertLogVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InterfaceAssertLogService {
    @Transactional(rollbackFor = Exception.class)
    void saveInterfaceAssertLog(InterfaceAssertLogDO interfaceAssertLogDO);

    PageInfo<InterfaceAssertLogVO> findInterfaceAssertLogList(InterfaceAssertLogDTO interfaceAssertLogDTO,
                                                              Integer pageNum, Integer pageSize);
}
