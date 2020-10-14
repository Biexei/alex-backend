package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.ProjectDO;
import org.alex.platform.pojo.ProjectVO;

import java.util.ArrayList;
import java.util.List;


public interface ProjectService {
    PageInfo<ProjectDO> findProjectList(ProjectDO projectDO, Integer pageNum, Integer pageSize);

    List<ProjectDO> findAllProject(ProjectDO projectDO);

    ProjectDO findProject(ProjectDO projectDO);

    ProjectVO findModulesById(Integer projectId);

    ProjectVO findProjectById(Integer projectId);

    void modifyProject(ProjectDO projectDO) throws Exception;

    void saveProject(ProjectDO projectDO) throws Exception;

    void removeProjectById(Integer projectId) throws BusinessException;
}
