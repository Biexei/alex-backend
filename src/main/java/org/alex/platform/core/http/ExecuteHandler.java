package org.alex.platform.core.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.alex.platform.common.Env;
import org.alex.platform.core.common.Node;
import org.alex.platform.core.parser.Parser;
import org.alex.platform.enums.RelyType;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;

import org.alex.platform.pojo.*;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.alex.platform.service.*;
import org.alex.platform.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;

@Component
@SuppressWarnings({"unchecked","rawtypes"})
public class ExecuteHandler implements Node {

    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;
    @Autowired
    ProjectService projectService;
    @Autowired
    InterfaceAssertLogService assertLogService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    InterfaceCaseSuiteService ifSuiteService;
    @Autowired
    InterfaceProcessorService interfaceProcessorService;
    @Autowired
    InterfaceProcessorLogService interfaceProcessorLogService;
    @Autowired
    InterfacePreCaseService interfacePreCaseService;
    @Autowired
    Env env;
    @Autowired
    Parser parser;

    private static final Logger LOG = LoggerFactory.getLogger(ExecuteHandler.class);

    /**
     * 执行指定用例
     * @param executeInterfaceCaseParam 入参
     * @return 自增日志编号
     * @throws BusinessException BusinessException
     */
    public Integer executeInterfaceCase(ExecuteInterfaceCaseParam executeInterfaceCaseParam) throws BusinessException {
        long methodStart = TimeUtil.now();
        RelyType startRelyType;
        RelyType endRelyType;

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
        Byte source = executeInterfaceCaseParam.getSource();
        String casePreNo = executeInterfaceCaseParam.getCasePreNo();

        // source 来源（0用例调试 1依赖调试 2运行整个测试套件 3测试套件单个用例调试 4依赖解析 5综合用例-前置用例）
        switch (source) {
            case 1:
            case 4:
                startRelyType = RelyType.RELY_START;
                endRelyType = RelyType.RELY_END;
                break;
            case 5:
                startRelyType = RelyType.PRE_CASE_START;
                endRelyType = RelyType.PRE_CASE_END;
                break;
            default:
                startRelyType = RelyType.CASE_START;
                endRelyType = RelyType.CASE_END;
                break;
        }
        LOG.info("---------------------------------开始执行测试用例：caseId={}---------------------------------", interfaceCaseId);
        String exceptionMessage = null;
        // 运行结果 0成功 1失败 2错误
        byte caseStatus = 0;
        // 1.获取case详情
        InterfaceCaseInfoVO interfaceCaseInfoVO = interfaceCaseService.findInterfaceCaseByCaseId(interfaceCaseId);
        redisUtil.stackPush(chainNo, chainNode(startRelyType, null, interfaceCaseInfoVO.getDesc(), null, TimeUtil.now()-methodStart, null));
        Integer projectId = interfaceCaseInfoVO.getProjectId();

        ProjectVO projectVO = projectService.findProjectById(projectId);
        String desc = interfaceCaseInfoVO.getDesc();
        String url;
        if (suiteId == null) { // 未指定suiteId进入调试模式,使用domain
            url = env.domain(projectVO, (byte)4) + interfaceCaseInfoVO.getUrl();
        } else { // 指定suiteId则获取suite运行环境
            // 根据suiteId获取运行环境
            InterfaceCaseSuiteVO suiteVO = ifSuiteService.findInterfaceCaseSuiteById(suiteId);
            Byte runDev = suiteVO.getRunDev();
            // 0dev 1test 2stg 3prod 4debug调试模式
            url = env.domain(projectVO, runDev) + interfaceCaseInfoVO.getUrl();
        }

        String headers = kvCast(interfaceCaseInfoVO.getHeaders());
        String params = kvCast(interfaceCaseInfoVO.getParams());
        String formData = kvCast(interfaceCaseInfoVO.getFormData());
        String formDataEncoded = kvCast(interfaceCaseInfoVO.getFormDataEncoded());

        String raw = interfaceCaseInfoVO.getRaw();
        String rawType = interfaceCaseInfoVO.getRawType();
        Byte bodyType = interfaceCaseInfoVO.getBodyType();

        String rawHeaders = headers;
        String rawParams = params;
        String rawBody;
        String requestBody = null; // 记录请求日志body字段
        if (bodyType == 0) { //form-data
            rawBody = formData;
        } else if (bodyType == 1) { //x-www-form-encoded
            rawBody = formDataEncoded;
        } else if (bodyType == 2) { //raw
            rawBody = raw;
        } else { //none
            rawBody = null;
        }
        Byte method = interfaceCaseInfoVO.getMethod();
        List<InterfaceAssertVO> asserts = interfaceCaseInfoVO.getAsserts();
        LOG.info("1.获取case详情，caseId={}，用例详情={}", interfaceCaseId, interfaceCaseInfoVO);
        long runTime = 0;


        // 执行前置用例
        List<InterfacePreCaseDO> preCaseList = interfacePreCaseService.findInterfacePreCaseByParentId(interfaceCaseId);
        if (!preCaseList.isEmpty()) {
            String no = NoUtil.genCasePreNo();
            // 重置casePreNo
            casePreNo = no;
            for(InterfacePreCaseDO preCase : preCaseList) {
                Integer caseId = preCase.getPreCaseId();
                LOG.info("开始执行执行前置用例{}", caseId);
                executeInterfaceCaseParam.setSource((byte)5);
                executeInterfaceCaseParam.setCasePreNo(no);
                executeInterfaceCaseParam.setExecutor("前置用例");
                executeInterfaceCaseParam.setSuiteLogNo(null); // 把前置用例作为中间依赖case处理
                executeInterfaceCaseParam.setInterfaceCaseId(caseId);
                try {
                    this.executeInterfaceCase(executeInterfaceCaseParam);
                } catch (BusinessException e) {
                    caseStatus = 2;
                    e.printStackTrace();
                    LOG.error("前置用例执行失败!" + ExceptionUtil.msg(e));
                    exceptionMessage = "前置用例执行失败!" + ExceptionUtil.msg(e);
                }
                LOG.info("前置用例{}执行完成", caseId);
            }
        }


        // 2.执行case
        LOG.info("2.执行case，caseId={}", interfaceCaseId);
        // a.获取请求方式  0get,1post,2update,3put,4delete
        ResponseEntity responseEntity = null;
        HashMap headersMap = null;
        HashMap paramsMap = null;
        HashMap formDataEncodedMap = null;
        HashMap formDataMap = null;
        try {
            // 清洗
            if (null != headers) {
                headers = parser.parseDependency(headers, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                LOG.info("清洗headers，清洗前的内容={}", rawHeaders);
                LOG.info("清洗headers，清洗后的内容={}", headers);
            }
            if (null != params) {
                params = parser.parseDependency(params, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                LOG.info("清洗params，清洗前的内容={}", rawParams);
                LOG.info("清洗params，清洗后的内容={}", params);
            }

            if (bodyType == 0) { //form-data
                formData = parser.parseDependency(formData, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                LOG.info("清洗body，form-data，清洗前的内容={}", rawBody);
                LOG.info("清洗body，form-data，清洗后的内容={}", formData);
            } else if (bodyType == 1) { //x-www-form-encoded
                formDataEncoded = parser.parseDependency(formDataEncoded, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                LOG.info("清洗body，x-www-form-encoded，清洗前的内容={}", rawBody);
                LOG.info("清洗body，x-www-form-encoded，清洗后的内容={}", formDataEncoded);
            } else if (bodyType == 2) { //raw
                raw = parser.parseDependency(raw, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                LOG.info("清洗body，raw，清洗前的内容={}", rawBody);
                LOG.info("清洗body，raw，清洗后的内容={}", raw);
            } else if (bodyType == 9) { //none
                LOG.info("body type为none，不清洗");
            } else {
                throw new BusinessException("bodyType参数错误");
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
            // 合并公共formDataEncoded
            if (bodyType == 0) {
                formDataMap = JSONObject.parseObject(formData, HashMap.class);
            } else if (bodyType == 1) { //x-www-form-encoded
                formDataEncodedMap = JSONObject.parseObject(formDataEncoded, HashMap.class);
                if (globalData != null) {
                    if (formDataEncodedMap != null) {
                        globalParams.putAll(formDataEncodedMap);
                    }
                    formDataEncodedMap = globalData;
                }
            }

            // 确定请求方式
            HttpMethod methodEnum;
            if (method == 0) {
                methodEnum = HttpMethod.GET;
            } else if (method == 1) {
                methodEnum = HttpMethod.POST;
            } else if (method == 2) {
                methodEnum = HttpMethod.PATCH;
            } else if (method == 3) {
                methodEnum = HttpMethod.PUT;
            } else if (method == 4) {
                methodEnum = HttpMethod.DELETE;
            } else {
                LOG.error("不支持的请求方式");
                throw new BusinessException("不支持的请求方式");
            }

            // 确定请求bodyType
            long startTime = System.currentTimeMillis();
            if (bodyType == 0) { //form-data
                requestBody = JSON.toJSONString(formDataMap, SerializerFeature.PrettyFormat).equals("null") ? "" : JSON.toJSONString(formDataMap, SerializerFeature.PrettyFormat);
                responseEntity = Request.requestPro(methodEnum, url, headersMap, paramsMap, formDataMap, MediaType.MULTIPART_FORM_DATA);
            } else if (bodyType == 1) { //x-www-form-encoded
                requestBody = JSON.toJSONString(formDataEncodedMap, SerializerFeature.PrettyFormat).equals("null") ? "" : JSON.toJSONString(formDataEncodedMap, SerializerFeature.PrettyFormat);
                responseEntity = Request.requestPro(methodEnum, url, headersMap, paramsMap, formDataEncodedMap, MediaType.APPLICATION_FORM_URLENCODED);
            } else if (bodyType == 2) { //raw
                requestBody = raw;
                if ("Text".equalsIgnoreCase(rawType)) {
                    responseEntity = Request.requestPro(methodEnum, url, headersMap, paramsMap, raw, MediaType.TEXT_PLAIN);
                } else if ("JSON".equalsIgnoreCase(rawType)) {
                    responseEntity = Request.requestPro(methodEnum, url, headersMap, paramsMap, raw, MediaType.APPLICATION_JSON);
                } else if ("HTML".equalsIgnoreCase(rawType)) {
                    responseEntity = Request.requestPro(methodEnum, url, headersMap, paramsMap, raw, MediaType.TEXT_HTML);
                } else if ("XML".equalsIgnoreCase(rawType)) {
                    responseEntity = Request.requestPro(methodEnum, url, headersMap, paramsMap, raw, MediaType.APPLICATION_XML);
                } else {
                    throw new BusinessException("rawType参数错误");
                }
            } else if (bodyType == 9) { //none
                requestBody = null;
                responseEntity = Request.requestPro(methodEnum, url, headersMap, paramsMap, null);
            } else {
                throw new BusinessException("bodyType参数错误");
            }
            runTime = System.currentTimeMillis() - startTime;
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
            LOG.error("连接超时，请检查超时设置/代理服务器");
            exceptionMessage = "连接超时，请检查超时设置/代理服务器";
        } catch (Exception e) {
            // 出现异常则追加错误信息，并将case状态设置为2错误
            caseStatus = 2;
            e.printStackTrace();
            exceptionMessage = ExceptionUtil.msg(e);
        }

        int responseCode;
        String responseHeaders;
        String responseBody;

        if (responseEntity == null) { // responseEntity == null, 则请求失败，只保存执行日志
            // 3.保存日志
            InterfaceCaseExecuteLogDO executeLogDO = new InterfaceCaseExecuteLogDO();
            executeLogDO.setCaseId(interfaceCaseId);
            executeLogDO.setCaseDesc(desc);
            executeLogDO.setCaseMethod(method);
            executeLogDO.setRequestHeaders(JSON.toJSONString(headersMap, SerializerFeature.PrettyFormat).equals("null") ? "" : JSON.toJSONString(headersMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestParams(JSON.toJSONString(paramsMap, SerializerFeature.PrettyFormat).equals("null") ? "" : JSON.toJSONString(paramsMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestBody(requestBody);
            executeLogDO.setRawRequestHeaders(rawHeaders);
            executeLogDO.setRawRequestParams(rawParams);
            executeLogDO.setRawRequestBody(rawBody);
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
            executeLogDO.setSource(source);
            executeLogDO.setRawType(rawType);
            executeLogDO.setBodyType(bodyType);
            InterfaceCaseExecuteLogDO executedLogDO = executeLogService.saveExecuteLog(executeLogDO);
            // 返回自增id
            Integer executeLogId = executedLogDO.getId();

            InterfaceCaseExecuteLogDO updateChain = new InterfaceCaseExecuteLogDO();
            updateChain.setId(executeLogId);
            updateChain.setChain(JSON.toJSONString(redisUtil.stackGetAll(chainNo)));
            executeLogService.modifyExecuteLog(updateChain);

            LOG.warn("3.请求错误，仅保存执行，保存执行日志，日志内容={}，自增执行日志编号={}", executedLogDO, executeLogId);
            return executeLogId;
        } else { //请求成功记录执行日志和断言日志
            // 只有接口调用未出现异常才统计code、header、body
            responseCode = Request.code(responseEntity);
            responseHeaders = Request.headersPretty(responseEntity);
            responseBody = Request.body(responseEntity);
            String temp = responseBody;
            try {
                JSONObject responseBodyObject = JSONObject.parseObject(temp);
                responseBody = JSON.toJSONString(responseBodyObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
            } catch (Exception e) {
                responseBody = temp;
            }
            // 3.保存日志
            InterfaceCaseExecuteLogDO executeLogDO = new InterfaceCaseExecuteLogDO();
            executeLogDO.setCaseId(interfaceCaseId);
            executeLogDO.setCaseDesc(desc);
            executeLogDO.setCaseMethod(method);
            executeLogDO.setRequestHeaders(JSON.toJSONString(headersMap, SerializerFeature.PrettyFormat).equals("null") ? "" : JSON.toJSONString(headersMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestParams(JSON.toJSONString(paramsMap, SerializerFeature.PrettyFormat).equals("null") ? "" : JSON.toJSONString(paramsMap, SerializerFeature.PrettyFormat));
            executeLogDO.setRequestBody(requestBody);
            executeLogDO.setRawRequestHeaders(rawHeaders);
            executeLogDO.setRawRequestParams(rawParams);
            executeLogDO.setRawRequestBody(rawBody);
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
            executeLogDO.setSource(source);
            executeLogDO.setRawType(rawType);
            executeLogDO.setBodyType(bodyType);
            InterfaceCaseExecuteLogDO executedLogDO = executeLogService.saveExecuteLog(executeLogDO);
            // 4.保存断言日志表，获取运行日志自增id然后在断言日志表中写入断言信息，断言日志都成功后再将日志修改状态为0成功
            // 日志自增id
            int executedLogId = executedLogDO.getId();
            LOG.info("3.请求成功，保存执行日志，日志内容={}，自增执行日志编号={}", executedLogDO, executedLogId);


            // 断言执行前，执行从request提取类型
            // 即type=3request-header/4request-params/5request-data/6request-json的处理器
            // 但是response提取必须所有断言执行完成且用例状态为通过才提取
            if (1 == 1) { // 控制作用域 懒得再写。。。
                InterfaceProcessorDTO interfaceProcessorDTO = new InterfaceProcessorDTO();
                interfaceProcessorDTO.setCaseId(interfaceCaseId);
                List<InterfaceProcessorVO> processorList = interfaceProcessorService.findInterfaceProcessorList(interfaceProcessorDTO);
                InterfaceProcessorLogDO interfaceProcessorLogDO;
                String value = null;
                if (!processorList.isEmpty()) {
                    for(InterfaceProcessorVO postProcessorVO : processorList) {
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
                                        s = ParseUtil.parseJson(formDataEncoded, expression);
                                        break;
                                    case 6:
                                        s = ParseUtil.parseJson(raw, expression);
                                        break;
                                }
                            } catch (ParseException e) {
                                isThrowException = true;
                                exceptionMsg = e.getMessage();
                                errorMsg = e.getMessage();
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
                                    errorMsg = "用例执行失败，" + "处理器" + name + "提取结果为空，提取表达式为：" + expression + "，用例编号为：" + interfaceCaseId;
                                }
                            } else {
                                isDefaultValue = 1;
                                if (jsonPathArray.size() == 1) {
                                    Object o = jsonPathArray.get(0);
                                    value = o == null ? null : o.toString();
                                    //value = jsonPathArray.get(0).toString();
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
                                // 写入处理器记录表
                                interfaceProcessorLogDO.setIsDefaultValue(isDefaultValue);
                                interfaceProcessorLogDO.setValue(value);
                                interfaceProcessorLogDO.setCreatedTime(new Date());
                                interfaceProcessorLogDO.setStatus(status);
                                if (status == 0) {
                                    interfaceProcessorLogDO.setErrorMsg(null);
                                } else {
                                    interfaceProcessorLogDO.setErrorMsg(errorMsg);
                                }
                                // 写入处理器日志表
                                long start = TimeUtil.now();
                                interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
                                // type=3request-header/4request-params/5request-data/6request-json的处理器
                                RelyType relyType;
                                if (type == 3) {
                                    relyType = RelyType.WRITE_PROCESSOR_REQUEST_HEADER;
                                } else if (type == 4) {
                                    relyType = RelyType.WRITE_PROCESSOR_REQUEST_PARAMS;
                                } else if (type == 5) {
                                    relyType = RelyType.WRITE_PROCESSOR_REQUEST_DATA;
                                } else {
                                    relyType = RelyType.WRITE_PROCESSOR_REQUEST_JSON;
                                }
                                LOG.info("写入处理器日志表");
                                // 如果处理器出错，那么将用例执行状态重新置为错误
                                if (status == 1) {
                                    InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                                    afterPostProcessorLogDo.setId(executedLogId);
                                    afterPostProcessorLogDo.setStatus((byte) 2);
                                    afterPostProcessorLogDo.setErrorMessage(errorMsg);
                                    executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                                    LOG.info("处理器运行错误，主动将用例执行状态置为错误");
                                    throw new BusinessException("请求参数缓存出错，" + errorMsg);
                                } else {
                                    if (casePreNo != null) { //如果为前置用例，仅写入前置用例域，
                                        redisUtil.hashPut(casePreNo, name, value);
                                        LOG.info("写入处理器-前置用例域，key={}，hashKey={}，value={}", casePreNo, name, value);
                                    } else {
                                        if (suiteLogDetailNo == null) { // 不存在则写入临时域
                                            redisUtil.hashPut(NoUtil.TEMP_POST_PROCESSOR_NO, name, value);
                                            LOG.info("写入处理器-临时域，key={}，hashKey={}，value={}", NoUtil.TEMP_POST_PROCESSOR_NO, name, value);
                                        } else { // 否则写入测试套件域
                                            redisUtil.hashPut(suiteLogDetailNo, name, value, 24*60*60);
                                            LOG.info("写入处理器-测试套件域，key={}，hashKey={}，value={}", suiteLogDetailNo, name, value);
                                        }
                                    }
                                }
                                redisUtil.stackPush(chainNo, chainNode(relyType, null, name, value, TimeUtil.now()-start, expression));
                                // 手动写入，否则前置用例的跟踪链看不到
                                InterfaceCaseExecuteLogDO modifyChain = new InterfaceCaseExecuteLogDO();
                                modifyChain.setId(executedLogId);
                                modifyChain.setChain(JSON.toJSONString(redisUtil.stackGetAll(chainNo)));
                                executeLogService.modifyExecuteLog(modifyChain);
                            } else { // 若解析json、xpath出错
                                // 写入处理器记录表
                                interfaceProcessorLogDO.setIsDefaultValue(null);
                                interfaceProcessorLogDO.setValue(null);
                                interfaceProcessorLogDO.setCreatedTime(new Date());
                                interfaceProcessorLogDO.setStatus((byte) 1);
                                interfaceProcessorLogDO.setErrorMsg(exceptionMsg);
                                interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
                                LOG.warn("处理器解析出错，写入处理器记录表");
                                // 重置用例状态
                                InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                                afterPostProcessorLogDo.setId(executedLogId);
                                afterPostProcessorLogDo.setStatus((byte) 2);
                                afterPostProcessorLogDo.setErrorMessage(exceptionMsg);
                                executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                                LOG.info("处理器解析出错，主动将用例执行状态置为错误");
                                throw new BusinessException("请求参数缓存出错，" + errorMsg);
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
                byte assertStatus;
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
                        exceptedResult = parser.parseDependency(exceptedResult, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
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
                boolean isPass;
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
                        actualResult = ParseUtil.parseJson(Request.headers(responseEntity), expression);
                        LOG.info("断言实际结果={}, 类型={}, 响应header={}, 提取表达式={}", actualResult, "header", Request.headers(responseEntity), expression);
                        resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                    } else if (type == 3) { // responseCode
                        actualResult = String.valueOf(ParseUtil.parseHttpCode(responseEntity));
                        LOG.info("断言实际结果={}, 类型={}, 响应code={}, 提取表达式={}", actualResult, "code", ParseUtil.parseHttpCode(responseEntity), expression);
                    } else if (type == 4) { // runtime
                        actualResult = String.valueOf(runTime);
                        LOG.info("断言实际结果={}, 类型={}, 执行耗时={}, 提取表达式={}", actualResult, "runtime", actualResult, expression);
                    } else {
                        throw new BusinessException("不支持的断言方式");
                    }
                    LOG.info("预期结果={}", exceptedResult);
                    LOG.info("操作符={}", operator);
                    LOG.info("实际结果={}", actualResult);
                    if (type < 3) { // 0json/1html/2header/3responseCode/4runtime
                        if (resultList.size() == 1) {
                            Object o = resultList.get(0);
                            actualResult = o == null ? null : o.toString();
                            if (type == 2) {
                                JSONArray var1 = JSON.parseArray(actualResult);
                                if (var1.size() == 1) {
                                    Object var2 = var1.get(0);
                                    actualResult = var2 == null ? null : var2.toString();
                                } else {
                                    actualResult = JSON.toJSONString(var1);
                                }
                            }
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
            updateChain.setChain(JSON.toJSONString(redisUtil.stackGetAll(chainNo)));
            executeLogService.modifyExecuteLog(updateChain);


            // *仅用例执行通过才写入处理器记录
            if (updateStatus.getStatus() == 0 ) {
                InterfaceProcessorDTO interfaceProcessorDTO = new InterfaceProcessorDTO();
                interfaceProcessorDTO.setCaseId(interfaceCaseId);
                List<InterfaceProcessorVO> processorList = interfaceProcessorService.findInterfaceProcessorList(interfaceProcessorDTO);
                InterfaceProcessorLogDO interfaceProcessorLogDO;
                String value = null;
                if (!processorList.isEmpty()) {
                    for(InterfaceProcessorVO postProcessorVO : processorList) {
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
                                    errorMsg = "响应数据缓存出错，" + "用例执行失败，" + "处理器" + name + "提取结果为空，提取表达式为：" + expression + "，用例编号为：" + interfaceCaseId;
                                }
                            } else {
                                isDefaultValue = 1;
                                if (jsonPathArray.size() == 1) {
                                    Object o = jsonPathArray.get(0);
                                    value = o == null ? null : o.toString();
                                    //value = jsonPathArray.get(0).toString();
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
                                    errorMsg = "响应数据缓存出错，" + "用例执行失败，" + "处理器" + name + "提取结果为空，提取表达式为：" + expression + "，用例编号为：" + interfaceCaseId;
                                }
                            } else {
                                isDefaultValue = 1;
                                if (xpathArray.size() == 1) {
                                    Object o = xpathArray.get(0);
                                    value = o == null ? null : o.toString();
                                    //value = xpathArray.get(0).toString();
                                } else {
                                    value = JSON.toJSONString(xpathArray);
                                }
                                LOG.info("提取类型={}，提取表达式={}，写入缓存内容={}", "response-xml", expression, value);
                            }
                        } else if (type == 2) { //header
                            String s = "[]";
                            try {
                                s = ParseUtil.parseJson(responseHeaders, expression);
                            } catch (ParseException e) {
                                isThrowException = true;
                                exceptionMsg = e.getMessage();
                            }
                            ArrayList headerArray = JSONObject.parseObject(s, ArrayList.class);
                            if (headerArray == null || headerArray.isEmpty()) {
                                if (haveDefaultValue == 0) {
                                    isDefaultValue = 0;
                                    value = defaultValue;
                                    LOG.info("提取类型={}，提取表达式={} 为空，使用默认值={}", "header", expression, value);
                                } else {
                                    status = 1;
                                    LOG.warn("header提取内容为空，header={}", expression);
                                    errorMsg = "响应数据缓存出错，" + "用例执行失败，" + "处理器" + name + "提取结果为空，提取表达式为：" + expression + "，用例编号为：" + interfaceCaseId;
                                }
                            } else {
                                isDefaultValue = 1;
                                if (headerArray.size() == 1) {
                                    Object o = headerArray.get(0);
                                    value = o == null ? null : o.toString();
                                    JSONArray var1 = JSON.parseArray(value);
                                    if (var1.size() == 1) {
                                        Object var2 = var1.get(0);
                                        value = var2 == null ? null : var2.toString();
                                    } else {
                                        value = JSON.toJSONString(var1);
                                    }
                                } else {
                                    value = JSON.toJSONString(headerArray);
                                }
                            }
                        } else if (type >= 7) {
                            status = 1;
                            LOG.error("处理器提取类型错误");
                            errorMsg = "处理器提取类型错误";
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
                                // 写入处理器记录表
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
                                // 写入处理器日志表
                                long start = TimeUtil.now();
                                interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
                                // 0response-json/1response-html/2response-header
                                RelyType relyType;
                                if (type == 0) {
                                    relyType = RelyType.WRITE_PROCESSOR_RESPONSE_JSON;
                                } else if (type == 1) {
                                    relyType = RelyType.WRITE_PROCESSOR_RESPONSE_HTML;
                                } else {
                                    relyType = RelyType.WRITE_PROCESSOR_RESPONSE_HEADER;
                                }
                                LOG.info("写入处理器日志表");
                                // 如果处理器出错，那么将用例执行状态重新置为错误
                                if (status == 1) {
                                    InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                                    afterPostProcessorLogDo.setId(executedLogId);
                                    afterPostProcessorLogDo.setStatus((byte) 2);
                                    afterPostProcessorLogDo.setErrorMessage(errorMsg);
                                    executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                                    LOG.info("处理器运行错误，主动将用例执行状态置为错误");
                                } else {
                                    if (casePreNo != null) { //如果为前置用例，仅写入前置用例域，
                                        redisUtil.hashPut(casePreNo, name, value);
                                        LOG.info("写入处理器-前置用例域，key={}，hashKey={}，value={}", casePreNo, name, value);
                                    } else {
                                        if (suiteLogDetailNo == null) { // 不存在则写入临时域
                                            redisUtil.hashPut(NoUtil.TEMP_POST_PROCESSOR_NO, name, value);
                                            LOG.info("写入处理器-临时域，key={}，hashKey={}，value={}", NoUtil.TEMP_POST_PROCESSOR_NO, name, value);
                                        } else { // 否则写入测试套件域
                                            redisUtil.hashPut(suiteLogDetailNo, name, value, 24*60*60);
                                            LOG.info("写入处理器-测试套件域，key={}，hashKey={}，value={}", suiteLogDetailNo, name, value);
                                        }
                                    }
                                }
                                redisUtil.stackPush(chainNo, chainNode(relyType, null, name, value, TimeUtil.now()-start, expression));
                                // 手动写入，否则前置用例的跟踪链看不到
                                InterfaceCaseExecuteLogDO modifyChain = new InterfaceCaseExecuteLogDO();
                                modifyChain.setId(executedLogId);
                                modifyChain.setChain(JSON.toJSONString(redisUtil.stackGetAll(chainNo)));
                                executeLogService.modifyExecuteLog(modifyChain);
                            } else { // 若解析json、xpath出错
                                // 写入处理器记录表
                                interfaceProcessorLogDO.setIsDefaultValue(null);
                                interfaceProcessorLogDO.setValue(null);
                                interfaceProcessorLogDO.setCreatedTime(new Date());
                                interfaceProcessorLogDO.setStatus((byte) 1);
                                interfaceProcessorLogDO.setErrorMsg(exceptionMsg);
                                interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
                                LOG.warn("处理器解析出错，写入处理器记录表");
                                // 重置用例状态
                                InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                                afterPostProcessorLogDo.setId(executedLogId);
                                afterPostProcessorLogDo.setStatus((byte) 2);
                                afterPostProcessorLogDo.setErrorMessage(exceptionMsg);
                                executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                                LOG.info("处理器解析出错，主动将用例执行状态置为错误");
                            }
                        }
                    }
                }
            }
            // 手动写入，否则前置用例的跟踪链看不到
            redisUtil.stackPush(chainNo, chainNode(endRelyType, executedLogId, interfaceCaseInfoVO.getDesc(), null, TimeUtil.now()-methodStart, null));
            InterfaceCaseExecuteLogDO modifyChain = new InterfaceCaseExecuteLogDO();
            modifyChain.setId(executedLogId);
            modifyChain.setChain(JSON.toJSONString(redisUtil.stackGetAll(chainNo)));
            executeLogService.modifyExecuteLog(modifyChain);
            return executedLogId;
        }
    }

    /**
     * 将前端包含name、value、checked、value的对象数组转成对象
     * @param text header、param、form、formEncoded
     * @return 转换后的
     */
    private String kvCast(String text) throws BusinessException {
        try {
            JSONObject object = new JSONObject();
            JSONArray array = JSON.parseArray(text);
            if (array != null && !array.isEmpty()) {
                array.forEach(item -> {
                    String s = JSON.toJSONString(item);
                    JSONObject var1 = JSON.parseObject(s);
                    String name = var1.getString("name");
                    String value = var1.getString("value");
                    boolean checked = var1.getBooleanValue("checked");
                    if (checked) {
                        object.put(name, value);
                    }
                });
                return JSON.toJSONString(object);
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException("headers/params/form-data/form-data-encoded格式错误");
        }
    }
}
