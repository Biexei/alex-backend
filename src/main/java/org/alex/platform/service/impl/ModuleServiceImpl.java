package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.controller.ModuleController;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.mapper.ProjectMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;

@Service
public class ModuleServiceImpl implements ModuleService {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleController.class);
    @Autowired
    ModuleMapper moduleMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;

    /**
     * 查看模块列表
     *
     * @param moduleDto moduleDto
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @return
     */
    @Override
    public PageInfo<Serializable> findModuleList(ModuleDTO moduleDto, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(moduleMapper.selectModuleList(moduleDto));
    }

    /**
     * 查看模块列表（不分页）
     * @param projectId 项目编号
     * @return
     */
    @Override
    public ArrayList<ModuleDO> findAllModuleList(Integer projectId) {
        return new ArrayList(moduleMapper.selectAllModuleList(projectId));
    }

    /**
     * 新增模块
     *
     * @param moduleDO moduleDO
     * @throws BusinessException BusinessException
     */
    @Override
    public void saveModule(ModuleDO moduleDO) throws BusinessException {
        ProjectDO project = new ProjectDO();
        project.setProjectId(moduleDO.getProjectId());
        //判断入参projectId是否存在
        if (projectMapper.selectProject(project) == null) {
            LOG.warn("项目编号不存在, projectId={}", moduleDO.getProjectId());
            throw new BusinessException("请选择项目名称");
        } else {
            moduleMapper.insertModule(moduleDO);
        }

    }

    /**
     * 修改模块
     *
     * @param moduleDO moduleDO
     */
    @Override
    public void modifyModule(ModuleDO moduleDO) {
        moduleMapper.updateModule(moduleDO);
    }

    /**
     * 删除模块
     *
     * @param moduleId 模块编号
     * @throws BusinessException BusinessException
     */
    @Override
    public void removeModuleById(Integer moduleId) throws BusinessException {
        // 模块下存在用例则不允许删除
        InterfaceCaseListDTO listDTO = new InterfaceCaseListDTO();
        listDTO.setModuleId(moduleId);
        if (interfaceCaseMapper.selectInterfaceCaseList(listDTO).isEmpty()) {
            moduleMapper.deleteModuleById(moduleId);
        } else {
            LOG.warn("删除模块失败，该模块下已存在用例, moduleId={}", moduleId);
            throw new BusinessException("该模块下已存在用例");
        }
    }
}
