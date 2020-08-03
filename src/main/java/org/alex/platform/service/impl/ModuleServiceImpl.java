package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.controller.ModuleController;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.mapper.ProjectMapper;
import org.alex.platform.pojo.ModuleDO;
import org.alex.platform.pojo.ModuleDTO;
import org.alex.platform.pojo.ProjectDO;
import org.alex.platform.service.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ModuleServiceImpl implements ModuleService {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleController.class);
    @Autowired
    ModuleMapper moduleMapper;
    @Autowired
    ProjectMapper projectMapper;

    @Override
    public PageInfo<Serializable> findModuleList(ModuleDTO moduleDto, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(moduleMapper.selectModuleList(moduleDto));
    }

    @Override
    public void saveModule(ModuleDO moduleDO) throws BusinessException {
        ProjectDO project = new ProjectDO();
        project.setProjectId(moduleDO.getProjectId());
        //判断入参projectId是否存在
        if (projectMapper.selectProject(project) == null) {
            LOG.error("项目id不存在");
            throw new BusinessException("项目id不存在");
        } else {
            moduleMapper.insertModule(moduleDO);
        }

    }

    @Override
    public void modifyModule(ModuleDO moduleDO) {
        moduleMapper.updateModule(moduleDO);
    }

    @Override
    public void removeModuleById(Integer moduleId) {
        moduleMapper.deleteModuleById(moduleId);
    }
}
