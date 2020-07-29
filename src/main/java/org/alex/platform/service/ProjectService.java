package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.Project;


public interface ProjectService {
    PageInfo<Project> findProjectList(Project project, Integer pageNum, Integer pageSize);
    Project findProject(Project project);
    void modifyProject(Project project) throws Exception;
    void saveProject(Project project) throws Exception;
    void removeProjectById(Integer projectId);
}
