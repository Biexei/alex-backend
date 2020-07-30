package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.ModuleDO;
import org.alex.platform.pojo.ModuleDTO;

import java.io.Serializable;

public interface ModuleService {
    PageInfo<Serializable> findModuleList(ModuleDTO moduleDto, Integer pageNum, Integer pageSize);
    void saveModule(ModuleDO moduleDO) throws BusinessException;
    void modifyModule(ModuleDO moduleDO);
    void removeModuleById(Integer moduleId);
}
