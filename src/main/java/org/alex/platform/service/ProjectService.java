package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.ProjectDO;


public interface ProjectService {
    PageInfo<ProjectDO> findProjectList(ProjectDO projectDO, Integer pageNum, Integer pageSize);
    ProjectDO findProject(ProjectDO projectDO);
    void modifyProject(ProjectDO projectDO) throws Exception;
    void saveProject(ProjectDO projectDO) throws Exception;
    void removeProjectById(Integer projectId) throws BusinessException;
}
