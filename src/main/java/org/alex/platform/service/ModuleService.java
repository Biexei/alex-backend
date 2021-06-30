package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.ModuleDO;
import org.alex.platform.pojo.ModuleDTO;
import org.alex.platform.pojo.ModuleListVO;

import java.io.Serializable;
import java.util.ArrayList;

public interface ModuleService {
    PageInfo<Serializable> findModuleList(ModuleDTO moduleDto, Integer pageNum, Integer pageSize);

    ArrayList<ModuleDO> findAllModuleList(Integer projectId);

    void saveModule(ModuleDO moduleDO) throws BusinessException;

    void modifyModule(ModuleDO moduleDO);

    void removeModuleById(Integer moduleId) throws BusinessException;
}
