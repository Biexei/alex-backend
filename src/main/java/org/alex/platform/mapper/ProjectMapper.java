package org.alex.platform.mapper;

import org.alex.platform.pojo.ProjectDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMapper {
    List<ProjectDO> selectProjectList(ProjectDO projectDO);
    ProjectDO selectProject(ProjectDO projectDO);
    void updateProject(ProjectDO projectDO);
    void insertProject(ProjectDO projectDO);
    void deleteProjectById(Integer projectId);
}
