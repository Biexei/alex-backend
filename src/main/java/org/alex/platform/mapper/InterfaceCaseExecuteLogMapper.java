package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceCaseExecuteLogDO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListDTO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListVO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceCaseExecuteLogMapper {
    Integer insertExecuteLog(InterfaceCaseExecuteLogDO executeLogDO);

    List<InterfaceCaseExecuteLogListVO> selectExecuteList(InterfaceCaseExecuteLogListDTO executeLogListDTO);

    InterfaceCaseExecuteLogVO selectExecute(Integer executeId);

    void updateExecuteLog(InterfaceCaseExecuteLogDO executeLogDO);
}
