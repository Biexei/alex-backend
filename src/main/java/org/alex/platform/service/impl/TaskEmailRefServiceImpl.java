package org.alex.platform.service.impl;

import org.alex.platform.mapper.TaskEmailRefMapper;
import org.alex.platform.pojo.TaskEmailRefDO;
import org.alex.platform.pojo.TaskEmailRefDTO;
import org.alex.platform.pojo.TaskEmailRefVO;
import org.alex.platform.service.TaskEmailRefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TaskEmailRefServiceImpl implements TaskEmailRefService {
    @Autowired
    TaskEmailRefMapper taskEmailRefMapper;
    private static final Logger LOG = LoggerFactory.getLogger(TaskEmailRefServiceImpl.class);

    /**
     * 查看定时任务所有收件人
     * @param taskEmailRefDTO taskEmailRefDTO
     * @return ArrayList<TaskEmailRefVO>
     */
    @Override
    public ArrayList<TaskEmailRefVO> findTaskEmailRefList(TaskEmailRefDTO taskEmailRefDTO) {
        return taskEmailRefMapper.selectTaskEmailRefList(taskEmailRefDTO);
    }

    /**
     * 定时任务新增收件人
     * @param taskEmailRefDO taskEmailRefDO
     */
    @Override
    public void saveTaskEmailRef(TaskEmailRefDO taskEmailRefDO) {
        taskEmailRefMapper.insertTaskEmailRef(taskEmailRefDO);
    }

    /**
     * 删除定时任务的收件人
     * @param taskEmailRefDTO taskEmailRefDTO
     */
    @Override
    public void removeTaskEmailRef(TaskEmailRefDTO taskEmailRefDTO) {
        taskEmailRefMapper.deleteTaskEmailRef(taskEmailRefDTO);
    }
}
