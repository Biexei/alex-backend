package org.alex.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.InterfaceCaseExecuteLogDO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListDTO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListVO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface InterfaceCaseExecuteLogService {
    @Transactional(rollbackFor = Exception.class)
    InterfaceCaseExecuteLogDO saveExecuteLog(InterfaceCaseExecuteLogDO executeLogDO);

    PageInfo<InterfaceCaseExecuteLogListVO> findExecuteList(InterfaceCaseExecuteLogListDTO executeLogListDTO,
                                                            Integer pageNum, Integer pageSize);

    InterfaceCaseExecuteLogVO findExecute(Integer executeId);

    void modifyExecuteLog(InterfaceCaseExecuteLogDO executeLogDO);

    JSONArray caseExecuteLogChain(Integer executeId);

    List<InterfaceCaseExecuteLogListVO> findExecuteListAll(InterfaceCaseExecuteLogListDTO executeLogListDTO);
}
