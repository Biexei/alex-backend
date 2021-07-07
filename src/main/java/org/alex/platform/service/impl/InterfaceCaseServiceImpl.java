package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.core.http.ExecuteHandler;
import org.alex.platform.core.parser.Parser;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.*;
import org.alex.platform.pojo.*;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.alex.platform.service.*;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings({"unchecked","rawtypes"})
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
    InterfaceCaseRelyDataMapper interfaceCaseRelyDataMapper;
    @Autowired
    InterfaceSuiteCaseRefMapper interfaceSuiteCaseRefMapper;
    @Autowired
    InterfaceAssertMapper interfaceAssertMapper;
    @Autowired
    InterfaceProcessorService interfaceProcessorService;
    @Autowired
    InterfacePreCaseService interfacePreCaseService;
    @Autowired
    ExecuteHandler executeHandler;
    @Autowired
    ProjectService projectService;
    @Autowired
    ModuleService moduleService;
    @Autowired
    Parser parser;
    @Autowired
    RelyDataMapper relyDataMapper;

    private static final Logger LOG = LoggerFactory.getLogger(InterfaceCaseServiceImpl.class);

    /**
     * 新增接口测试用例
     *
     * @param interfaceCaseDO interfaceCaseDO
     * @return InterfaceCaseDO
     * @throws BusinessException BusinessException
     */
    @Override
    public InterfaceCaseDO saveInterfaceCase(InterfaceCaseDO interfaceCaseDO) throws BusinessException {
        Date date = new Date();
        interfaceCaseDO.setCreatedTime(date);
        interfaceCaseDO.setUpdateTime(date);
        Integer moduleId = interfaceCaseDO.getModuleId();
        Integer projectId = interfaceCaseDO.getProjectId();

        Byte bodyType = interfaceCaseDO.getBodyType();
        if (bodyType >= 3 && bodyType != 9) {
            throw new BusinessException("bodyType参数错误");
        }

        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setModuleId(moduleId);
        moduleDTO.setProjectId(projectId);
        //判断入参moduleId是否存在, projectId是否存在
        if (moduleMapper.selectModuleList(moduleDTO).isEmpty()) {
            LOG.warn("新增接口测试用例，模块编号/项目编号不存在");
            throw new BusinessException("模块编号/项目编号不存在");
        } else {
            interfaceCaseMapper.insertInterfaceCase(interfaceCaseDO);
            return interfaceCaseDO;
        }
    }

    /**
     * 新增接口测试用例及断言及处理器
     *
     * @param interfaceCaseDTO interfaceCaseDTO
     * @throws BusinessException BusinessException
     */
    @Override
    public void saveInterfaceCaseAndAssertAndPostProcessorAndPreCase(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException {
        //插入用例详情表，获取自增用例编号
        Integer caseId = this.saveInterfaceCase(interfaceCaseDTO).getCaseId();
        //插入前置用例表
        List<InterfacePreCaseDO> preCases = interfaceCaseDTO.getPreCases();
        if (!preCases.isEmpty()) {
            for (InterfacePreCaseDO interfacePreCaseDO : preCases) {
                Integer preCaseId = interfacePreCaseDO.getPreCaseId();
                ValidUtil.notNUll(preCaseId, "前置用例编号不能为空");
                ValidUtil.notNUll(this.findInterfaceCaseByCaseId(preCaseId), "前置用例编号不存在");
                if (interfacePreCaseDO.getPreCaseId().equals(caseId)) {
                    LOG.error("系统疑似受到攻击, 前置用例编号等于当前自增用例编号");
                    throw new BusinessException("参数非法");
                }
                interfacePreCaseDO.setParentCaseId(caseId);
                interfacePreCaseService.saveInterfacePreCase(interfacePreCaseDO);
            }
        }
        //插入断言表
        List<InterfaceAssertDO> assertList = interfaceCaseDTO.getAsserts();
        if (!assertList.isEmpty()) {
            for (InterfaceAssertDO assertDO : assertList) {
                assertDO.setCaseId(caseId);
                interfaceAssertService.saveAssert(assertDO);
            }
        }
        //插入处理器表
        List<InterfaceProcessorDO> postProcessorList = interfaceCaseDTO.getPostProcessors();
        if (!postProcessorList.isEmpty()) {
            for (InterfaceProcessorDO interfaceProcessorDO : postProcessorList) {
                interfaceProcessorDO.setCaseId(caseId);
                interfaceProcessorService.saveInterfaceProcessor(interfaceProcessorDO);
            }
        }
    }

    /**
     * 修改接口测试用例
     *
     * @param interfaceCaseDTO interfaceCaseDTO
     * @throws BusinessException BusinessException
     */
    @Override
    public void modifyInterfaceCase(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException {

        Integer caseId = interfaceCaseDTO.getCaseId();

        ValidUtil.notNUll(caseId, "用例编号错误");

        Byte bodyType = interfaceCaseDTO.getBodyType();
        if (bodyType >= 3 && bodyType != 9) {
            throw new BusinessException("bodyType参数错误");
        }

        Date updateTime = new Date();
        interfaceCaseDTO.setUpdateTime(updateTime);
        InterfaceCaseDO interfaceCaseDO = new InterfaceCaseDO();
        interfaceCaseDO.setModuleId(interfaceCaseDTO.getModuleId());
        interfaceCaseDO.setProjectId(interfaceCaseDTO.getProjectId());
        interfaceCaseDO.setCaseId(caseId);
        interfaceCaseDO.setUrl(interfaceCaseDTO.getUrl());
        interfaceCaseDO.setMethod(interfaceCaseDTO.getMethod());
        interfaceCaseDO.setDesc(interfaceCaseDTO.getDesc());
        interfaceCaseDO.setLevel(interfaceCaseDTO.getLevel());
        interfaceCaseDO.setDoc(interfaceCaseDTO.getDoc());
        interfaceCaseDO.setHeaders(interfaceCaseDTO.getHeaders());
        interfaceCaseDO.setParams(interfaceCaseDTO.getParams());
        interfaceCaseDO.setFormData(interfaceCaseDTO.getFormData());
        interfaceCaseDO.setFormDataEncoded(interfaceCaseDTO.getFormDataEncoded());
        interfaceCaseDO.setRaw(interfaceCaseDTO.getRaw());
        interfaceCaseDO.setRawType(interfaceCaseDTO.getRawType());
        interfaceCaseDO.setBodyType(interfaceCaseDTO.getBodyType());
        interfaceCaseDO.setCreater(interfaceCaseDTO.getCreater());
        interfaceCaseDO.setUpdateTime(interfaceCaseDTO.getUpdateTime());

        String headers = interfaceCaseDO.getHeaders();
        String params = interfaceCaseDO.getParams();
        String formData = interfaceCaseDO.getFormData();
        String formDataEncoded = interfaceCaseDO.getFormDataEncoded();
        String raw = interfaceCaseDO.getRaw();

        // 检查属性中的接口依赖是否包含自身
        this.checkDependencyInCaseProperty(caseId, headers, "headers", false);
        this.checkDependencyInCaseProperty(caseId, params, "params", false);
        this.checkDependencyInCaseProperty(caseId, formData, "formData", false);
        this.checkDependencyInCaseProperty(caseId, formDataEncoded, "formDataEncoded", false);
        this.checkDependencyInCaseProperty(caseId, raw, "raw", false);

        Integer moduleId = interfaceCaseDO.getModuleId();
        Integer projectId = interfaceCaseDO.getProjectId();
        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setModuleId(moduleId);
        moduleDTO.setProjectId(projectId);
        //判断入参moduleId是否存在, projectId是否存在
        if (moduleMapper.selectModuleList(moduleDTO).isEmpty()) {
            LOG.warn("修改接口测试用例，模块编号/项目编号不存在");
            throw new BusinessException("模块编号/项目编号不存在");
        }
        // 修改用例表
        interfaceCaseMapper.updateInterfaceCase(interfaceCaseDO);
        List<InterfaceAssertDO> asserts = interfaceCaseDTO.getAsserts();
        List<Integer> allAssertId = interfaceAssertMapper.selectAllAssertId(interfaceCaseDTO.getCaseId());
        if (asserts != null && !asserts.isEmpty()) {
            for (InterfaceAssertDO assertDO : asserts) {

                // 检查断言预期结果中接口依赖是否包含自身
                String exceptedResult = assertDO.getExceptedResult();
                this.checkDependencyInCaseProperty(caseId, exceptedResult, "断言预期结果", false);

                // 修改断言表  修改存在的
                assertDO.setCaseId(interfaceCaseDTO.getCaseId());
                interfaceAssertService.modifyAssert(assertDO);
                // 新增没有传assertId的
                if (assertDO.getAssertId() == null) {
                    interfaceAssertService.saveAssert(assertDO);
                } else {
                    // 有就移出此次新增前的id队列
                    for (int i = allAssertId.size() - 1; i >= 0; i--) {
                        if (allAssertId.get(i).equals(assertDO.getAssertId())) {
                            allAssertId.remove(i);
                        }
                    }
                }
            }
            for (Integer assertId : allAssertId) {
                interfaceAssertService.removeAssertByAssertId(assertId);
            }
        } else {
            // 移除该接口下所有断言
            interfaceAssertService.removeAssertByCaseId(caseId);
        }

        // 修改前置用例
        List<InterfacePreCaseDO> preCases = interfaceCaseDTO.getPreCases();
        List<Integer> preIdList = interfacePreCaseService.findInterfacePreIdByParentId(interfaceCaseDTO.getCaseId());
        if (preCases != null && !preCases.isEmpty()) {
            for (InterfacePreCaseDO interfacePreCaseDO : preCases) {
                Integer preCaseId = interfacePreCaseDO.getPreCaseId();
                ValidUtil.notNUll(preCaseId, "前置用例编号不能为空");
                InterfaceCaseInfoVO preCaseVO = this.findInterfaceCaseByCaseId(preCaseId);
                ValidUtil.notNUll(preCaseVO, "前置用例编号不存在");

                // 检查前置用例属性中的接口依赖是否包含自身
                String preHeaders = preCaseVO.getHeaders();
                String preParams = preCaseVO.getParams();
                String preFormData = preCaseVO.getFormData();
                String preFormDataEncoded = preCaseVO.getFormDataEncoded();
                String preRaw = preCaseVO.getRaw();
                List<InterfaceAssertVO> preCaseAsserts = preCaseVO.getAsserts();
                this.checkDependencyInCaseProperty(caseId, preHeaders, String.format("[%s]headers", preCaseId), true);
                this.checkDependencyInCaseProperty(caseId, preParams, String.format("[%s]params", preCaseId), true);
                this.checkDependencyInCaseProperty(caseId, preFormData, String.format("[%s]formData", preCaseId), true);
                this.checkDependencyInCaseProperty(caseId, preFormDataEncoded, String.format("[%s]formDataEncoded", preCaseId), true);
                this.checkDependencyInCaseProperty(caseId, preRaw, String.format("[%s]raw", preCaseId), true);
                for (InterfaceAssertVO interfaceAssert : preCaseAsserts) {
                    String exceptedResult = interfaceAssert.getExceptedResult();
                    this.checkDependencyInCaseProperty(caseId, exceptedResult, String.format("[%s]断言预期结果", preCaseId), true);
                }

                interfacePreCaseDO.setParentCaseId(interfaceCaseDTO.getCaseId());
                // 1.修改已存在的
                interfacePreCaseService.modifyInterfacePreCase(interfacePreCaseDO);
                // 2.新增没有自增Id的
                if (interfacePreCaseDO.getId() == null) {
                    interfacePreCaseService.saveInterfacePreCase(interfacePreCaseDO);
                } else {
                    // 3.有就移出此次新增前的id队列
                    for (int i = preIdList.size() - 1; i >= 0; i--) {
                        if (preIdList.get(i).equals(interfacePreCaseDO.getId())) {
                            preIdList.remove(i);
                        }
                    }
                }
            }
            for (Integer id : preIdList) {
                interfacePreCaseService.removeInterfacePreCaseById(id);
            }
        } else {
            // 移除该用例下所有的前置用例
            interfacePreCaseService.removeInterfacePreCaseByParentId(caseId);
        }

        // 修改处理器
        List<InterfaceProcessorDO> postProcessors = interfaceCaseDTO.getPostProcessors();
        List<Integer> postProcessorIdList = interfaceProcessorService.findInterfaceProcessorIdByCaseId(interfaceCaseDTO.getCaseId());
        if (postProcessors != null && !postProcessors.isEmpty()) {
            for(InterfaceProcessorDO interfaceProcessorDO : postProcessors) {
                interfaceProcessorDO.setCaseId(interfaceCaseDTO.getCaseId());
                // 1.修改已存在的
                interfaceProcessorService.modifyInterfaceProcessor(interfaceProcessorDO);
                // 2.新增没有postProcessorId的
                if (interfaceProcessorDO.getProcessorId() == null) {
                    interfaceProcessorService.saveInterfaceProcessor(interfaceProcessorDO);
                } else {
                    // 3.有就移出此次新增前的id队列
                    for (int i = postProcessorIdList.size() - 1; i >= 0; i--) {
                        if (postProcessorIdList.get(i).equals(interfaceProcessorDO.getProcessorId())) {
                            postProcessorIdList.remove(i);
                        }
                    }
                }
            }
            for (Integer postProcessorId : postProcessorIdList) {
                interfaceProcessorService.removeInterfaceProcessorById(postProcessorId);
            }
        } else {
            // 移除该用例下所有的处理器
            interfaceProcessorService.removeInterfaceProcessorByCaseId(caseId);
        }
    }

    /**
     * 删除接口测试用例
     *
     * @param interfaceCaseId interfaceCaseId
     * @throws BusinessException BusinessException
     */
    @Override
    public void removeInterfaceCase(Integer interfaceCaseId) throws BusinessException {
        boolean inIfRelyData = true;
        boolean inCaseRef = true;
        String errorMsg = "";

        // 检查是否存在于t_interface_case_rely_data
        InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
        interfaceCaseRelyDataDTO.setRelyCaseId(interfaceCaseId);

        // 检查是否存在于t_interface_suite_case_ref
        InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO = new InterfaceSuiteCaseRefDTO();
        interfaceSuiteCaseRefDTO.setCaseId(interfaceCaseId);

        if (interfaceCaseRelyDataMapper.selectIfRelyDataList(interfaceCaseRelyDataDTO).isEmpty()) {
            inIfRelyData = false;
        } else {
            LOG.warn("删除接口测试用例，该用例已存在与数据中心-接口依赖，interfaceCaseId={}", interfaceCaseId);
            errorMsg = errorMsg + "该用例已存在于接口依赖";
        }
        // 检查是否存在于t_interface_suite_case_ref
        if (interfaceSuiteCaseRefMapper.selectSuiteCaseList(interfaceSuiteCaseRefDTO).isEmpty()) {
            inCaseRef = false;
        } else {
            LOG.warn("删除接口测试用例，该用例已存在与接口测试-测试套件，interfaceCaseId={}", interfaceCaseId);
            errorMsg = errorMsg + "该用例已存在于测试套件";
        }
        if (!inIfRelyData && !inCaseRef) {
            interfaceCaseMapper.removeInterfaceCase(interfaceCaseId);
            // 删除与之相关的断言
            interfaceAssertService.removeAssertByCaseId(interfaceCaseId);
            // 删除与之相关的处理器
            interfaceProcessorService.removeInterfaceProcessorByCaseId(interfaceCaseId);
            // 删除与之相关的前置用例
            interfacePreCaseService.removeInterfacePreCaseByParentId(interfaceCaseId);
        } else {
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 获取接口测试用例列表
     *
     * @param interfaceCaseListDTO interfaceCaseListDTO
     * @param pageNum              pageNum
     * @param pageSize             pageSize
     * @return PageInfo<InterfaceCaseListVO>
     */
    @Override
    public PageInfo<InterfaceCaseListVO> findInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(interfaceCaseMapper.selectInterfaceCaseList(interfaceCaseListDTO));
    }

    /**
     * 获取接口测试用例列表(不分页)
     *
     * @param interfaceCaseListDTO interfaceCaseListDTO
     * @return PageInfo<InterfaceCaseListVO>
     */
    @Override
    public ArrayList<InterfaceCaseListVO> findAllInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO) {
        return new ArrayList(interfaceCaseMapper.selectInterfaceCaseList(interfaceCaseListDTO));
    }

    /**
     * 获取接口测试用例详情
     *
     * @param caseId 用例编号
     * @return InterfaceCaseInfoVO
     */
    @Override
    public InterfaceCaseInfoVO findInterfaceCaseByCaseId(Integer caseId) {
        return interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId);
    }

    /**
     * 执行接口测试用例
     * @param executeInterfaceCaseParam executeInterfaceCaseParam
     * @return 日志编号
     * @throws BusinessException 执行异常
     */
    @Override
    public Integer executeInterfaceCase(ExecuteInterfaceCaseParam executeInterfaceCaseParam) throws BusinessException {
        return executeHandler.executeInterfaceCase(executeInterfaceCaseParam);
    }

    /**
     * 懒加载获取树型case列表
     * @param level 树层级0代表project级别、1代表module级别、2代表case级别
     * @param id level为0时，不接受该参数，默认返回全部项目列表
     *           level为1时，id为项目编号，返回该项目下所有模块
     *           level为2时，id为模块编号，返回该模块下所有用例
     * @return tree
     */
    @Override
    public JSONArray caseTree(Integer level, Integer id) {
        JSONArray result = new JSONArray();
        if (level == 0) {
            List<ProjectDO> projectList = projectService.findAllProject(null);
            projectList.forEach(projectDO -> {
                JSONObject object = new JSONObject();
                object.put("label", projectDO.getName());
                object.put("scope", "project");
                object.put("number", projectDO.getProjectId());
                object.put("leaf", false);
                result.add(object);
            });
        } else if (level == 1) {
            ArrayList<ModuleDO> moduleList = moduleService.findAllModuleList(id);
            moduleList.forEach(moduleDO -> {
                JSONObject object = new JSONObject();
                object.put("label", moduleDO.getName());
                object.put("scope", "module");
                object.put("number", moduleDO.getModuleId());
                object.put("leaf", false);
                result.add(object);
            });
        } else if (level == 2) {
            InterfaceCaseListDTO dto = new InterfaceCaseListDTO();
            dto.setModuleId(id);
            ArrayList<InterfaceCaseListVO> caseList = this.findAllInterfaceCaseList(dto);
            caseList.forEach(cs -> {
                JSONObject object = new JSONObject();
                object.put("label", cs.getDesc());
                object.put("scope", "case");
                object.put("number", cs.getCaseId());
                object.put("projectName", cs.getProjectName());
                object.put("moduleName", cs.getModuleName());
                object.put("leaf", true);
                result.add(object);
            });
        }
        return result;
    }

    /**
     * 检查属性中的接口依赖是否包含自身
     * @param caseId 用例编号
     * @param property 域，如headers、params、formdata、formdataencoded、raw
     * @param propertyDesc 描述
     * @param isPreCase 是否为前置用例
     * @throws BusinessException BusinessException
     */
    private void checkDependencyInCaseProperty(Integer caseId, String property, String propertyDesc, boolean isPreCase)
            throws BusinessException {
        ArrayList<String> dependencyOfHeaders = parser.extractDependencyName(property);
        for (String s : dependencyOfHeaders) {

            // 检查接口依赖
            InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = interfaceCaseRelyDataMapper.selectIfRelyDataByName(s);
            if (null != interfaceCaseRelyDataVO) {
                int relyCaseId = interfaceCaseRelyDataVO.getRelyCaseId();
                if (caseId == relyCaseId) {
                    if (!isPreCase) {
                        throw new BusinessException(String.format("当前用例%s中的依赖不能引用自身", propertyDesc));
                    } else {
                        throw new BusinessException(String.format("当前用例的前置用例%s中的依赖不能引用自身", propertyDesc));
                    }
                }
            }

            // 检查SQL依赖中的接口依赖
            RelyDataVO vo = relyDataMapper.selectRelyDataByName(s);
            if (vo != null) {
                int type = vo.getType().intValue();
                if (type > 1) {
                    String sql = vo.getValue();
                    for (String var1 : parser.extractDependencyName(sql)) {
                        InterfaceCaseRelyDataVO interfaceDependencyInSQL = interfaceCaseRelyDataMapper.selectIfRelyDataByName(var1);
                        if (null != interfaceDependencyInSQL) {
                            int relyCaseId = interfaceDependencyInSQL.getRelyCaseId();
                            if (caseId == relyCaseId) {
                                if (!isPreCase) {
                                    throw new BusinessException(String.format("当前用例%s中的SQL依赖中的依赖不能引用自身", propertyDesc));
                                } else {
                                    throw new BusinessException(String.format("当前用例的前置用例%s中的SQL依赖中的依赖不能引用自身", propertyDesc));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
