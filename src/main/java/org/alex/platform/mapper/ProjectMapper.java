package org.alex.platform.mapper;

import org.alex.platform.pojo.ProjectDO;
import org.alex.platform.pojo.ProjectVO;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface ProjectMapper {
    List<ProjectDO> selectProjectList(ProjectDO projectDO);

    List<ProjectDO> checkName(ProjectDO projectDO);

    ProjectDO selectProject(ProjectDO projectDO);

    ProjectVO selectModulesById(Integer projectId);

    ProjectVO selectProjectById(Integer projectId);

    void updateProject(ProjectDO projectDO);

    void insertProject(ProjectDO projectDO);

    void deleteProjectById(Integer projectId);
}
