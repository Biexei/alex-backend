package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.InterfaceCaseExecuteLogMapper;
import org.alex.platform.pojo.InterfaceCaseExecuteLogDO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListDTO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListVO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogVO;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterfaceCaseExecuteLogServiceImpl implements InterfaceCaseExecuteLogService {
    @Autowired
    InterfaceCaseExecuteLogMapper mapper;

    @Override
    public void saveExecuteLog(InterfaceCaseExecuteLogDO executeLogDO) {
        mapper.insertExecuteLog(executeLogDO);
    }

    @Override
    public PageInfo<InterfaceCaseExecuteLogListVO> findExecuteList(InterfaceCaseExecuteLogListDTO executeLogListDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(mapper.selectExecuteList(executeLogListDTO));
    }


    @Override
    public InterfaceCaseExecuteLogVO findExecute(Integer executeId) {
        return mapper.selectExecute(executeId);
    }
}
