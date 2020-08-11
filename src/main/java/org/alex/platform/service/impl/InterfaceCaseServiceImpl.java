package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.InterfaceAssertMapper;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceAssertService;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Autowired
    InterfaceCaseExecuteLogService logService;
    @Autowired
    ProjectService projectService;

    @Override
    public InterfaceCaseDO saveInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException {
        Integer moduleId = interfaceCaseDO.getModuleId();
        Integer projectId = interfaceCaseDO.getProjectId();


        String data = interfaceCaseDO.getData();
        String json = interfaceCaseDO.getJson();
        //data json 只能任传其一
        if (data != null && json != null) {
            throw new BusinessException("data/json只能任传其一");
        }

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

    @Override
    public void executeInterfaceCase(Integer interfaceCaseId) {
        // 1.获取case详情
        InterfaceCaseInfoVO interfaceCaseInfoVO = this.findInterfaceCaseByCaseId(interfaceCaseId);
        Integer projectId = interfaceCaseInfoVO.getProjectId();
        Integer moduleId = interfaceCaseInfoVO.getModuleId();
        String url = projectService.findModulesById(projectId).getDomain() + interfaceCaseInfoVO.getUrl();
        String desc = interfaceCaseInfoVO.getDesc();
        Byte level = interfaceCaseInfoVO.getLevel();
        String doc = interfaceCaseInfoVO.getDoc();
        String headers = interfaceCaseInfoVO.getHeaders();
        String params = interfaceCaseInfoVO.getParams();
        String data = interfaceCaseInfoVO.getData();
        String json = interfaceCaseInfoVO.getJson();
        String creater = interfaceCaseInfoVO.getCreater();
        Date createdTime = interfaceCaseInfoVO.getCreatedTime();
        List<InterfaceAssertVO> asserts = interfaceCaseInfoVO.getAsserts();
        // 2.执行case
        // 3.保存日志，先将status设置成1失败，待该日志关联的断言日志都成功后再修改状态为0成功
        InterfaceCaseExecuteLogDO logDo = new InterfaceCaseExecuteLogDO();
        logDo.setCaseId(interfaceCaseId);
        logDo.setCaseDesc(desc);
        logDo.setRequestHeaders(headers);
        logDo.setRequestParams(params);
        logDo.setRequestData(data);
        logDo.setRequestJson(json);
        logDo.setResponseCode();
        logDo.setResponseHeaders();
        logDo.setResponseBody();
        // 后续加入拦截器后根据token反查
        logDo.setExecuter();
        logDo.setAssertExtractExpression();
        logDo.setAssertOperator();
        logDo.setAssertExceptedResult();
        logDo.setStatus();
        logDo.setCreatedTime(new Date());
        logDo.setErrorMessage();
        logService.saveExecuteLog(logDo);
        // 4.保存断言日志表，获取运行日志自增id然后在断言日志表中写入断言信息，断言日志都成功后再将日志修改状态为0成功
    }
}
