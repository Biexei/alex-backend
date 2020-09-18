package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.mapper.ProjectMapper;
import org.alex.platform.pojo.ModuleDTO;
import org.alex.platform.pojo.ProjectDO;
import org.alex.platform.pojo.ProjectVO;
import org.alex.platform.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ModuleMapper moduleMapper;

    @Override
    public PageInfo<ProjectDO> findProjectList(ProjectDO projectDO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProjectDO> projectDOList = projectMapper.selectProjectList(projectDO);
        return new PageInfo(projectDOList);
    }

    @Override
    public List<ProjectDO> findAllProject(ProjectDO projectDO) {
        return projectMapper.selectProjectList(projectDO);
    }

    @Override
    public ProjectDO findProject(ProjectDO projectDO) {
        return projectMapper.selectProject(projectDO);
    }

    @Override
    public ProjectVO findModulesById(Integer projectId) {
        return projectMapper.selectModulesById(projectId);
    }

    @Override
    public void modifyProject(ProjectDO projectDO) throws Exception {
        projectMapper.updateProject(projectDO);
    }

    @Override
    public void saveProject(ProjectDO projectDO) throws Exception {
        ProjectDO project = new ProjectDO();
        project.setName(projectDO.getName());
        if (findProject(project) != null) {
            throw new BusinessException("新增失败，项目名称已存在");
        } else {
            projectMapper.insertProject(projectDO);
        }
    }

    @Override
    public void removeProjectById(Integer projectId) throws BusinessException {
        //若项目下存在模块则不允许删除
        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setProjectId(projectId);
        if (moduleMapper.selectModuleList(moduleDTO).isEmpty()) {
            projectMapper.deleteProjectById(projectId);
        } else {
            LOG.error("该项目下已存在模块");
            throw new BusinessException("该项目下已存在模块");
        }

    }
}
