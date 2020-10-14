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

import java.util.List;


@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ModuleMapper moduleMapper;

    /**
     * 查询项目列表
     * @param projectDO projectDO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo<ProjectDO>
     */
    @Override
    public PageInfo<ProjectDO> findProjectList(ProjectDO projectDO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProjectDO> projectDOList = projectMapper.selectProjectList(projectDO);
        return new PageInfo(projectDOList);
    }

    /**
     * 查询所有项目（不分页）
     * @param projectDO projectDO
     * @return List<ProjectDO>
     */
    @Override
    public List<ProjectDO> findAllProject(ProjectDO projectDO) {
        return projectMapper.selectProjectList(projectDO);
    }

    /**
     * 查看项目详情
     * @param projectDO projectDO
     * @return ProjectDO
     */
    @Override
    public ProjectDO findProject(ProjectDO projectDO) {
        return projectMapper.selectProject(projectDO);
    }

    /**
     * 查看项目下所有模块
     * @param projectId 项目编号
     * @return ProjectVO
     */
    @Override
    public ProjectVO findModulesById(Integer projectId) {
        return projectMapper.selectModulesById(projectId);
    }

    @Override
    public ProjectVO findProjectById(Integer projectId) {
        return projectMapper.selectProjectById(projectId);
    }

    /**
     * 修改项目信息
     * @param projectDO projectDO
     */
    @Override
    public void modifyProject(ProjectDO projectDO) throws BusinessException {
        if (!projectMapper.checkName(projectDO).isEmpty()) {
            LOG.warn("项目名称已存在, projectName={}", projectDO.getName());
            throw new BusinessException("项目名称已存在");
        }
        projectMapper.updateProject(projectDO);
    }

    @Override
    public void saveProject(ProjectDO projectDO) throws Exception {
        ProjectDO project = new ProjectDO();
        project.setName(projectDO.getName());
        if (findProject(project) != null) {
            LOG.warn("项目名称已存在, projectName={}", projectDO.getName());
            throw new BusinessException("项目名称已存在");
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
            LOG.warn("该项目下已存在模块, projectId={}", projectId);
            throw new BusinessException("该项目下已存在模块");
        }

    }
}
