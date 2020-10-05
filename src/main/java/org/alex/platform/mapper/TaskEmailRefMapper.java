package org.alex.platform.mapper;

import org.alex.platform.pojo.TaskEmailRefDO;
import org.alex.platform.pojo.TaskEmailRefDTO;
import org.alex.platform.pojo.TaskEmailRefVO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TaskEmailRefMapper {
    ArrayList<TaskEmailRefVO> selectTaskEmailRefList(TaskEmailRefDTO taskEmailRefDTO);

    void insertTaskEmailRef(TaskEmailRefDO taskEmailRefDO);

    void deleteTaskEmailRef(TaskEmailRefDTO taskEmailRefDTO);
}
