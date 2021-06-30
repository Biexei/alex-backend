package org.alex.platform.mapper;

import org.alex.platform.pojo.ModuleDO;
import org.alex.platform.pojo.ModuleDTO;
import org.alex.platform.pojo.ModuleListVO;
import org.alex.platform.pojo.ModuleVO;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface ModuleMapper {
    List<Serializable> selectModuleList(ModuleDTO moduleMto);

    ArrayList<ModuleDO> selectAllModuleList(Integer projectId);

    void insertModule(ModuleDO moduleDO);

    void updateModule(ModuleDO moduleDO);

    void deleteModuleById(Integer moduleId);
}
