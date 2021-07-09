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
        Boolean skipPreCase = executeInterfaceCaseParam.getSkipPreCase();

        // 获取case详情
        InterfaceCaseInfoVO interfaceCaseInfoVO = interfaceCaseService.findInterfaceCaseByCaseId(interfaceCaseId);
        if (interfaceCaseInfoVO == null) {
            throw new BusinessException("用例信息不存在");
        }
        String desc = interfaceCaseInfoVO.getDesc();
        Integer projectId = interfaceCaseInfoVO.getProjectId();

        String exceptionMessage = null; // 接收异常信息
        byte caseStatus = 0; // 运行结果 0成功 1失败 2错误
        String url;
        String requestBody = null; // 记录请求日志requestBody字段

        // source 来源（0用例调试 1依赖调试 2运行整个测试套件 3测试套件单个用例调试 4依赖解析 5综合用例-前置用例）
        RelyType startRelyType;
        RelyType endRelyType;
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

        LOG.info("--------------------------------case [{}] start execution--------------------------------", interfaceCaseId);

        redisUtil.stackPush(chainNo, chainNode(startRelyType, null, desc, null, TimeUtil.now()-methodStart, null));

        if (projectId != null) {
            // 判定运行环境
            ProjectVO projectVO = projectService.findProjectById(projectId);
            if (suiteId == null) { // 无suiteId，使用调试环境
                url = env.domain(projectVO, (byte)4) + interfaceCaseInfoVO.getUrl();
            } else { // 根据suiteId获取运行环境
                InterfaceCaseSuiteVO suiteVO = ifSuiteService.findInterfaceCaseSuiteById(suiteId);
                url = env.domain(projectVO, suiteVO.getRunDev()) + interfaceCaseInfoVO.getUrl();
            }
        } else {
            // 如果没有projectId，则不使用host port protocol 拼接
            url = interfaceCaseInfoVO.getUrl();
        }

        // 过滤未启用的属性
        String headers = kvCast(interfaceCaseInfoVO.getHeaders());
        String params = kvCast(interfaceCaseInfoVO.getParams());
        String formData = kvCast(interfaceCaseInfoVO.getFormData());
        String formDataEncoded = kvCast(interfaceCaseInfoVO.getFormDataEncoded());

        String raw = interfaceCaseInfoVO.getRaw();
        String rawType = interfaceCaseInfoVO.getRawType();
        Byte bodyType = interfaceCaseInfoVO.getBodyType();
        Byte method = interfaceCaseInfoVO.getMethod();

        String rawHeaders = headers;
        String rawParams = params;
        // 确定日志记录最终记录的rawBody
        String rawBody = this.logRawBody(bodyType, formData, formDataEncoded, raw);
        long runTime = 0;


        // 执行前置用例
        if (!skipPreCase) {
            List<InterfacePreCaseDO> preCaseList = interfacePreCaseService.findInterfacePreCaseByParentId(interfaceCaseId);
            if (!preCaseList.isEmpty()) {
                String no = NoUtil.genCasePreNo();
                casePreNo = no; // 重置casePreNo
                for(InterfacePreCaseDO preCase : preCaseList) {
                    Integer caseId = preCase.getPreCaseId();
                    LOG.info("pre-case [{}] start execution....", caseId);
                    executeInterfaceCaseParam.setSource((byte)5);
                    executeInterfaceCaseParam.setCasePreNo(no);
                    executeInterfaceCaseParam.setExecutor("前置用例");
                    executeInterfaceCaseParam.setSuiteLogNo(null); // 把前置用例作为中间依赖case处理
                    executeInterfaceCaseParam.setInterfaceCaseId(caseId);
                    executeInterfaceCaseParam.setSkipPreCase(true); // 当用例作为前置用例执行时，应该跳过前置用例的前置用例，仅执行自身
                    Integer logId = this.executeInterfaceCase(executeInterfaceCaseParam);
                    // 查询用例执行状态
                    InterfaceCaseExecuteLogVO log = executeLogService.findExecute(logId);
                    String caseDesc = log.getCaseDesc();
                    Byte status = log.getStatus();
                    if (status != 0) {
                        if (status == 1) {
                            exceptionMessage = String.format("pre-case [%s] [%s] execution failed, " +
                                    "other pre-case will stop running", caseId,  caseDesc);
                        } else {
                            exceptionMessage = String.format("pre-case [%s] [%s] execution error, errorMsg [%s], " +
                                    "other pre-case will stop running", caseId, caseDesc, log.getErrorMessage());
                        }
                        caseStatus = 2;
                        LOG.error(exceptionMessage);
                        break; // 终止循环，其它前置用例不再执行
                    }
                    LOG.info("pre-case [{}] at the end of execution", caseId);
                }
            }
        }

        // 当前置用例执行不成功, 记录日志并停止后续流程
        if (caseStatus == 2) {
            return this.onRequestError(interfaceCaseId, url, desc, method, null, null, null,
                    rawHeaders, rawParams, rawBody, executor, exceptionMessage, runTime,
                    suiteLogNo, suiteLogDetailNo, isFailedRetry, source, rawType, bodyType, chainNo);
        }

        // 发送请求
        ResponseEntity responseEntity = null;
        HashMap headersMap = null;
        HashMap paramsMap = null;
        HashMap formDataMap;
        HashMap formDataEncodedMap;

        try {
            // 解析headers中的依赖
            headers = parser.parseDependency(headers, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
            // 解析params中的依赖
            params = parser.parseDependency(params, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
            if (bodyType == 0) { //form-data
                formData = parser.parseDependency(formData, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
            } else if (bodyType == 1) { //x-www-form-encoded
                formDataEncoded = parser.parseDependency(formDataEncoded, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
            } else if (bodyType == 2) { //raw
                raw = parser.parseDependency(raw, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
            } else if (bodyType == 9) { //none
                LOG.info("bodyType is none, not parse the dependency");
            } else {
                throw new BusinessException("not supported the bodyType");
            }

            // 合并公共headers
            headersMap = this.mergeSuiteProperty(globalHeaders, headers);
            // 合并公共params
            paramsMap = this.mergeSuiteProperty(globalParams, params);
            // 合并公共formDataEncoded
            formDataEncodedMap = this.mergeSuiteProperty(globalData, formDataEncoded);
            // formData 不合并
            formDataMap = JSONObject.parseObject(formData, HashMap.class);

            // 处理URL参数
            HashMap<String, Object> urlParamsWrapper = Request.pathVariableParser(url, paramsMap);
            url = (String) urlParamsWrapper.get("url");
            paramsMap = (HashMap<String, String>) urlParamsWrapper.get("params");

            // 确定日志记录最终记录的requestBody
            requestBody = this.logRequestBody(bodyType, formDataMap, formDataEncodedMap, raw);
            // 确定请求方式
            HttpMethod methodEnum = this.httpMethod(method);

            // 发送请求
            long startTime = System.currentTimeMillis();
            responseEntity = this.doRequest(bodyType, methodEnum, url, headersMap, paramsMap, formDataMap, formDataEncodedMap, raw, rawType);
            runTime = System.currentTimeMillis() - startTime;
        } catch (ResourceAccessException e) {
            caseStatus = 2;
            e.printStackTrace();
            LOG.error("connection timed out, try to check timeout setting and proxy server");
            exceptionMessage = "connection timed out, try to check timeout setting and proxy server";
        } catch (Exception e) {
            caseStatus = 2;
            e.printStackTrace();
            LOG.error(ExceptionUtil.msg(e));
            exceptionMessage = e.getMessage();
        }

        int responseCode;
        String responseHeaders;
        String responseBody;

        if (responseEntity == null) { // 请求出错
            return this.onRequestError(interfaceCaseId, url, desc, method, headersMap, paramsMap, requestBody,
                    rawHeaders, rawParams, rawBody, executor, exceptionMessage, runTime,
                    suiteLogNo, suiteLogDetailNo, isFailedRetry, source, rawType, bodyType, chainNo);
        } else { // 请求成功

            responseCode = Request.code(responseEntity);
            responseHeaders = Request.headersPretty(responseEntity);
            responseBody = Request.body(responseEntity);

            // 记录请求日志
            int executedLogId = this.onRequestSuccess(responseCode, responseHeaders, responseBody,
                    interfaceCaseId, url, desc, method, headersMap, paramsMap, requestBody,
                    rawHeaders, rawParams, rawBody, runTime, executor, suiteLogNo, suiteLogDetailNo,
                    isFailedRetry, source, rawType, bodyType, caseStatus);

            // 请求参数缓存
            this.cacheRequest(interfaceCaseInfoVO.getPostProcessors(), executedLogId, headers, params, formDataEncoded,
                    raw, casePreNo, suiteLogDetailNo, chainNo);

            // 执行断言
            List<InterfaceAssertVO> asserts = interfaceCaseInfoVO.getAsserts();
            Byte statusAfterAssert = this.doAssert(asserts, interfaceCaseId, executedLogId,
                    responseCode, responseHeaders, responseBody, runTime,
                    chainNo, suiteId, isFailedRetry, suiteLogDetailNo, casePreNo,
                    globalHeaders, globalParams, globalData);

            // 响应数据缓存
            this.cacheResponse(interfaceCaseInfoVO.getPostProcessors(), interfaceCaseId, executedLogId,
                    statusAfterAssert, responseHeaders, responseBody,
                    casePreNo, suiteLogDetailNo, chainNo);

            // 手动写入，否则前置用例的跟踪链看不到
            redisUtil.stackPush(chainNo, chainNode(endRelyType, executedLogId, interfaceCaseInfoVO.getDesc(), null, TimeUtil.now()-methodStart, null));
            InterfaceCaseExecuteLogDO modifyChain = new InterfaceCaseExecuteLogDO();
            modifyChain.setId(executedLogId);
            modifyChain.setChain(JSON.toJSONString(redisUtil.stackGetAll(chainNo)));
            executeLogService.modifyExecuteLog(modifyChain);

            // 返回日志编号
            return executedLogId;
        }
    }

    /**
     * 将前端包含name、value、checked、value的对象数组转成对象
     * @param text header、param、form、formEncoded
     * @return 转换后的
     */
    public String kvCast(String text) throws BusinessException {
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
            throw new BusinessException("headers/params/form-data/form-data-encoded syntax error");
        }
    }

    /**
     * 将数据库中的method字段映射HttpMethod枚举
     * @param methodByte 数据库中的method字段
     * @return HttpMethod枚举
     * @throws BusinessException BusinessException
     */
    public HttpMethod httpMethod(Byte methodByte) throws BusinessException {
        if (methodByte == 0) {
            return HttpMethod.GET;
        } else if (methodByte == 1) {
            return HttpMethod.POST;
        } else if (methodByte == 2) {
            return HttpMethod.PATCH;
        } else if (methodByte == 3) {
            return HttpMethod.PUT;
        } else if (methodByte == 4) {
            return HttpMethod.DELETE;
        } else if (methodByte == 5) {
            return HttpMethod.HEAD;
        } else if (methodByte == 6) {
            return HttpMethod.OPTIONS;
        } else if (methodByte == 7) {
            return HttpMethod.TRACE;
        } else {
            LOG.error("not supported the http method");
            throw new BusinessException("not supported the http method");
        }
    }

    /**
     * 根据bodyType，确定执行日志中的requestBody
     * @param bodyType bodyType
     * @return 执行日志中记录的requestBody
     */
    private String logRequestBody(Byte bodyType, HashMap formDataMap, HashMap formDataEncodedMap, String raw) throws BusinessException {
        if (bodyType == 0) { //form-data
            return JSON.toJSONString(formDataMap, SerializerFeature.PrettyFormat).equals("null") ? "" : JSON.toJSONString(formDataMap, SerializerFeature.PrettyFormat);
        } else if (bodyType == 1) { //x-www-form-encoded
            return JSON.toJSONString(formDataEncodedMap, SerializerFeature.PrettyFormat).equals("null") ? "" : JSON.toJSONString(formDataEncodedMap, SerializerFeature.PrettyFormat);
        } else if (bodyType == 2) { //raw
            return raw;
        } else if (bodyType == 9) { //none
            return null;
        } else {
            throw new BusinessException("not supported the bodyType");
        }
    }

    /**
     * 根据bodyType，确定执行日志中的rawBody
     * @param bodyType bodyType
     * @return 执行日志中记录的rawBody
     */
    private String logRawBody(Byte bodyType, String formData, String formDataEncoded, String raw) throws BusinessException {
        if (bodyType == 0) { //form-data
            return formData;
        } else if (bodyType == 1) { //x-www-form-encoded
            return formDataEncoded;
        } else if (bodyType == 2) { //raw
            return raw;
        } else if (bodyType == 9) { //none
            return null;
        } else {
            throw new BusinessException("not supported the bodyType");
        }
    }

    /**
     * 发送http请求
     * @param bodyType bodyType
     * @param methodEnum 请求方式枚举
     * @param url 请求url
     * @param headersMap headersMap
     * @param paramsMap paramsMap
     * @param formDataMap formDataMap
     * @param formDataEncodedMap formDataEncodedMap
     * @param raw raw
     * @param rawType raw类型
     * @return ResponseEntity
     * @throws BusinessException BusinessException
     */
    public ResponseEntity doRequest(Byte bodyType, HttpMethod methodEnum,
                                     String url, HashMap headersMap, HashMap paramsMap,
                                     HashMap formDataMap, HashMap formDataEncodedMap,
                                     String raw, String rawType) throws BusinessException {
        if (bodyType == 0) { //form-data
            return Request.requestPro(methodEnum, url, headersMap, paramsMap, formDataMap, MediaType.MULTIPART_FORM_DATA);
        } else if (bodyType == 1) { //x-www-form-encoded
            return Request.requestPro(methodEnum, url, headersMap, paramsMap, formDataEncodedMap, MediaType.APPLICATION_FORM_URLENCODED);
        } else if (bodyType == 2) { //raw
            if ("Text".equalsIgnoreCase(rawType)) {
                return Request.requestPro(methodEnum, url, headersMap, paramsMap, raw, MediaType.TEXT_PLAIN);
            } else if ("JSON".equalsIgnoreCase(rawType)) {
                return Request.requestPro(methodEnum, url, headersMap, paramsMap, raw, MediaType.APPLICATION_JSON);
            } else if ("HTML".equalsIgnoreCase(rawType)) {
                return Request.requestPro(methodEnum, url, headersMap, paramsMap, raw, MediaType.TEXT_HTML);
            } else if ("XML".equalsIgnoreCase(rawType)) {
                return Request.requestPro(methodEnum, url, headersMap, paramsMap, raw, MediaType.APPLICATION_XML);
            } else {
                throw new BusinessException("not supported the rawType");
            }
        } else if (bodyType == 9) { //none
            return Request.requestPro(methodEnum, url, headersMap, paramsMap, null);
        } else {
            throw new BusinessException("not supported the bodyType");
        }
    }

    /**
     * 执行用例的全部断言
     * @param asserts 断言列表
     * @param interfaceCaseId 测试用例编号
     * @param executedLogId 此次执行日志编号
     * @param responseCode 响应状态码
     * @param responseHeaders 响应头
     * @param responseBody 响应体
     * @param runTime 执行耗时
     * @param chainNo 调用链路跟踪 将调用链信息序列化
     * @param suiteId 测试套件编号
     * @param isFailedRetry 记录此次是否为失败重跑
     * @param suiteLogDetailNo suiteLogNo仅记录重跑和真正运行的，suiteLogDetailNo会包括用例所依赖的case
     * @param casePreNo 前置用例的参数缓存用的key，为了防止异步执行用例时，tempPostProcessor key被覆盖， 仅前置用例执行时，需要该参数
     * @param globalHeaders 测试套件的headers
     * @param globalParams 测试套件的params
     * @param globalData 测试套件的data
     * @return 根据断言列表中的每次断言结果判定最终断言结果并返回
     */
    private Byte doAssert(List<InterfaceAssertVO> asserts, Integer interfaceCaseId, Integer executedLogId,
                          Integer responseCode, String responseHeaders, String responseBody, Long runTime,
                          String chainNo, Integer suiteId, Byte isFailedRetry, String suiteLogDetailNo, String casePreNo,
                          HashMap globalHeaders, HashMap globalParams, HashMap globalData) {

        List<Byte> statusList = new ArrayList<>(); // 用来接收用例每个断言的结果

        for (InterfaceAssertVO interfaceAssertVO : asserts) {
            byte assertStatus; // 是否通过 0通过 1失败 2错误
            String assertErrorMessage = null;

            Integer assertId = interfaceAssertVO.getAssertId();
            String assertName = interfaceAssertVO.getAssertName();
            Byte type = interfaceAssertVO.getType();
            String expression = interfaceAssertVO.getExpression();
            Byte operator = interfaceAssertVO.getOperator();
            Integer order = interfaceAssertVO.getOrder();

            // 断言基本信息
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

            // 当断言出错
            if (null != exceptedResult) {
                try {
                    LOG.info("start parsing assertion expected results");
                    exceptedResult = parser.parseDependency(exceptedResult, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                    LOG.info("complete the analysis of the expected results, expected results [{}]", exceptedResult);
                } catch (ParseException | BusinessException | SqlException e) {
                    assertErrorMessage = e.getMessage();
                    assertStatus = 2;
                    statusList.add(assertStatus); // 将每次断言status都加入集合
                    assertLogDO.setExceptedResult(exceptedResult);
                    assertLogDO.setOrder(order);
                    assertLogDO.setActualResult(null);
                    assertLogDO.setStatus(assertStatus);
                    assertLogDO.setErrorMessage(assertErrorMessage);
                    assertLogDO.setCreatedTime(new Date());
                    LOG.error("error in parsing assert's expected results process");
                    assertLogService.saveInterfaceAssertLog(assertLogDO);
                    continue;
                }
            }

            // 执行断言
            assertLogDO.setExceptedResult(exceptedResult);
            assertLogDO.setOrder(order);
            String actualResult = null;
            ArrayList resultList = null;
            boolean isPass;
            try {
                if (type == 0) { // json
                    actualResult = ParseUtil.parseJson(responseBody, expression);
                    LOG.info("actual results={}, assert type={}, body={}, expression={}", actualResult, "json-path", responseBody, expression);
                    resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                } else if (type == 1) { // html
                    actualResult = ParseUtil.parseXml(responseBody, expression);
                    LOG.info("actual results={}, assert type={}, body={}, expression={}", actualResult, "xpath", responseBody, expression);
                    resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                } else if (type == 2) { // header
                    actualResult = ParseUtil.parseJson(responseHeaders, expression);
                    LOG.info("actual results={}, assert type={}, header={}, expression={}", actualResult, "json-path", responseHeaders, expression);
                    resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                } else if (type == 3) { // responseCode
                    actualResult = String.valueOf(responseCode);
                    LOG.info("actual results={}, assert type={}, code={}, expression={}", actualResult, "code", responseCode, expression);
                } else if (type == 4) { // runtime
                    actualResult = String.valueOf(runTime);
                    LOG.info("actual results={}, assert type={}, runtime={}, expression={}", actualResult, "runtime", actualResult, expression);
                } else {
                    throw new BusinessException("not supported assert type");
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
                            if (var1 == null) {
                                actualResult = null;
                            } else {
                                if (var1.size() == 1) {
                                    Object var2 = var1.get(0);
                                    actualResult = var2 == null ? null : var2.toString();
                                } else {
                                    actualResult = JSON.toJSONString(var1);
                                }
                            }
                        }
                    } else {
                        actualResult = JSON.toJSONString(resultList);
                    }
                }
                isPass = AssertUtil.asserts(actualResult, operator, exceptedResult);
                if (isPass) {
                    LOG.info("assertion success");
                    assertStatus = 0;
                } else {
                    LOG.warn("assertion failure");
                    assertStatus = 1;
                }
            } catch (Exception e) {
                assertStatus = 2;
                LOG.error("assertion failure, errorMsg={}", ExceptionUtil.msg(e));
                assertErrorMessage = e.getMessage();
            }
            assertLogDO.setActualResult(actualResult);
            assertLogDO.setStatus(assertStatus);
            statusList.add(assertStatus); // 将每次断言status都加入集合
            assertLogDO.setErrorMessage(assertErrorMessage);
            assertLogDO.setCreatedTime(new Date());
            assertLogService.saveInterfaceAssertLog(assertLogDO);
        }

        // 根据断言结果调整用例的执行结果
        InterfaceCaseExecuteLogDO updateStatus = new InterfaceCaseExecuteLogDO();
        updateStatus.setId(executedLogId);
        if (statusList.contains((byte) 2)) {
            updateStatus.setStatus((byte) 2);
        } else {
            if (!statusList.contains((byte) 1)) {
                updateStatus.setStatus((byte) 0);
            } else {
                updateStatus.setStatus((byte) 1);
            }
        }
        executeLogService.modifyExecuteLog(updateStatus);

        // 修改调用链
        InterfaceCaseExecuteLogDO updateChain = new InterfaceCaseExecuteLogDO();
        updateChain.setId(executedLogId);
        updateChain.setChain(JSON.toJSONString(redisUtil.stackGetAll(chainNo)));
        executeLogService.modifyExecuteLog(updateChain);

        // 返回用例执行断言后的状态
        return updateStatus.getStatus();
    }

    /**
     * 将测试套件中的header、params、form-data-encoded合并到请求信息中
     * @param suiteProperty 测试套件中的属性
     * @param kv 测试用例的属性
     * @return 合并后的属性
     */
    private HashMap mergeSuiteProperty(HashMap suiteProperty, String kv) {
        HashMap property = JSONObject.parseObject(kv, HashMap.class);
        if (suiteProperty != null) {
            if (property != null) {
                suiteProperty.putAll(property);
            }
            return suiteProperty;
        } else {
            return property;
        }
    }

    /**
     * 用例请求失败时触发（此时还未执行断言）
     * @param interfaceCaseId 用例编号
     * @param url 完整的url
     * @param desc 用例描述
     * @param method 请求方式
     * @param headersMap 请求headers
     * @param paramsMap 请求params
     * @param requestBody 请求body
     * @param rawHeaders 未解析依赖前的headers
     * @param rawParams 未解析依赖前的params
     * @param rawBody 未解析依赖前的body
     * @param executor 执行人
     * @param exceptionMessage 异常信息
     * @param runTime 执行耗时
     * @param suiteLogNo 测试套件日志编号 主要为调用入口为测试套件时使用，否则传参null
     * @param suiteLogDetailNo suiteLogNo仅记录重跑和真正运行的，suiteLogDetailNo会包括用例所依赖的case
     * @param isFailedRetry 是否为失败重跑的请求 0是1否
     * @param source 来源（0用例调试 1依赖调试 2运行整个测试套件 3测试套件单个用例调试 4依赖解析 5综合用例-前置用例）
     * @param rawType raw类型 "Text" "JSON" "HTML" "XML"
     * @param bodyType 请求体类型0form-data 1x-www-form-Encoded 2raw 9none
     * @param chainNo 调用链路跟踪 将调用链信息序列化
     * @return 日志编号
     */
    private Integer onRequestError(Integer interfaceCaseId, String url, String desc, Byte method,
                                   HashMap headersMap, HashMap paramsMap, String requestBody,
                                   String rawHeaders, String rawParams, String rawBody,
                                   String executor, String exceptionMessage, Long runTime,
                                   String suiteLogNo, String suiteLogDetailNo, Byte isFailedRetry,
                                   Byte source, String rawType, Byte bodyType, String chainNo) {

        // 写入日志表
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
        // 将调用链信息序列化保存
        Integer executeLogId = executedLogDO.getId();
        InterfaceCaseExecuteLogDO updateChain = new InterfaceCaseExecuteLogDO();
        updateChain.setId(executeLogId);
        updateChain.setChain(JSON.toJSONString(redisUtil.stackGetAll(chainNo)));
        executeLogService.modifyExecuteLog(updateChain);
        LOG.error("case [{}] execution error, executeLog={}, executeLogId={}", interfaceCaseId, executedLogDO, executeLogId);
        return executeLogId;
    }

    /**
     * 用例请求成功时触发（此时还未执行断言）
     * @param responseCode 响应状态码
     * @param responseHeaders 响应头
     * @param responseBody 响应体
     * @param interfaceCaseId 接口测试用例编号
     * @param url 请求URL
     * @param desc 描述
     * @param method 请求方式
     * @param headersMap 请求headers
     * @param paramsMap 请求params
     * @param requestBody 请求body
     * @param rawHeaders 未解析依赖前的headers
     * @param rawParams 未解析依赖前的params
     * @param rawBody 未解析依赖前的body
     * @param runTime 执行耗时
     * @param executor 执行人
     * @param suiteLogNo 测试套件日志编号 主要为调用入口为测试套件时使用，否则传参null
     * @param suiteLogDetailNo suiteLogNo仅记录重跑和真正运行的，suiteLogDetailNo会包括用例所依赖的case
     * @param isFailedRetry 是否为失败重跑的请求 0是1否
     * @param source 来源（0用例调试 1依赖调试 2运行整个测试套件 3测试套件单个用例调试 4依赖解析 5综合用例-前置用例）
     * @param rawType raw类型 "Text" "JSON" "HTML" "XML"
     * @param bodyType 请求体类型0form-data 1x-www-form-Encoded 2raw 9none
     * @param caseStatus 日志执行状态
     * @return 日志编号
     */
    private Integer onRequestSuccess(Integer responseCode, String responseHeaders, String responseBody,
                                     Integer interfaceCaseId, String url, String desc, Byte method,
                                     HashMap headersMap, HashMap paramsMap, String requestBody,
                                     String rawHeaders, String rawParams, String rawBody,
                                     Long runTime, String executor, String suiteLogNo, String suiteLogDetailNo,
                                     Byte isFailedRetry, Byte source, String rawType, Byte bodyType, Byte caseStatus) {
        String temp = responseBody;
        if (!temp.equals("")) {
            try {
                JSONObject responseBodyObject = JSONObject.parseObject(temp);
                responseBody = JSON.toJSONString(responseBodyObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
            } catch (Exception e) {
                responseBody = temp;
            }
        }
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
        Integer logId = executedLogDO.getId();
        LOG.info("case [{}] execution success, executeLog={}, executeLogId={}", interfaceCaseId, executedLogDO, logId);
        return logId;
    }

    /**
     * 请求参数缓存
     * @param processorList 处理器列表
     * @param executedLogId 执行日志编号
     * @param headers 请求headers
     * @param params 请求params
     * @param formDataEncoded 请求formDataEncoded
     * @param raw 请求raw
     * @param casePreNo 前置用例的参数缓存用的key，为了防止异步执行用例时，tempPostProcessor key被覆盖， 仅前置用例执行时，需要该参数
     * @param suiteLogDetailNo suiteLogNo仅记录重跑和真正运行的，suiteLogDetailNo会包括用例所依赖的case
     * @param chainNo 调用链路跟踪 将调用链信息序列化
     * @throws BusinessException BusinessException
     */
    private void cacheRequest(List<InterfaceProcessorDO> processorList, Integer executedLogId,
                              String headers, String params, String formDataEncoded, String raw,
                              String casePreNo, String suiteLogDetailNo, String chainNo) throws BusinessException {

        // 查询用例的请求参数
        InterfaceProcessorLogDO interfaceProcessorLogDO;
        String value = null; // 缓存值

        if (!processorList.isEmpty()) {
            for (InterfaceProcessorDO processorDO : processorList) {
                Byte type = processorDO.getType();
                String expression = processorDO.getExpression();
                String name = processorDO.getName();
                Byte haveDefaultValue = processorDO.getHaveDefaultValue();
                String defaultValue = processorDO.getDefaultValue();
                Integer caseId = processorDO.getCaseId();

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
                        } else {
                            status = 1;
                            errorMsg = String.format("request cache error, json-path extract content is null, " +
                                    "expression[%s], processor=[%s], case=[%s]", expression, name, caseId);
                            LOG.error(errorMsg);
                        }
                    } else {
                        isDefaultValue = 1;
                        if (jsonPathArray.size() == 1) {
                            Object o = jsonPathArray.get(0);
                            value = o == null ? null : o.toString();
                        } else {
                            value = JSON.toJSONString(jsonPathArray);
                        }
                    }
                    interfaceProcessorLogDO = new InterfaceProcessorLogDO();
                    interfaceProcessorLogDO.setProcessorId(processorDO.getProcessorId());
                    interfaceProcessorLogDO.setCaseId(processorDO.getCaseId());
                    interfaceProcessorLogDO.setCaseExecuteLogId(executedLogId);
                    interfaceProcessorLogDO.setName(name);
                    interfaceProcessorLogDO.setType(type);
                    interfaceProcessorLogDO.setExpression(expression);
                    interfaceProcessorLogDO.setWr((byte) 1);
                    if (!isThrowException) {
                        interfaceProcessorLogDO.setIsDefaultValue(isDefaultValue);
                        interfaceProcessorLogDO.setValue(value);
                        interfaceProcessorLogDO.setCreatedTime(new Date());
                        interfaceProcessorLogDO.setStatus(status);
                        if (status == 0) {
                            interfaceProcessorLogDO.setErrorMsg(null);
                        } else {
                            interfaceProcessorLogDO.setErrorMsg(errorMsg);
                        }
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
                        if (status == 1) {
                            InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                            afterPostProcessorLogDo.setId(executedLogId);
                            afterPostProcessorLogDo.setStatus((byte) 2);
                            afterPostProcessorLogDo.setErrorMessage(errorMsg);
                            executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                            LOG.info("处理器运行错误，主动将用例执行状态置为错误");
                            throw new BusinessException(errorMsg);
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
                        LOG.error("处理器解析出错，写入处理器记录表");
                        // 重置用例状态
                        InterfaceCaseExecuteLogDO afterPostProcessorLogDo = new InterfaceCaseExecuteLogDO();
                        afterPostProcessorLogDo.setId(executedLogId);
                        afterPostProcessorLogDo.setStatus((byte) 2);
                        afterPostProcessorLogDo.setErrorMessage(exceptionMsg);
                        executeLogService.modifyExecuteLog(afterPostProcessorLogDo);
                        LOG.error("处理器解析出错，主动将用例执行状态置为错误");
                        throw new BusinessException(errorMsg);
                    }
                }
            }
        }
    }

    /**
     * 缓存响应数据
     * @param processorList 处理器列表
     * @param interfaceCaseId 测试用例编号
     * @param executedLogId 此次执行日志编号
     * @param statusAfterAssert 断言处理后的执行日志状态
     * @param responseHeaders 响应头
     * @param responseBody 响应体
     * @param casePreNo 前置用例的参数缓存用的key，为了防止异步执行用例时，tempPostProcessor key被覆盖， 仅前置用例执行时，需要该参数
     * @param suiteLogDetailNo suiteLogNo仅记录重跑和真正运行的，suiteLogDetailNo会包括用例所依赖的case
     * @param chainNo 调用链路跟踪 将调用链信息序列
     */
    private void cacheResponse(List<InterfaceProcessorDO> processorList, Integer interfaceCaseId, Integer executedLogId,
                               Byte statusAfterAssert, String responseHeaders, String responseBody,
                               String casePreNo, String suiteLogDetailNo, String chainNo) {
        if (statusAfterAssert.intValue() == 0 ) { // *仅用例执行通过才写入处理器记录

            InterfaceProcessorLogDO interfaceProcessorLogDO;
            String value = null;

            if (!processorList.isEmpty()) {
                for(InterfaceProcessorDO processorDO : processorList) {
                    Byte type = processorDO.getType();
                    String expression = processorDO.getExpression();
                    String name = processorDO.getName();
                    Byte haveDefaultValue = processorDO.getHaveDefaultValue();
                    String defaultValue = processorDO.getDefaultValue();
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
                                errorMsg = String.format("response cache error, json-path extract content is null, " +
                                        "expression[%s], processor=[%s], case=[%s]", expression, name, interfaceCaseId);
                                LOG.error(errorMsg);
                            }
                        } else {
                            isDefaultValue = 1;
                            if (jsonPathArray.size() == 1) {
                                Object o = jsonPathArray.get(0);
                                value = o == null ? null : o.toString();
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
                                errorMsg = String.format("response cache error, xpath extract content is null, " +
                                        "expression[%s], processor=[%s], case=[%s]", expression, name, interfaceCaseId);
                                LOG.error(errorMsg);
                            }
                        } else {
                            isDefaultValue = 1;
                            if (xpathArray.size() == 1) {
                                Object o = xpathArray.get(0);
                                value = o == null ? null : o.toString();
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
                                errorMsg = String.format("response cache error, headers json-path extract content is null, " +
                                        "expression[%s], processor=[%s], case=[%s]", expression, name, interfaceCaseId);
                                LOG.error(errorMsg);
                            }
                        } else {
                            isDefaultValue = 1;
                            if (headerArray.size() == 1) {
                                Object o = headerArray.get(0);
                                value = o == null ? null : o.toString();
                                JSONArray var1 = JSON.parseArray(value);
                                if (var1 == null) {
                                    value = null;
                                } else {
                                    if (var1.size() == 1) {
                                        Object var2 = var1.get(0);
                                        value = var2 == null ? null : var2.toString();
                                    } else {
                                        value = JSON.toJSONString(var1);
                                    }
                                }
                            } else {
                                value = JSON.toJSONString(headerArray);
                            }
                        }
                    } else if (type >= 7) {
                        status = 1;
                        LOG.error("not supported the processor type");
                        errorMsg = "not supported the processor type";
                    }

                    if (type <= 2) {
                        interfaceProcessorLogDO = new InterfaceProcessorLogDO();
                        interfaceProcessorLogDO.setProcessorId(processorDO.getProcessorId());
                        interfaceProcessorLogDO.setCaseId(processorDO.getCaseId());
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
                            RelyType relyType; // 0response-json/1response-html/2response-header
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
                            LOG.error("处理器解析出错，写入处理器记录表");
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
    }
}
