package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.*;
import org.alex.platform.pojo.*;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.alex.platform.service.*;
import org.alex.platform.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    InterfaceCaseExecuteLogService executeLogService;
    @Autowired
    ProjectService projectService;
    @Autowired
    InterfaceAssertLogService assertLogService;
    @Autowired
    InterfaceCaseRelyDataService ifCaseRelyDataService;
    @Autowired
    RelyDataService relyDataService;
    @Autowired
    DbService dbService;
    @Autowired
    InterfaceCaseRelyDataMapper interfaceCaseRelyDataMapper;
    @Autowired
    InterfaceSuiteCaseRefMapper interfaceSuiteCaseRefMapper;
    @Autowired
    InterfaceAssertMapper interfaceAssertMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    InterfaceCaseSuiteService ifSuiteService;
    @Autowired
    InterfaceProcessorService interfaceProcessorService;
    @Autowired
    InterfaceProcessorLogService interfaceProcessorLogService;

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

        String headers = interfaceCaseDO.getHeaders();
        String params = interfaceCaseDO.getParams();
        String data = interfaceCaseDO.getData();
        String json = interfaceCaseDO.getJson();
        ValidUtil.isJsonObject(headers, "请检查header格式");
        ValidUtil.isJsonObject(params, "请检查params格式");
        ValidUtil.isJsonObject(data, "请检查data格式");
        ValidUtil.isJson(json, "请检查json格式");

        //data json 只能任传其一
        if (data != null && json != null) {
            LOG.warn("新增接口测试用例，data/json只能任传其一");
            throw new BusinessException("data/json只能任传其一");
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
     * 新增接口测试用例及断言及后置处理器
     *
     * @param interfaceCaseDTO interfaceCaseDTO
     * @throws BusinessException BusinessException
     */
    @Override
    public void saveInterfaceCaseAndAssertAndPostProcessor(InterfaceCaseDTO interfaceCaseDTO) throws BusinessException {
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
        //插入后置处理器表
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

        if (caseId == null) {
            LOG.error("参数错误，缺少caseId");
            throw new BusinessException("参数错误，缺少caseId");
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
        interfaceCaseDO.setData(interfaceCaseDTO.getData());
        interfaceCaseDO.setJson(interfaceCaseDTO.getJson());
        interfaceCaseDO.setCreater(interfaceCaseDTO.getCreater());
        interfaceCaseDO.setUpdateTime(interfaceCaseDTO.getUpdateTime());

        String headers = interfaceCaseDO.getHeaders();
        String params = interfaceCaseDO.getParams();
        String data = interfaceCaseDO.getData();
        String json = interfaceCaseDO.getJson();
        ValidUtil.isJsonObject(headers, "请检查header格式");
        ValidUtil.isJsonObject(params, "请检查params格式");
        ValidUtil.isJsonObject(data, "请检查data格式");
        ValidUtil.isJson(json, "请检查json格式");

        // 编辑的时候如果注入依赖为接口依赖，并且依赖接口为当前接口，应该禁止，避免造成死循环
        String checkStr = headers + " " + params + " " + data + " " + json + " ";
        Pattern p = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = p.matcher(checkStr);
        while (matcher.find()) {
            String findStr = matcher.group();
            // 获取relyName
            String relyName = findStr.substring(2, findStr.length() - 1);
            InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = interfaceCaseRelyDataMapper.selectIfRelyDataByName(relyName);
            if (null != interfaceCaseRelyDataVO) {
                int relyCaseId = interfaceCaseRelyDataVO.getRelyCaseId();
                if (caseId == relyCaseId) {
                    LOG.warn("修改接口测试用例，headers/params/data/json，接口依赖的用例不能为当前用例");
                    throw new BusinessException("接口依赖的用例不能为当前用例");
                }
            }
        }

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
        if (asserts != null) {
            for (InterfaceAssertDO assertDO : asserts) {
                // 编辑的时候如果注入依赖为接口依赖，并且依赖接口为当前接口，应该禁止，避免造成死循环
                Pattern pp = Pattern.compile("\\$\\{.+?\\}");
                Matcher mm = pp.matcher(assertDO.getExceptedResult());
                while (mm.find()) {
                    String findStr = mm.group();
                    // 获取relyName
                    String relyName = findStr.substring(2, findStr.length() - 1);
                    InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = interfaceCaseRelyDataMapper.selectIfRelyDataByName(relyName);
                    if (null != interfaceCaseRelyDataVO) {
                        int relyCaseId = interfaceCaseRelyDataVO.getRelyCaseId();
                        if (caseId == relyCaseId) {
                            LOG.warn("修改接口测试用例，assert，接口依赖的用例不能为当前用例");
                            throw new BusinessException("接口依赖的用例不能为当前用例");
                        }
                    }
                }

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

        // 修改后置处理器
        List<InterfaceProcessorDO> postProcessors = interfaceCaseDTO.getPostProcessors();
        List<Integer> postProcessorIdList = interfaceProcessorService.findInterfaceProcessorIdByCaseId(interfaceCaseDTO.getCaseId());
        if (postProcessors != null) {
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
            // 移除该用例下所有的后置处理器
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
            // 删除与之相关的后置处理器
            interfaceProcessorService.removeInterfaceProcessorByCaseId(interfaceCaseId);
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
     * 执行指定用例
     * @param executeInterfaceCaseParam 入参
     * @return 自增日志编号
     * @throws BusinessException BusinessException
     */
    @Override
    public Integer executeInterfaceCase(ExecuteInterfaceCaseParam executeInterfaceCaseParam) throws BusinessException {
        Integer interfaceCaseId = executeInterfaceCaseParam.getInterfaceCaseId();
        String executor = executeInterfaceCaseParam.getExecutor();
        String suiteLogNo = executeInterfaceCaseParam.getSuiteLogNo();
        String chainNo = executeInterfaceCaseParam.getChainNo();
        Integer suiteId = executeInterfaceCaseParam.getSuiteId();
        Byte isFailedRetry = executeInterfaceCaseParam.getIsFailedRetry();
        String suiteLogDetailNo = executeInterfaceCaseParam.getSuiteLogDetailNo();
        HashMap globalHeaders = executeInterfaceCaseParam.getGlobalHeaders();
        HashMap globalParams = executeInterfaceCaseParam.getGlobalParams();
        HashMap globalData = executeInterfaceCaseParam.getGlobalData();
        LOG.info("---------------------------------开始执行测试用例：caseId={}---------------------------------", interfaceCaseId);
        String exceptionMessage = null;
        // 运行结果 0成功 1失败 2错误
        byte caseStatus = 0;

        // 1.获取case详情
        InterfaceCaseInfoVO interfaceCaseInfoVO = this.findInterfaceCaseByCaseId(interfaceCaseId);
        Integer projectId = interfaceCaseInfoVO.getProjectId();

        ProjectVO projectVO = projectService.findProjectById(projectId);
        // 获取项目域名
        String url;
        if (suiteId == null) { // 未指定suiteId进入调试模式,使用domain
            url = projectVO.getDomain() + interfaceCaseInfoVO.getUrl();
        } else { // 指定suiteId则获取suite运行环境
            // 根据suiteId获取运行环境
            InterfaceCaseSuiteVO suiteVO = ifSuiteService.findInterfaceCaseSuiteById(suiteId);
            Byte runDev = suiteVO.getRunDev();
            // 0dev 1test 2stg 3prod 4debug调试模式
            if (runDev == 0) {
                url = projectVO.getDevDomain() + interfaceCaseInfoVO.getUrl();
            } else if (runDev == 1) {
                url = projectVO.getTestDomain() + interfaceCaseInfoVO.getUrl();
            } else if (runDev == 2) {
                url = projectVO.getStgDomain() + interfaceCaseInfoVO.getUrl();
            } else if (runDev == 3) {
                url = projectVO.getProdDomain() + interfaceCaseInfoVO.getUrl();
            } else if (runDev == 4) {
                url = projectVO.getDomain() + interfaceCaseInfoVO.getUrl();
            } else {
                LOG.error("运行环境错误，invalid runDev={}", runDev);
                throw new BusinessException("运行环境错误");
            }

        }
        String desc = interfaceCaseInfoVO.getDesc();
        String headers = interfaceCaseInfoVO.getHeaders();
        String params = interfaceCaseInfoVO.getParams();
        String data = interfaceCaseInfoVO.getData();
        String json = interfaceCaseInfoVO.getJson();
        String rawHeaders = headers;
        String rawParams = params;
        String rawData = data;
        String rawJson = json;
        Byte method = interfaceCaseInfoVO.getMethod();
        List<InterfaceAssertVO> asserts = interfaceCaseInfoVO.getAsserts();
        LOG.info("1.获取case详情，caseId={}，用例详情={}", interfaceCaseId, interfaceCaseInfoVO);
        long runTime = 0;

        // 2.执行case
        LOG.info("2.执行case，caseId={}", interfaceCaseId);
        // a.获取请求方式  0get,1post,2update,3put,4delete
        ResponseEntity responseEntity = null;
        HashMap headersMap = null;
        HashMap paramsMap = null;
        HashMap dataMap = null;
        try {
            // 清洗
            if (null != headers) {
                headers = this.parseRelyData(headers, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData);
                LOG.info("清洗headers，清洗前的内容={}", rawHeaders);
                LOG.info("清洗headers，清洗后的内容={}", headers);
            }
            if (null != params) {
                params = this.parseRelyData(params, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData);
                LOG.info("清洗params，清洗前的内容={}", rawParams);
                LOG.info("清洗params，清洗后的内容={}", params);
            }
            if (null != data) {
                data = this.parseRelyData(data, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData);
                LOG.info("清洗data，清洗前的内容={}", rawData);
                LOG.info("清洗data，清洗后的内容={}", data);
            }
            if (null != json) {
                json = this.parseRelyData(json, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData);
                LOG.info("清洗json，清洗前的内容={}", rawJson);
                LOG.info("清洗json，清洗后的内容={}", json);
            }

            headersMap = JSONObject.parseObject(headers, HashMap.class);
            // 合并公共headers
            if (globalHeaders != null) {
                if (headersMap != null) {
                    globalHeaders.putAll(headersMap);
                }
                headersMap = globalHeaders;
            }
            paramsMap = JSONObject.parseObject(params, HashMap.class);
            // 合并公共params
            if (globalParams != null) {
                if (paramsMap != null) {
                    globalParams.putAll(paramsMap);
                }
                paramsMap = globalParams;
            }
            dataMap = JSONObject.parseObject(data, HashMap.class);
            // 合并公共data
            if (globalData != null) {
                if (dataMap == null && json == null) {
                    dataMap = globalData;
                } else if (dataMap != null){
                    globalData.putAll(dataMap);
                    dataMap = globalData;
                }
            }
            if (method == 0) { //get
                long startTime = System.currentTimeMillis();
                LOG.info("开始执行GET方法");
                responseEntity = RestUtil.get(url, headersMap, paramsMap);
                runTime = System.currentTimeMillis() - startTime;
            } else if (method == 1) { //post
                LOG.info("开始执行POST方法");
                long startTime = System.currentTimeMillis();
                responseEntity = RestUtil.post(url, headersMap, paramsMap, dataMap, json);
                runTime = System.currentTimeMillis() - startTime;
            } else if (method == 2) { //update
                LOG.warn("暂不支持update请求方式");
                throw new BusinessException("暂不支持update请求方式");
            } else if (method == 3) { //put
                LOG.warn("暂不支持put请求方式");
                throw new BusinessException("暂不支持put请求方式");
            } else if (method == 4) { //delete
                LOG.warn("暂不支持delete请求方式");
                throw new BusinessException("暂不支持delete请求方式");
            } else {
                LOG.error("不支持的请求方式");
                throw new BusinessException("不支持的请求方式");
            }
        } catch (ParseException | BusinessException | SqlException e) {
            // 自定义异常
            caseStatus = 2;
            e.printStackTrace();
            LOG.error(ExceptionUtil.msg(e));
            exceptionMessage = e.getMessage();
        } catch (ResourceAccessException e) {
            // 代理未开启
            caseStatus = 2;
            e.printStackTrace();
            LOG.error("请检查是否启用代理服务器");
            exceptionMessage = "请检查是否启用代理服务器";
        } catch (Exception e) {
            // 出现异常则追加错误信息，并将case状态设置为2错误
            caseStatus = 2;
            e.printStackTrace();
            exceptionMessage = ExceptionUtil.msg(e);
        }

        Integer responseCode = null;
        String responseHeaders = null;
        String responseBody = null;

        if (responseEntity == null) { // responseEntity == null, 则请求失败，只保存执行日志
            // 3.保存日志
            InterfaceCaseExecuteLogDO executeLogDO = new InterfaceCaseExecuteLogDO();
            executeLogDO.setCaseId(interfaceCaseId);
            executeLogDO.setCaseDesc(desc);
            executeLogDO.setRequestHeaders(JSON.toJSONString(headersMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestParams(JSON.toJSONString(paramsMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestData(JSON.toJSONString(dataMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestJson(json);
            executeLogDO.setRawRequestHeaders(rawHeaders);
            executeLogDO.setRawRequestParams(rawParams);
            executeLogDO.setRawRequestData(rawData);
            executeLogDO.setRawRequestJson(rawJson);
            executeLogDO.setResponseCode(null);
            executeLogDO.setResponseHeaders(null);
            executeLogDO.setResponseBody(null);
            // 后续加入拦截器后根据token反查
            executeLogDO.setExecuter(executor);
            executeLogDO.setStatus((byte) 2);
            executeLogDO.setCreatedTime(new Date());
            executeLogDO.setErrorMessage(exceptionMessage);
            executeLogDO.setRunTime(runTime);
            executeLogDO.setCaseUrl(url);
            executeLogDO.setSuiteLogNo(suiteLogNo);
            executeLogDO.setSuiteLogDetailNo(suiteLogDetailNo);
            executeLogDO.setIsFailedRetry(isFailedRetry);
            InterfaceCaseExecuteLogDO executedLogDO = executeLogService.saveExecuteLog(executeLogDO);
            // 返回自增id
            Integer executeLogId = executedLogDO.getId();

            InterfaceCaseExecuteLogDO updateChain = new InterfaceCaseExecuteLogDO();
            updateChain.setId(executeLogId);
            updateChain.setChain(redisUtil.stackGetAll(chainNo).toString());
            executeLogService.modifyExecuteLog(updateChain);

            LOG.warn("3.请求错误，仅保存执行，保存执行日志，日志内容={}，自增执行日志编号={}", executedLogDO, executeLogId);
            return executeLogId;
        } else { //请求成功记录执行日志和断言日志
            // 只有接口调用未出现异常才统计code、header、body
            responseCode = RestUtil.code(responseEntity);
            responseHeaders = RestUtil.headersPretty(responseEntity);
            responseBody = RestUtil.body(responseEntity);
            String temp = responseBody;
            try {
                JSONObject responseBodyObject = JSONObject.parseObject(temp);
                responseBody = JSON.toJSONString(responseBodyObject, SerializerFeature.PrettyFormat);
            } catch (Exception e) {
                responseBody = temp;
            }
            // 3.保存日志
            InterfaceCaseExecuteLogDO executeLogDO = new InterfaceCaseExecuteLogDO();
            executeLogDO.setCaseId(interfaceCaseId);
            executeLogDO.setCaseDesc(desc);
            executeLogDO.setRequestHeaders(JSON.toJSONString(headersMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestParams(JSON.toJSONString(paramsMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestData(JSON.toJSONString(dataMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestJson(json);
            executeLogDO.setRawRequestHeaders(rawHeaders);
            executeLogDO.setRawRequestParams(rawParams);
            executeLogDO.setRawRequestData(rawData);
            executeLogDO.setRawRequestJson(rawJson);
            executeLogDO.setResponseCode(responseCode);
            executeLogDO.setResponseHeaders(responseHeaders);
            executeLogDO.setResponseBody(responseBody);
            executeLogDO.setRunTime(runTime);
            executeLogDO.setExecuter(executor);
            executeLogDO.setStatus(caseStatus);
            executeLogDO.setCreatedTime(new Date());
            executeLogDO.setCaseUrl(url);
            executeLogDO.setSuiteLogNo(suiteLogNo);
            executeLogDO.setSuiteLogDetailNo(suiteLogDetailNo);
            executeLogDO.setIsFailedRetry(isFailedRetry);
            InterfaceCaseExecuteLogDO executedLogDO = executeLogService.saveExecuteLog(executeLogDO);
            // 4.保存断言日志表，获取运行日志自增id然后在断言日志表中写入断言信息，断言日志都成功后再将日志修改状态为0成功
            // 日志自增id
            int executedLogId = executedLogDO.getId();
            LOG.info("3.请求成功，保存执行日志，日志内容={}，自增执行日志编号={}", executedLogDO, executedLogId);


            // 断言执行前，执行从request提取类型
            // 即type=3request-header/4request-params/5request-data/6request-json的后置处理器
            // 但是response提取必须所有断言执行完成且用例状态为通过才提取
            if (1 == 1) { // 控制作用域 懒得再写。。。
                InterfaceProcessorDTO interfaceProcessorDTO = new InterfaceProcessorDTO();
                interfaceProcessorDTO.setCaseId(interfaceCaseId);
                List<InterfaceProcessorVO> processorList = interfaceProcessorService.findInterfaceProcessorList(interfaceProcessorDTO);
                InterfaceProcessorLogDO interfaceProcessorLogDO;
                String value = null;
                if (!processorList.isEmpty()) {
                    for(InterfaceProcessorVO postProcessorVO : processorList) {
                        interfaceProcessorLogDO = new InterfaceProcessorLogDO();
                        // 提取值
                        Byte type = postProcessorVO.getType();
                        String expression = postProcessorVO.getExpression();
                        String name = postProcessorVO.getName();
                        Byte haveDefaultValue = postProcessorVO.getHaveDefaultValue();
                        String defaultValue = postProcessorVO.getDefaultValue();
                        Byte isDefaultValue = null;
                        byte status = 0; // 状态 0通过 1失败
                        String errorMsg = "";
                        boolean isThrowException = false;
                        String exceptionMsg = "";
                        if (type >= 3 && type <= 6) { // 3request-header/4request-params/5request-data/6request-json
                            // 均使用jsonPath提取表达式
                            String s = "[]";
                            try {
                                switch (type) {
                                    case 3:
                                        s = ParseUtil.parseJson(headers, expression);
                                        break;
                                    case 4:
                                        s = ParseUtil.parseJson(params, expression);
                                        break;
                                    case 5:
                                        s = ParseUtil.parseJson(data, expression);
                                        break;
                                    case 6:
                                        s = ParseUtil.parseJson(json, expression);
                                        break;
                                }
                            } catch (ParseException e) {
                                isThrowException = true;
                                exceptionMsg = e.getMessage();
                            }
                            ArrayList jsonPathArray = JSONObject.parseObject(s, ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                if (haveDefaultValue == 0) {
                                    isDefaultValue = 0;
                                    value = defaultValue;
                                    LOG.info("提取类型={}，3request-header/4request-params/5request-data/6request-json，" +
                                            "提取表达式={} 为空，使用默认值={}", type, expression, value);
                                } else {
                                    status = 1;
                                    LOG.warn("jsonPath提取内容为空，jsonPath={}", expression);
                                    errorMsg = "后置处理器" + name + "提取类型为空，提取表达式为：" + expression;
                                }
                            } else {
                                isDefaultValue = 1;
                                if (jsonPathArray.size() == 1) {
                                    value = jsonPathArray.get(0).toString();
                                } else {
                                    value = JSON.toJSONString(jsonPathArray);
                                }
                                LOG.info("提取类型={}，提取表达式={}，写入缓存内容={}", "jsonPath", expression, value);
                            }
                            interfaceProcessorLogDO = new InterfaceProcessorLogDO();
                            interfaceProcessorLogDO.setProcessorId(postProcessorVO.getProcessorId());
                            interfaceProcessorLogDO.setCaseId(postProcessorVO.getCaseId());
                            interfaceProcessorLogDO.setCaseExecuteLogId(executedLogId);
                            interfaceProcessorLogDO.setName(name);
                            interfaceProcessorLogDO.setType(type);
                            interfaceProcessorLogDO.setExpression(expression);
                            interfaceProcessorLogDO.setWr((byte) 1);
                            if (!isThrowException) {
                                // 写入后置处理器记录表
                                interfaceProcessorLogDO.setIsDefaultValue(isDefaultValue);
                                interfaceProcessorLogDO.setValue(value);
                                interfaceProcessorLogDO.setCreatedTime(new Date());
                                interfaceProcessorLogDO.setStatus(status);
                                if (status == 0) {
                                    interfaceProcessorLogDO.setErrorMsg(null);
                                } else {
                                    interfaceProcessorLogDO.setErrorMsg(errorMsg);
                                }
                                // 写入后置处理器日志表
                                interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
                                LOG.info("写入后置处理器日志表");
                                // 如果后置处理器出错，那么将用例执行状态重新置为错误
                                if (status == 1) {
                                    InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                                    afterPostProcessorLogDo.setId(executedLogId);
                                    afterPostProcessorLogDo.setStatus((byte) 2);
                                    afterPostProcessorLogDo.setErrorMessage(errorMsg);
                                    executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                                    LOG.info("后置处理器运行错误，主动将用例执行状态置为错误");
                                } else {
                                    // 写入redis
                                    if (suiteLogDetailNo == null) { // 不存在则写入临时域
                                        redisUtil.hashPut(NoUtil.TEMP_POST_PROCESSOR_NO, name, value);
                                        LOG.info("写入后置处理器，key={}，hashKey={}，value={}", NoUtil.TEMP_POST_PROCESSOR_NO, name, value);
                                    } else { // 否则写入测试套件域
                                        redisUtil.hashPut(suiteLogDetailNo, name, value, 24*60*60);
                                        LOG.info("写入后置处理器，key={}，hashKey={}，value={}", suiteLogDetailNo, name, value);
                                    }
                                }
                            } else { // 若解析json、xpath出错
                                // 写入后置处理器记录表
                                interfaceProcessorLogDO.setIsDefaultValue(null);
                                interfaceProcessorLogDO.setValue(null);
                                interfaceProcessorLogDO.setCreatedTime(new Date());
                                interfaceProcessorLogDO.setStatus((byte) 1);
                                interfaceProcessorLogDO.setErrorMsg(exceptionMsg);
                                interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
                                LOG.warn("后置处理器解析出错，写入后置处理器记录表");
                                // 重置用例状态
                                InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                                afterPostProcessorLogDo.setId(executedLogId);
                                afterPostProcessorLogDo.setStatus((byte) 2);
                                afterPostProcessorLogDo.setErrorMessage(exceptionMsg);
                                executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                                LOG.info("后置处理器解析出错，主动将用例执行状态置为错误");
                            }
                        }
                    }
                }
            }

            // 遍历用例关联的断言,并写入断言日志表
            // 获取每次执行断言的状态
            List<Byte> statusList = new ArrayList<>();
            LOG.info("4.遍历用例的断言，依次执行... ... ...");
            for (InterfaceAssertVO interfaceAssertVO : asserts) {
                // 获取断言基本信息
                // 是否通过 0通过 1失败 2错误
                byte assertStatus = 0;
                String assertErrorMessage = null;
                Integer assertId = interfaceAssertVO.getAssertId();
                String assertName = interfaceAssertVO.getAssertName();
                Byte type = interfaceAssertVO.getType();
                String expression = interfaceAssertVO.getExpression();
                Byte operator = interfaceAssertVO.getOperator();
                Integer order = interfaceAssertVO.getOrder();
                // 写入断言日志表
                InterfaceAssertLogDO assertLogDO = new InterfaceAssertLogDO();
                assertLogDO.setExecuteLogId(executedLogId);
                assertLogDO.setCaseId(interfaceCaseId);
                assertLogDO.setAssertName(assertName);
                assertLogDO.setAssertId(assertId);
                assertLogDO.setType(type);
                assertLogDO.setExpression(expression);
                assertLogDO.setOperator(operator);
                String exceptedResult = interfaceAssertVO.getExceptedResult();
                assertLogDO.setRawExceptedResult(exceptedResult);
                if (null != exceptedResult) {
                    try {
                        // 清洗断言预期结果
                        LOG.info("5.清洗断言预期结果... ... ...");
                        exceptedResult = this.parseRelyData(exceptedResult, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData);
                        LOG.info("清洗断言，清洗后的结果={}", exceptedResult);
                    } catch (ParseException | BusinessException | SqlException e) {
                        assertErrorMessage = e.getMessage();
                        assertStatus = 2;
                        assertLogDO.setExceptedResult(exceptedResult);
                        assertLogDO.setOrder(order);
                        assertLogDO.setActualResult(null);
                        assertLogDO.setStatus(assertStatus);
                        // 将每次断言status都加入集合
                        statusList.add(assertStatus);
                        assertLogDO.setErrorMessage(assertErrorMessage);
                        assertLogDO.setCreatedTime(new Date());
                        LOG.error("清洗断言过程出现异常，断言状态置为失败，保存断言失败日志");
                        assertLogService.saveInterfaceAssertLog(assertLogDO);
                        continue;
                    }
                }
                assertLogDO.setExceptedResult(exceptedResult);
                assertLogDO.setOrder(order);
                // 根据type 提取数据类型   0json/1html/2header/3responseCode 来制定断言方案
                String actualResult = null;
                // 断言出错异常信息
                boolean isPass = false;
                ArrayList resultList = null;
                try {
                    if (type == 0) { // json
                        actualResult = ParseUtil.parseJson(responseBody, expression);
                        LOG.info("断言实际结果={}, 类型={}, 响应Body={}, 提取表达式={}", actualResult, "json", responseBody, expression);
                        resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                    } else if (type == 1) { // html
                        actualResult = ParseUtil.parseXml(responseBody, expression);
                        LOG.info("断言实际结果={}, 类型={}, 响应Body={}, 提取表达式={}", actualResult, "html", responseBody, expression);
                        resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                    } else if (type == 2) { // header
                        actualResult = ParseUtil.parseHttpHeader(responseEntity, expression);
                        LOG.info("断言实际结果={}, 类型={}, 响应header={}, 提取表达式={}", actualResult, "header", JSON.toJSONString(responseEntity.getHeaders()), expression);
                        resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                    } else if (type == 3) { // responseCode
                        actualResult = String.valueOf(ParseUtil.parseHttpCode(responseEntity));
                        LOG.info("断言实际结果={}, 类型={}, 响应code={}, 提取表达式={}", actualResult, "code", ParseUtil.parseHttpCode(responseEntity), expression);
                    } else {
                        throw new BusinessException("不支持的断言方式");
                    }
                    LOG.info("预期结果={}", exceptedResult);
                    LOG.info("操作符={}", operator);
                    LOG.info("实际结果={}", actualResult);
                    if (type != 3) {
                        if (resultList.size() == 1) {
                            actualResult = resultList.get(0).toString();
                        } else {
                            actualResult = JSON.toJSONString(resultList);
                        }
                    }
                    isPass = AssertUtil.asserts(actualResult, operator, exceptedResult);
                    if (isPass) {
                        LOG.info("断言通过");
                        assertStatus = 0;
                    } else {
                        LOG.warn("断言失败");
                        assertStatus = 1;
                    }
                } catch (BusinessException | ParseException e) {
                    assertStatus = 2;
                    LOG.error("断言错误, errorMsg={}", ExceptionUtil.msg(e));
                    // assertErrorMessage = ExceptionUtil.msg(e);
                    assertErrorMessage = e.getMessage();
                } catch (Exception e) {
                    assertStatus = 2;
                    LOG.error("断言错误, errorMsg={}", ExceptionUtil.msg(e));
                    assertErrorMessage = ExceptionUtil.msg(e);
                }
                assertLogDO.setActualResult(actualResult);
                assertLogDO.setStatus(assertStatus);
                // 将每次断言status都加入集合
                statusList.add(assertStatus);
                assertLogDO.setErrorMessage(assertErrorMessage);
                assertLogDO.setCreatedTime(new Date());
                LOG.info("保存断言日志");
                assertLogService.saveInterfaceAssertLog(assertLogDO);
            }
            InterfaceCaseExecuteLogDO updateStatus = new InterfaceCaseExecuteLogDO();
            // 上一步新增日志的自增id
            updateStatus.setId(executedLogId);
            if (statusList.contains((byte) 2)) { // 只要出现2，则用例算错误 status = 2
                updateStatus.setStatus((byte) 2);
            } else { // 没有出现2，且没有出现1， 则用例通过 status = 0
                if (!statusList.contains((byte) 1)) {
                    updateStatus.setStatus((byte) 0);
                } else { // 没有出现2，且没有出现0， 则用例通过 status = 1
                    updateStatus.setStatus((byte) 1);
                }
            }
            // 根据所有断言的执行状态再去修改执行日志状态
            executeLogService.modifyExecuteLog(updateStatus);
            LOG.info("根据所有断言的状态，再次修改执行日志的状态");
            LOG.info("---------------------------------测试用例执行完毕：caseId={}---------------------------------", interfaceCaseId);
            // 修改调用链
            InterfaceCaseExecuteLogDO updateChain = new InterfaceCaseExecuteLogDO();
            updateChain.setId(executedLogId);
            updateChain.setChain(redisUtil.stackGetAll(chainNo).toString());
            executeLogService.modifyExecuteLog(updateChain);


            // *仅用例执行通过才写入后置处理器记录
            if (updateStatus.getStatus() == 0 ) {
                InterfaceProcessorDTO interfaceProcessorDTO = new InterfaceProcessorDTO();
                interfaceProcessorDTO.setCaseId(interfaceCaseId);
                List<InterfaceProcessorVO> processorList = interfaceProcessorService.findInterfaceProcessorList(interfaceProcessorDTO);
                InterfaceProcessorLogDO interfaceProcessorLogDO;
                String value = null;
                if (!processorList.isEmpty()) {
                    for(InterfaceProcessorVO postProcessorVO : processorList) {
                        interfaceProcessorLogDO = new InterfaceProcessorLogDO();
                        // 提取值
                        Byte type = postProcessorVO.getType();
                        String expression = postProcessorVO.getExpression();
                        String name = postProcessorVO.getName();
                        Byte haveDefaultValue = postProcessorVO.getHaveDefaultValue();
                        String defaultValue = postProcessorVO.getDefaultValue();
                        Byte isDefaultValue = null;
                        byte status = 0; // 状态 0通过 1失败
                        String errorMsg = "";
                        boolean isThrowException = false;
                        String exceptionMsg = "";
                        if (type == 0 ) { // json
                            String s = "[]";
                            try {
                                s = ParseUtil.parseJson(responseBody, expression);
                            } catch (ParseException e) {
                                isThrowException = true;
                                exceptionMsg = e.getMessage();
                            }
                            ArrayList jsonPathArray = JSONObject.parseObject(s, ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                if (haveDefaultValue == 0) {
                                    isDefaultValue = 0;
                                    value = defaultValue;
                                    LOG.info("提取类型={}，提取表达式={} 为空，使用默认值={}", "response-json", expression, value);
                                } else {
                                    status = 1;
                                    LOG.warn("response-json提取内容为空，jsonPath={}", expression);
                                    errorMsg = "后置处理器" + name + "提取类型为空，提取表达式为：" + expression;
                                }
                            } else {
                                isDefaultValue = 1;
                                if (jsonPathArray.size() == 1) {
                                    value = jsonPathArray.get(0).toString();
                                } else {
                                    value = JSON.toJSONString(jsonPathArray);
                                }
                                LOG.info("提取类型={}，提取表达式={}，写入缓存内容={}", "response-json", expression, value);
                            }
                        } else if (type == 1) { // xml
                            String s = "[]";
                            try {
                                s = ParseUtil.parseXml(responseBody, expression);
                            } catch (ParseException e) {
                                isThrowException = true;
                                exceptionMsg = e.getMessage();
                            }
                            ArrayList xpathArray = JSONObject.parseObject(s, ArrayList.class);
                            if (xpathArray.isEmpty()) {
                                if (haveDefaultValue == 0) {
                                    isDefaultValue = 0;
                                    value = defaultValue;
                                    LOG.info("提取类型={}，提取表达式={} 为空，使用默认值={}", "response-xml", expression, value);
                                } else {
                                    status = 1;
                                    LOG.warn("response-xml提取内容为空，xpath={}", expression);
                                    errorMsg = "后置处理器" + name + "提取类型为空，提取表达式为：" + expression;
                                }
                            } else {
                                isDefaultValue = 1;
                                if (xpathArray.size() == 1) {
                                    value = xpathArray.get(0).toString();
                                } else {
                                    value = JSON.toJSONString(xpathArray);
                                }
                                LOG.info("提取类型={}，提取表达式={}，写入缓存内容={}", "response-xml", expression, value);
                            }
                        } else if (type == 2) { //header
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(expression);
                            if (headerArray == null || headerArray.isEmpty()) {
                                if (haveDefaultValue == 0) {
                                    isDefaultValue = 0;
                                    value = defaultValue;
                                    LOG.info("提取类型={}，提取表达式={} 为空，使用默认值={}", "header", expression, value);
                                } else {
                                    status = 1;
                                    LOG.warn("header提取内容为空，header={}", expression);
                                    errorMsg = "后置处理器" + name + "提取类型为空，提取表达式为：" + expression;
                                }
                            } else {
                                isDefaultValue = 1;
                                if (headerArray.size() == 1) {
                                    value = headerArray.get(0).toString();
                                } else {
                                    value = JSON.toJSONString(headerArray);
                                }
                            }
                        } else if (type >= 7) {
                            status = 1;
                            LOG.error("后置处理器提取类型错误");
                            errorMsg = "后置处理器提取类型错误";
                        }
                        if (type <= 2 || type >7 ) { // 3-6时，已在前面写入缓存
                            interfaceProcessorLogDO = new InterfaceProcessorLogDO();
                            interfaceProcessorLogDO.setProcessorId(postProcessorVO.getProcessorId());
                            interfaceProcessorLogDO.setCaseId(postProcessorVO.getCaseId());
                            interfaceProcessorLogDO.setCaseExecuteLogId(executedLogId);
                            interfaceProcessorLogDO.setName(name);
                            interfaceProcessorLogDO.setType(type);
                            interfaceProcessorLogDO.setExpression(expression);
                            if (!isThrowException) {
                                // 写入后置处理器记录表
                                interfaceProcessorLogDO.setIsDefaultValue(isDefaultValue);
                                interfaceProcessorLogDO.setValue(value);
                                interfaceProcessorLogDO.setCreatedTime(new Date());
                                interfaceProcessorLogDO.setStatus(status);
                                interfaceProcessorLogDO.setWr((byte) 1);
                                if (status == 0) {
                                    interfaceProcessorLogDO.setErrorMsg(null);
                                } else {
                                    interfaceProcessorLogDO.setErrorMsg(errorMsg);
                                }
                                // 写入后置处理器日志表
                                interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
                                LOG.info("写入后置处理器日志表");
                                // 如果后置处理器出错，那么将用例执行状态重新置为错误
                                if (status == 1) {
                                    InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                                    afterPostProcessorLogDo.setId(executedLogId);
                                    afterPostProcessorLogDo.setStatus((byte) 2);
                                    afterPostProcessorLogDo.setErrorMessage(errorMsg);
                                    executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                                    LOG.info("后置处理器运行错误，主动将用例执行状态置为错误");
                                } else {
                                    if (suiteLogDetailNo == null) { // 不存在则写入临时域
                                        redisUtil.hashPut(NoUtil.TEMP_POST_PROCESSOR_NO, name, value);
                                        LOG.info("写入后置处理器，key={}，hashKey={}，value={}", NoUtil.TEMP_POST_PROCESSOR_NO, name, value);
                                    } else { // 否则写入测试套件域
                                        redisUtil.hashPut(suiteLogDetailNo, name, value, 24*60*60);
                                        LOG.info("写入后置处理器，key={}，hashKey={}，value={}", suiteLogDetailNo, name, value);
                                    }
                                }
                            } else { // 若解析json、xpath出错
                                // 写入后置处理器记录表
                                interfaceProcessorLogDO.setIsDefaultValue(null);
                                interfaceProcessorLogDO.setValue(null);
                                interfaceProcessorLogDO.setCreatedTime(new Date());
                                interfaceProcessorLogDO.setStatus((byte) 1);
                                interfaceProcessorLogDO.setErrorMsg(exceptionMsg);
                                interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
                                LOG.warn("后置处理器解析出错，写入后置处理器记录表");
                                // 重置用例状态
                                InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                                afterPostProcessorLogDo.setId(executedLogId);
                                afterPostProcessorLogDo.setStatus((byte) 2);
                                afterPostProcessorLogDo.setErrorMessage(exceptionMsg);
                                executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                                LOG.info("后置处理器解析出错，主动将用例执行状态置为错误");
                            }
                        }
                    }
                }
            }
            return executedLogId;
        }
    }

    /**
     * 字符清洗
     * @param s 待清洗数据
     * @param chainNo 调用链路跟踪 每次调用均会将自增日志编号写入缓存，再序列化
     * @param suiteId 测试套件编号，主要用于调用入口为测试套件时确定运行环境，否则应该传参null
     * @param isFailedRetry 判断该执行日志是否为失败重试，0是1否，主要用于测试报告统计断言情况
     * @param suiteLogDetailNo suiteLogNo仅记录重跑和真正运行的，suiteLogDetailNo会包括用例所依赖的case
     * @return 清洗后的数据
     * @throws ParseException ParseException
     * @throws BusinessException BusinessException
     * @throws SqlException SqlException
     */
    public String parseRelyData(String s, String chainNo, Integer suiteId, Byte isFailedRetry, String suiteLogDetailNo,
    HashMap globalHeaders, HashMap globalParams, HashMap globalData)
            throws ParseException, BusinessException, SqlException {

        // 解析后置处理器
        s = parsePostProcessor(s, suiteLogDetailNo);

        LOG.info("--------------------------------------开始字符串解析流程--------------------------------------");
        LOG.info("--------------------------------------待解析字符串原文={}", s);
        // env 0dev1test2stg3prod4debug
        Byte runEnv;
        if (suiteId == null) {
            runEnv = 4;
        } else {
            runEnv = ifSuiteService.findInterfaceCaseSuiteById(suiteId).getRunDev();
        }
        LOG.info("--------------------------------------运行环境={}, 0dev 1test 2stg 3prod 4debug", runEnv);
        Pattern p = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            // 获取relyName
            String relyName = findStr.substring(2, findStr.length() - 1);
            LOG.info("relyName={}", relyName);
// 进入普通依赖数据模式-再进入根据数组下标模式
            if (Pattern.matches("[a-zA-Z]+\\[[0-9]+\\]", relyName)) {
                LOG.info("--------------------------------------进入数组下标取值模式");
                // if (relyName.indexOf("[") != -1 && relyName.endsWith("]")) {
                // 判断出现次数,首次出现和最后一次出现位置不一致，则说明[>1 ]>1
                if (relyName.indexOf("[") != relyName.lastIndexOf("[") ||
                        relyName.indexOf("]") != relyName.lastIndexOf("]")) {
                    LOG.warn("数据取值语法错误，relyName={}", relyName);
                    throw new ParseException("数组取值语法错误");
                }
                String indexStr = relyName.substring(relyName.indexOf("[") + 1, relyName.length() - 1);
                try {
                    int index = Integer.parseInt(indexStr);
                    LOG.info("数据下标={}", index);
                    relyName = relyName.substring(0, relyName.indexOf("["));
                    LOG.info("去除下标后的真实relyName={}", relyName);
                    // 查询其所依赖的caseId
                    InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                    interfaceCaseRelyDataDTO.setRelyName(relyName);
                    LOG.info("根据relyName查询用例信息，relyName={}", relyName);
                    InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                    if (null == interfaceCaseRelyDataVO) {
                        LOG.warn("未找到对应的用例信息，relyName={}", relyName);
                        throw new ParseException("未找到该依赖数值");
                    }
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    LOG.info("获取到的用例编号={}", caseId);
                    // 根据caseId调用相应case
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(
                            caseId, "系统调度", null, chainNo, suiteId,
                            isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData));
                    redisUtil.stackPush(chainNo, executeLogId);

                    LOG.info("执行用例编号={}，执行日志编号={}", caseId, executeLogId);
                    // 获取case执行结果, 不等于0, 则用例未通过
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        LOG.warn("前置用例执行未通过，执行用例编号={}，执行日志编号={}", caseId, executeLogId);
                        throw new BusinessException("前置用例执行未通过");
                    }
                    // 根据executeLogId查询对应的执行记录
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    LOG.warn("前置用例responseBody={}", responseBody);
                    LOG.warn("前置用例responseHeaders={}", responseHeaders);
                    // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
//                    if (contentType != 2) {
//                        throw new ParseException("只有依赖数据提取类型为header时才支持指定下标，" +
//                                "否则请自行调整jsonpath/xpath表达式，使提取结果唯一");
//                    }
                    // 2020.09.27 xpath/jsonPath也支持下标
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                LOG.warn("jsonPath提取内容为空，jsonPath={}", expression);
                                throw new ParseException(expression + "提取内容为空");
                            }
                            try {
                                s = s.replace(findStr, jsonPathArray.get(index).toString());
                                LOG.info("jsonPath提取值并替换后的结果={}", s);
                            } catch (Exception e) {
                                LOG.warn("jsonPath数组下表越界，relyName={}, index={}", relyName, index);
                                throw new ParseException(relyName + " 数组下标越界");
                            }
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            if (xpathArray.isEmpty()) {
                                LOG.warn("xpath提取内容为空，xpath={}", expression);
                                throw new ParseException(expression + "提取内容为空");
                            }
                            try {
                                s = s.replace(findStr, xpathArray.get(index).toString());
                                LOG.info("xpath提取值并替换后的结果={}", s);
                            } catch (Exception e) {
                                LOG.warn("xpath数组下表越界，relyName={}, index={}", relyName, index);
                                throw new ParseException(relyName + " 数组下标越界");
                            }
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(expression);
                            if (null == headerArray || headerArray.isEmpty()) {
                                LOG.warn("未找到请求头，header={}", expression);
                                throw new ParseException("未找到请求头:" + expression);
                            }
                            try {
                                s = s.replace(findStr, headerArray.get(index).toString());
                                LOG.info("header提取值并替换后的结果={}", s);
                            } catch (Exception e) {
                                LOG.warn("header数组下表越界，header={}, index={}", expression, index);
                                throw new ParseException(expression + " 数组下标越界");
                            }
                        } else {
                            throw new BusinessException("不支持该contentType");
                        }
                    } catch (BusinessException e) {
                        LOG.error("不支持该contentType");
                        throw new BusinessException("不支持该contentType");
                    } catch (Exception e) {
                        LOG.error("下标取值模式执行异常，errorMsg={}", ExceptionUtil.msg(e));
                        throw new ParseException(e.getMessage());
                    }
                } catch (NumberFormatException e) {
                    LOG.error("数组下标只能为数字");
                    throw new ParseException("数组下标只能为数字");
                }
// 进入预置函数模式
            } else if (Pattern.matches("\\w+\\((,?|(\\\".+\\\")?|\\s?)+\\)$", relyName)) {
                LOG.info("--------------------------------------进入预置方法/动态SQL模式");
                // } else if (relyName.indexOf("(") != -1 && relyName.endsWith(")")) {
                // 判断出现次数,首次出现和最后一次出现位置不一致，则说明(>1 )>1
                if (relyName.indexOf("(") != relyName.lastIndexOf("(") ||
                        relyName.indexOf(")") != relyName.lastIndexOf(")")) {
                    LOG.warn("预置方法/动态SQL语法错误， string={}", relyName);
                    throw new ParseException("预置方法/动态SQL语法错误");
                }
                // 获取方法名称
                String methodName = relyName.substring(0, relyName.indexOf("("));
                LOG.warn("预置方法名称/动态SQL依赖名称={}", methodName);
                // 获取参数列表, 去除引号空格
                String[] params = relyName.substring(relyName.indexOf("(") + 1, relyName.length() - 1)
                        .replaceAll(",\\s+", ",").split(",");
                RelyDataVO relyDataVO = relyDataService.findRelyDataByName(methodName);
                if (null == relyDataVO) {
                    LOG.warn("未找到该预置方法/动态SQL依赖名称， string={}", methodName);
                    throw new ParseException("未找到该预置方法/动态SQL依赖名称");
                }
                if (relyDataVO.getType() == 1) { //反射方法
                    LOG.info("--------------------------------------进入预置方法模式");
                    // 无参方法特殊处理
                    if (params.length == 1 && "".equals(params[0])) {
                        params = new String[0];
                    }
                    // 反射执行对应方法
                    try {
                        Class<?> clazz = Class.forName("org.alex.platform.common.ReflectMethod");
                        Class[] paramsList = new Class[params.length];
                        for (int i = 0; i < params.length; i++) {
                            paramsList[i] = String.class;
                            // 去除首尾引号
                            params[i] = params[i].substring(1, params[i].length() - 1);
                        }
                        LOG.info("方法名称={}，方法参数={}", methodName, Arrays.toString(params));
                        Method method = clazz.getMethod(methodName, paramsList);
                        s = s.replace(findStr, (String) method.invoke(clazz.newInstance(), params));
                        LOG.info("预置方法执行并替换后的结果={}", s);
                    } catch (Exception e) {
                        LOG.error("未找到依赖方法或者入参错误, errorMsg={}", ExceptionUtil.msg(e));
                        throw new ParseException("未找到依赖方法或者入参错误, errorMsg=" + ExceptionUtil.msg(e));
                    }
                } else if (relyDataVO.getType() == 2) { //sql
                    LOG.info("--------------------------------------进入动态SQL模式");
                    for (int i = 0; i < params.length; i++) {
                        // 去除首尾引号
                        params[i] = params[i].substring(1, params[i].length() - 1);
                    }
                    Integer datasourceId = relyDataVO.getDatasourceId();
                    if (null == datasourceId) {
                        LOG.warn("SQL依赖名称未找到对应的数据源");
                        throw new ParseException("SQL依赖名称未找到对应的数据源");
                    }
                    DbVO dbVO = dbService.findDbById(datasourceId);
                    // 0启动 1禁用
                    int status = dbVO.getStatus();
                    if (status == 1) {
                        LOG.warn("数据源已被禁用，dbName={}", dbVO.getName());
                        throw new ParseException("数据源已被禁用");
                    }
                    String url;
                    String username;
                    String password;
                    // env 0dev 1test 2stg 3prod 4debug
                    if (runEnv == 4) {
                        url = dbVO.getUrl();
                        username = dbVO.getUsername();
                        password = dbVO.getPassword();
                    } else if (runEnv == 0) {
                        url = dbVO.getDevUrl();
                        username = dbVO.getDevUsername();
                        password = dbVO.getDevPassword();
                    } else if (runEnv == 1) {
                        url = dbVO.getTestUrl();
                        username = dbVO.getTestUsername();
                        password = dbVO.getTestPassword();
                    } else if (runEnv == 2) {
                        url = dbVO.getStgUrl();
                        username = dbVO.getStgUsername();
                        password = dbVO.getStgPassword();
                    } else if (runEnv == 3) {
                        url = dbVO.getProdUrl();
                        username = dbVO.getProdUsername();
                        password = dbVO.getProdPassword();
                    } else {
                        throw new BusinessException("数据源确定运行环境时出错");
                    }
                    // 支持动态sql
                    String sql = relyDataVO.getValue();
                    if (relyDataVO.getValue() != null) {
                        LOG.info("开始解析SQL，解析前SQL={}", sql);
                        sql = parseRelyData(sql, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData);
                        LOG.info("解析SQL完成，解析后SQL={}", sql);
                    }
                    LOG.info("SQL执行参数，SQL={}, params={}", sql, params);
                    String sqlResult = JdbcUtil.selectFirstColumn(url, username, password, sql, params);
                    s = s.replace(findStr, sqlResult);
                }

// 进入普通依赖数据模式
            } else {
                LOG.info("--------------------------------------进入普通依赖数据模式");
                // 查询其所依赖的caseId
                InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                interfaceCaseRelyDataDTO.setRelyName(relyName);
                InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                // 判断是否在t_interface_case_rely_data
                if (null == interfaceCaseRelyDataVO) {
                    RelyDataVO relyDataVO = relyDataService.findRelyDataByName(relyName);
                    // 判断是否在t_rely_data
                    if (null == relyDataVO) {
                        LOG.warn("未找到该依赖数值，relyName={}", relyName);
                        throw new ParseException("未找到该依赖数值");
                    } else {
                        // 此处不考虑反射函数类型，已经在${xx()}步骤处理
                        // 依赖类型 0固定值 1反射方法 2sql
                        int type = relyDataVO.getType();
                        if (type == 0) {
                            s = s.replace(findStr, relyDataVO.getValue());
                        } else if (type == 2) {
                            Integer datasourceId = relyDataVO.getDatasourceId();
                            if (null == datasourceId) {
                                LOG.warn("SQL依赖名称未找到对应的数据源");
                                throw new ParseException("SQL依赖名称未找到对应的数据源");
                            }
                            DbVO dbVO = dbService.findDbById(datasourceId);
                            // 0启动 1禁用
                            int status = dbVO.getStatus();
                            if (status == 1) {
                                LOG.warn("数据源已被禁用，dbName={}", dbVO.getName());
                                throw new ParseException("数据源已被禁用");
                            }
                            String url;
                            String username;
                            String password;
                            // env 0dev 1test 2stg 3prod 4debug
                            if (runEnv == 4) {
                                url = dbVO.getUrl();
                                username = dbVO.getUsername();
                                password = dbVO.getPassword();
                            } else if (runEnv == 0) {
                                url = dbVO.getDevUrl();
                                username = dbVO.getDevUsername();
                                password = dbVO.getDevPassword();
                            } else if (runEnv == 1) {
                                url = dbVO.getTestUrl();
                                username = dbVO.getTestUsername();
                                password = dbVO.getTestPassword();
                            } else if (runEnv == 2) {
                                url = dbVO.getStgUrl();
                                username = dbVO.getStgUsername();
                                password = dbVO.getStgPassword();
                            } else if (runEnv == 3) {
                                url = dbVO.getProdUrl();
                                username = dbVO.getProdUsername();
                                password = dbVO.getProdPassword();
                            } else {
                                throw new BusinessException("数据源确定运行环境时出错");
                            }
                            // 支持动态sql
                            String sql = relyDataVO.getValue();
                            if (relyDataVO.getValue() != null) {
                                LOG.info("开始解析SQL，解析前SQL={}", sql);
                                sql = parseRelyData(sql, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData);
                                LOG.info("解析SQL完成，解析后SQL={}", sql);
                            }
                            LOG.info("SQL执行参数，SQL={}", sql);
                            String sqlResult = JdbcUtil.selectFirstColumn(url, username, password, sql);
                            s = s.replace(findStr, sqlResult);
                        }
                    }
                } else {
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    // 根据caseId调用相应case
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId,
                            "系统调度", null, chainNo, suiteId, isFailedRetry, suiteLogDetailNo,
                            globalHeaders, globalParams, globalData));
                    redisUtil.stackPush(chainNo, executeLogId);

                    // 获取case执行结果, 不等于0, 则用例未通过
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        LOG.warn("前置用例执行未通过");
                        throw new BusinessException("前置用例执行未通过");
                    }
                    // 根据executeLogId查询对应的执行记录
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    LOG.warn("前置用例responseBody={}", responseBody);
                    LOG.warn("前置用例responseHeaders={}", responseHeaders);
                    // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                LOG.warn("jsonPath提取内容为空，jsonPath={}", expression);
                                throw new ParseException(expression + "提取内容为空");
                            }
                            if (jsonPathArray.size() == 1) {
                                s = s.replace(findStr, jsonPathArray.get(0).toString());
                            } else {
                                s = s.replace(findStr, JSON.toJSONString(jsonPathArray));
                            }
                            LOG.info("jsonPath提取值并替换后的结果={}", s);
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            if (xpathArray.isEmpty()) {
                                LOG.warn("xpath提取内容为空，jsonPath={}", expression);
                                throw new ParseException(expression + "提取内容为空");
                            }
                            if (xpathArray.size() == 1) {
                                s = s.replace(findStr, xpathArray.get(0).toString());
                            } else {
                                s = s.replace(findStr, JSON.toJSONString(xpathArray));
                            }
                            LOG.info("xml提取值并替换后的结果={}", s);
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders,
                                    HashMap.class).get(expression);
                            if (headerArray == null || headerArray.isEmpty()) {
                                LOG.warn("未找到请求头，header={}", expression);
                                throw new ParseException("未找到请求头:" + expression);
                            } else {
                                if (headerArray.size() == 1) {
                                    s = s.replace(findStr, headerArray.get(0).toString());
                                } else {
                                    s = s.replace(findStr, JSON.toJSONString(headerArray));
                                }
                                LOG.info("header提取值并替换后的结果={}", s);
                            }
                        } else {
                            throw new BusinessException("不支持该种取值方式");
                        }
                    } catch (BusinessException e) {
                        LOG.warn("不支持该种取值方式");
                        throw new BusinessException("不支持该种取值方式");
                    } catch (Exception e) {
                        LOG.error("普通依赖数据模式执行异常，errorMsg={}", ExceptionUtil.msg(e));
                        throw new ParseException("普通依赖数据模式执行异常，errorMsg=" + ExceptionUtil.msg(e));
                    }
                }
            }
        }
        return s;
    }

    /**
     * 解析后置处理器
     * @param s 原文
     * @param suiteLogDetailNo 非空时使用测试套件域，否则使用临时域
     * @return 解析后的字符串
     * @throws ParseException 找不到后置处理器
     */
    @Override
    public String parsePostProcessor(String s, String suiteLogDetailNo) throws ParseException {
        LOG.info("--------------------------------------开始后置处理器提取解析流程--------------------------------------");
        LOG.info("--------------------------------------待解析字符串原文={}", s);
        Pattern pattern = Pattern.compile("#\\{.+?\\}");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            String postProcessorName = findStr.substring(2, findStr.length() - 1);
            InterfaceProcessorVO postProcessor = interfaceProcessorService.findInterfaceProcessorByName(postProcessorName);
            LOG.info("后置处理器名称={}", postProcessorName);
            Object redisResult;
            if (suiteLogDetailNo == null) {
                LOG.info("suiteLogDetailNo == null，使用临时变量域");
                redisResult = redisUtil.hashGet(NoUtil.TEMP_POST_PROCESSOR_NO, postProcessorName);
            } else {
                LOG.info("suiteLogDetailNo != null，使用测试套件域");
                redisResult = redisUtil.hashGet(suiteLogDetailNo, postProcessorName);
            }
            if (redisResult == null) {
                LOG.error("未找到后置处理器" + postProcessorName);
                throw new ParseException("未找到后置处理器" + postProcessorName);
            }
            String redisResultStr = redisResult.toString();
            s = s.replace(findStr, redisResultStr);

            // 写入后置处理器日志表
            InterfaceProcessorLogDO interfaceProcessorLogDO = new InterfaceProcessorLogDO();
            interfaceProcessorLogDO.setName(postProcessor.getName());
            interfaceProcessorLogDO.setValue(redisResultStr);
            interfaceProcessorLogDO.setCreatedTime(new Date());
            interfaceProcessorLogDO.setStatus((byte) 0);
            interfaceProcessorLogDO.setWr((byte)0);
            interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
        }
        LOG.info("--------------------------------------结束后置处理器提取解析流程--------------------------------------");
        LOG.info("--------------------------------------解析后的字符串为={}", s);
        return s;
    }
}
