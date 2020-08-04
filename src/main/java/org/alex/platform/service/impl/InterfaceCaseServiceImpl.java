package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.InterfaceAssertMapper;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceAssertService;
import org.alex.platform.service.InterfaceCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceCaseServiceImpl implements InterfaceCaseService {
    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;
    @Autowired
    ModuleMapper moduleMapper;
    @Autowired
    InterfaceAssertService interfaceAssertService;
    @Autowired
    InterfaceCaseService interfaceCaseService;

    @Override
    public InterfaceCaseDO saveInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException {
        Integer moduleId = interfaceCaseDO.getModuleId();
        Integer projectId = interfaceCaseDO.getProjectId();
        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setModuleId(moduleId);
        moduleDTO.setProjectId(projectId);
        //判断入参moduleId是否存在, projectId是否存在
        if (moduleMapper.selectModuleList(moduleDTO).isEmpty()) {
            throw new BusinessException("模块编号/项目编号不存在");
        } else {
            interfaceCaseMapper.insertInterfaceCase(interfaceCaseDO);
            return interfaceCaseDO;
        }
    }

    @Override
    public void saveInterfaceCaseAndAssert(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException {
        //插入用例详情表，获取自增用例编号
        Integer caseId = this.saveInterfaceCase(interfaceCaseDTO).getCaseId();
        //插入断言表
        List<InterfaceAssertDO> assertList = interfaceCaseDTO.getAsserts();
        if (!assertList.isEmpty()) {
            for (InterfaceAssertDO assertDO : assertList) {
                assertDO.setCaseId(caseId);
                interfaceAssertService.saveAssert(assertDO);
            }
        }
    }

    @Override
    public void modifyInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException {
        Integer moduleId = interfaceCaseDO.getModuleId();
        Integer projectId = interfaceCaseDO.getProjectId();
        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setModuleId(moduleId);
        moduleDTO.setProjectId(projectId);
        //判断入参moduleId是否存在, projectId是否存在
        if (moduleMapper.selectModuleList(moduleDTO).isEmpty()) {
            throw new BusinessException("模块编号/项目编号不存在");
        }
        interfaceCaseMapper.updateInterfaceCase(interfaceCaseDO);
    }

    @Override
    public void removeInterfaceCase(Integer interfaceCaseId) {
        interfaceCaseMapper.removeInterfaceCase(interfaceCaseId);
    }

    @Override
    public PageInfo<InterfaceCaseListVO> findInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(interfaceCaseMapper.selectInterfaceCaseList(interfaceCaseListDTO));
    }

    @Override
    public InterfaceCaseInfoVO findInterfaceCaseByCaseId(Integer caseId) {
        return interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId);
    }
}
