package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.ProjectMapper;
import org.alex.platform.pojo.Project;
import org.alex.platform.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public PageInfo<Project> findProjectList(Project project, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Project> projectList =  projectMapper.selectProjectList(project);
        return new PageInfo(projectList);
    }

    @Override
    public Project findProject(Project project) {
        return projectMapper.selectProject(project);
    }

    @Override
    public void modifyProject(Project project) throws Exception {
        String projectName = project.getName();
        Project p = new Project();
        p.setName(projectName);
        if (findProject(p) != null){
            throw new BusinessException("修改失败，项目名称已存在");
        } else{
            projectMapper.updateProject(project);
        }
    }

    @Override
    public void saveProject(Project project) throws Exception {
        if (findProject(project) != null){
            throw new BusinessException("新增失败，项目名称已存在");
        } else{
            projectMapper.insertProject(project);
        }
    }

    @Override
    public void removeProjectById(Integer projectId) {
        projectMapper.deleteProjectById(projectId);
    }
}
