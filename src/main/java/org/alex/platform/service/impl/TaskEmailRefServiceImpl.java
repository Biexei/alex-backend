package org.alex.platform.service.impl;

import org.alex.platform.mapper.TaskEmailRefMapper;
import org.alex.platform.pojo.TaskEmailRefDO;
import org.alex.platform.pojo.TaskEmailRefDTO;
import org.alex.platform.pojo.TaskEmailRefVO;
import org.alex.platform.service.TaskEmailRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TaskEmailRefServiceImpl implements TaskEmailRefService {
    @Autowired
    TaskEmailRefMapper taskEmailRefMapper;

    @Override
    public ArrayList<TaskEmailRefVO> findTaskEmailRefList(TaskEmailRefDTO taskEmailRefDTO) {
        return taskEmailRefMapper.selectTaskEmailRefList(taskEmailRefDTO);
    }

    @Override
    public void saveTaskEmailRef(TaskEmailRefDO taskEmailRefDO) {
        taskEmailRefMapper.insertTaskEmailRef(taskEmailRefDO);
    }

    @Override
    public void removeTaskEmailRef(TaskEmailRefDTO taskEmailRefDTO) {
        taskEmailRefMapper.deleteTaskEmailRef(taskEmailRefDTO);
    }
}
