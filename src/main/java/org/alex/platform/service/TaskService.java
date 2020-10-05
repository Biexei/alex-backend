package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.TaskDO;
import org.alex.platform.pojo.TaskDTO;
import org.alex.platform.pojo.TaskRefDO;
import org.alex.platform.pojo.TaskVO;
import org.springframework.transaction.annotation.Transactional;

public interface TaskService {
    PageInfo<TaskVO> findTaskList(TaskDTO taskDTO, Integer pageNum, Integer pageSize);

    TaskVO findTaskById(Integer taskId);

    TaskDO saveTask(TaskDO taskDO);

    @Transactional
    void saveTaskAndRef(TaskRefDO taskRefDO);

    @Transactional
    void modifyTaskAndRef(TaskRefDO taskRefDO);

    @Transactional
    void removeTask(Integer taskId);
}
