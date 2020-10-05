package org.alex.platform.mapper;

import org.alex.platform.pojo.TaskDO;
import org.alex.platform.pojo.TaskDTO;
import org.alex.platform.pojo.TaskVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {
    List<TaskVO> selectTaskList(TaskDTO taskDTO);

    TaskVO selectTaskById(Integer taskId);

    Integer insertTask(TaskDO taskDO);

    void updateTask(TaskDO taskDO);

    void updateTaskNextTime(TaskDO taskDO);

    void deleteTask(Integer taskId);
}
