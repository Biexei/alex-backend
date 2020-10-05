package org.alex.platform.service;

import org.alex.platform.pojo.TaskEmailRefDO;
import org.alex.platform.pojo.TaskEmailRefDTO;
import org.alex.platform.pojo.TaskEmailRefVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

public interface TaskEmailRefService {
    ArrayList<TaskEmailRefVO> findTaskEmailRefList(TaskEmailRefDTO taskEmailRefDTO);

    @Transactional
    void saveTaskEmailRef(TaskEmailRefDO taskEmailRefDO);

    @Transactional
    void removeTaskEmailRef(TaskEmailRefDTO taskEmailRefDTO);
}
