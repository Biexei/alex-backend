package org.alex.platform.mapper;

import org.alex.platform.pojo.ModuleDO;
import org.alex.platform.pojo.ModuleDTO;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface ModuleMapper {
    List<Serializable> selectModuleList(ModuleDTO moduleMto);

    void insertModule(ModuleDO moduleDO);

    void updateModule(ModuleDO moduleDO);

    void deleteModuleById(Integer moduleId);
}
