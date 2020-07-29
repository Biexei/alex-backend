package org.alex.platform.mapper;

import org.alex.platform.pojo.Project;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMapper {
    List<Project> selectProjectList(Project project);
    Project selectProject(Project project);
    void updateProject(Project project);
    void insertProject(Project project);
    void deleteProjectById(Integer projectId);
}
