package org.alex.platform.core.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.common.Env;
import org.alex.platform.core.common.Node;
import org.alex.platform.enums.RelyType;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.*;
import org.alex.platform.pojo.entity.DbConnection;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.alex.platform.service.*;
import org.alex.platform.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@SuppressWarnings({"rawtypes"})
public class Parser implements Node {

    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);
    private static final String DEPENDENCY_REGEX = "\\$\\{.+?}";
    private static final String DEPENDENCY_REGEX_INDEX = "[a-zA-Z]+\\[[0-9]+]";
    private static final String DEPENDENCY_REGEX_PARAMS = "\\w+\\((,?|(\'.*\')?|\\s?)+\\)$";
    private static final String PROCESSOR_REGEX = "#\\{.+?}";

    @Autowired
    InterfaceCaseSuiteService ifSuiteService;
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;
    @Autowired
    InterfaceCaseRelyDataService ifCaseRelyDataService;
    @Autowired
    RelyDataService relyDataService;
    @Autowired
    DbService dbService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    InterfaceProcessorService interfaceProcessorService;
    @Autowired
    InterfaceProcessorLogService interfaceProcessorLogService;
    @Autowired
    Env env;



    /**
     * 字符清洗
     * @param s 待清洗数据
     * @param chainNo 调用链路跟踪 每次调用均会将自增日志编号写入缓存，再序列化
     * @param suiteId 测试套件编号，主要用于调用入口为测试套件时确定运行环境，否则应该传参null
     * @param isFailedRetry 判断该执行日志是否为失败重试，0是1否，主要用于测试报告统计断言情况
     * @param suiteLogDetailNo suiteLogNo仅记录重跑和真正运行的，suiteLogDetailNo会包括用例所依赖的case
     * @param globalHeaders 测试套件中的全局请求headers
     * @param globalParams 测试套件中的全局请求params
     * @param globalData 测试套件中的全局请求data
     * @param casePreNo 前置用例UUID编号
     * @return 清洗后的数据
     * @throws ParseException ParseException
     * @throws BusinessException BusinessException
     * @throws SqlException SqlException
     */

    public String parseDependency(String s, String chainNo, Integer suiteId, Byte isFailedRetry, String suiteLogDetailNo,
                                HashMap globalHeaders, HashMap globalParams, HashMap globalData, String casePreNo)
            throws ParseException, BusinessException, SqlException {
        if (s == null || s.isEmpty()) {
            return s;
        }
        // 解析处理器
        s = parseProcessor(s, suiteLogDetailNo, casePreNo, chainNo);

        LOG.info("--------------------------------------开始字符ParseUtil串解析流程--------------------------------------");
        LOG.info("--------------------------------------待解析字符串原文={}", s);
        Byte runEnv;
        if (suiteId == null) {
            runEnv = 4;
        } else {
            runEnv = ifSuiteService.findInterfaceCaseSuiteById(suiteId).getRunDev();
        }
        LOG.info("--------------------------------------运行环境={}, 0dev 1test 2stg 3prod 4debug", runEnv);
        Pattern p = Pattern.compile(DEPENDENCY_REGEX);
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            String relyName = findStr.substring(2, findStr.length() - 1);
            String relyExpress = relyName; // 带索引的
            LOG.info("relyName={}", relyName);
// 进入数组下标取值模式
            if (Pattern.matches(DEPENDENCY_REGEX_INDEX, relyName)) {
                long start = TimeUtil.now();
                LOG.info("--------------------------------------进入数组下标取值模式");
                if (relyName.indexOf("[") != relyName.lastIndexOf("[") ||
                        relyName.indexOf("]") != relyName.lastIndexOf("]")) {
                    LOG.error("array index error, relyName={}", relyName);
                    throw new ParseException("array index error");
                }
                String indexStr = relyName.substring(relyName.indexOf("[") + 1, relyName.length() - 1);
                try {
                    int index = Integer.parseInt(indexStr);
                    LOG.info("数据下标={}", index);
                    relyName = relyName.substring(0, relyName.indexOf("["));
                    LOG.info("去除下标后的真实relyName={}", relyName);
                    InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                    interfaceCaseRelyDataDTO.setRelyName(relyName);
                    LOG.info("根据relyName查询用例信息，relyName={}", relyName);
                    InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                    if (null == interfaceCaseRelyDataVO) {
                        String nf = String.format("dependency ${%s} not found", relyName);
                        LOG.error(nf);
                        throw new ParseException(nf);
                    }
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    LOG.info("获取到的用例编号={}", caseId);
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(
                            caseId, "系统调度", null, chainNo, suiteId,
                            isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, (byte)4, casePreNo, false));

                    LOG.info("执行用例编号={}，执行日志编号={}", caseId, executeLogId);
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        String nf = String.format("dependency related case [%s] execution failed or error, " +
                                "the execution log id was [%s]", caseId, executeLogId);
                        LOG.error(nf);
                        throw new BusinessException(nf);
                    }
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    LOG.info("依赖用例responseBody={}", responseBody);
                    LOG.info("依赖用例responseHeaders={}", responseHeaders);
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                String nf = String.format("case dependency [%s] json-path extract content was null, " +
                                        "the case id was [%s], the execution log id was [%s], the expression was [%s]", relyName, caseId, executeLogId, expression);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                            try {
                                String value = jsonPathArray.get(index).toString();
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_JSON, executeLogId, relyExpress, value, TimeUtil.now()-start, expression));
                                s = s.replace(findStr, value);
                                LOG.info("jsonPath提取值并替换后的结果={}", s);
                            } catch (Exception e) {
                                String nf = String.format("json-path index out of bounds, case dependency [%s], index [%s]", relyName, index);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            if (xpathArray.isEmpty()) {
                                String nf = String.format("case dependency [%s] xpath extract content was null, " +
                                        "the case id was [%s], the execution log id was [%s], the expression was [%s]", relyName, caseId, executeLogId, expression);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                            try {
                                String value = xpathArray.get(index).toString();
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HTML, executeLogId, relyExpress, value, TimeUtil.now()-start, expression));
                                s = s.replace(findStr, value);
                                LOG.info("xpath提取值并替换后的结果={}", s);
                            } catch (Exception e) {
                                String nf = String.format("xpath index out of bounds, case dependency [%s], index [%s]", relyName, index);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(expression);
                            if (null == headerArray || headerArray.isEmpty()) {
                                String nf = String.format("case dependency [%s] headers extract content was null, " +
                                        "the case id was [%s], the execution log id was [%s], the expression was [%s]", relyName, caseId, executeLogId, expression);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                            try {
                                String value = headerArray.get(index).toString();
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HEADER, executeLogId, relyExpress, value, TimeUtil.now()-start, expression));
                                s = s.replace(findStr, value);
                                LOG.info("header提取值并替换后的结果={}", s);
                            } catch (Exception e) {
                                String nf = String.format("header index out of bounds, case dependency [%s], index [%s]", relyName, index);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                        } else {
                            throw new BusinessException("not supported the extract type");
                        }
                    } catch (BusinessException e) {
                        LOG.error("not supported the extract type");
                        throw new BusinessException("not supported the extract type");
                    } catch (Exception e) {
                        LOG.error("extra by array error , " + ExceptionUtil.msg(e));
                        throw new ParseException(e.getMessage());
                    }
                } catch (NumberFormatException e) {
                    LOG.error("array index argument illegal");
                    throw new ParseException("array index argument illegal");
                }
// 进入预置方法/动态SQL模式
            } else if (Pattern.matches(DEPENDENCY_REGEX_PARAMS, relyName)) {
                long start = TimeUtil.now();
                LOG.info("--------------------------------------进入预置方法/动态SQL模式");
                if (relyName.indexOf("(") != relyName.lastIndexOf("(") ||
                        relyName.indexOf(")") != relyName.lastIndexOf(")")) {
                    String nf = String.format("dependency init method or sql [%s] syntax error", relyName);
                    LOG.error(nf);
                    throw new ParseException(nf);
                }
                String methodName = relyName.substring(0, relyName.indexOf("("));
                LOG.info("预置方法名称/动态SQL依赖名称={}", methodName);
                String[] params = relyName.substring(relyName.indexOf("(") + 1, relyName.length() - 1)
                        .replaceAll(",\\s+", ",").split(",");
                RelyDataVO relyDataVO = relyDataService.findRelyDataByName(methodName);
                if (null == relyDataVO) {
                    String nf = String.format("init method or sql [%s] not found", relyName);
                    LOG.error(nf);
                    throw new ParseException(nf);
                }
                byte type = relyDataVO.getType();
                if (type == 1) { //反射方法
                    LOG.info("--------------------------------------进入预置方法模式");

                    Method method;
                    String methodReturnValue;
                    Class<?> clazz;
                    Constructor<?> constructor;

                    try {
                        clazz = Class.forName("org.alex.platform.common.InvokeCenter");
                        constructor = clazz.getConstructor(byte.class);
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        String nf = "org.alex.platform.common.InvokeCenter constructor not found";
                        LOG.error(nf);
                        LOG.error(ExceptionUtil.msg(e));
                        throw new ParseException(nf);
                    }

                    if (params.length == 1 && "".equals(params[0])) {
                        params = new String[0];
                    }

                    try { // 尝试固定长度参数
                        clazz = Class.forName("org.alex.platform.common.InvokeCenter");
                        constructor = clazz.getConstructor(byte.class);
                        Class[] paramsList = new Class[params.length];
                        for (int i = 0; i < params.length; i++) {
                            paramsList[i] = String.class;
                            params[i] = params[i].substring(1, params[i].length() - 1);
                        }
                        LOG.info("固定长度参数，方法名称={}，方法参数={}", methodName, Arrays.toString(params));
                        method = clazz.getMethod(methodName, paramsList);
                        methodReturnValue = (String) method.invoke(constructor.newInstance(runEnv), params);
                        LOG.info("固定长度参数，预置方法执行并替换后的结果={}", s);
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                        // 尝试可变参数
                        try {
                            LOG.info("固定长度参数异常，尝试可变长度参数，方法名称={}，方法参数={}", methodName, Arrays.toString(params));
                            method = clazz.getMethod(methodName, String[].class);
                            methodReturnValue = (String) method.invoke(constructor.newInstance(runEnv), (Object) params);
                            LOG.info("固定长度参数异常，尝试可变长度参数，预置方法执行并替换后的结果={}", s);
                        } catch (Exception ee) {
                            String nf = String.format("dependency init method execution error, maybe not found or parameter error, method was [%s], params was[%s]",methodName, Arrays.toString(params));
                            LOG.error(nf);
                            LOG.error(ExceptionUtil.msg(ee));
                            throw new ParseException(nf);
                        }
                    }
                    redisUtil.stackPush(chainNo, chainNode(RelyType.INVOKE, relyDataVO.getId(), relyName, methodReturnValue, TimeUtil.now()-start, null));
                    s = s.replace(findStr, methodReturnValue);
                } else if (type >= 2 && type <= 6) { //sql 2sql-select 3sql-insert 4sql-update 5sql-delete 6sql-script
                    LOG.info("--------------------------------------进入动态SQL模式");
                    if (params.length == 1 && "".equals(params[0])) {
                        params = null;
                    } else {
                        for (int i = 0; i < params.length; i++) {
                            // 去除首尾引号
                            params[i] = params[i].substring(1, params[i].length() - 1);
                        }
                    }
                    Integer datasourceId = relyDataVO.getDatasourceId();
                    if (null == datasourceId) {
                        String nf = String.format("data source not found, the datasource id was [%s]", datasourceId);
                        LOG.error(nf);
                        throw new ParseException(nf);
                    }
                    DbVO dbVO = dbService.findDbById(datasourceId);
                    int status = dbVO.getStatus();
                    if (status == 1) {
                        String nf = String.format("data source access denied, the datasource id was [%s]", datasourceId);
                        LOG.error(nf);
                        throw new ParseException(nf);
                    }
                    DbConnection datasource = env.datasource(dbVO, runEnv);
                    String url = datasource.getUrl();
                    String username = datasource.getUsername();
                    String password = datasource.getPassword();
                    // 支持动态sql
                    String sql = relyDataVO.getValue();
                    if (relyDataVO.getValue() != null) {
                        LOG.info("开始解析SQL，解析前SQL={}", sql);
                        sql = parseDependency(sql, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                        LOG.info("解析SQL完成，解析后SQL={}", sql);
                    }
                    LOG.info("SQL执行参数，SQL={}, params={}", sql, params);

                    String sqlResult;
                    if (type == 2) { // 查询
                        if (relyDataVO.getAnalysisRely().intValue() == 0) {
                            sqlResult = JdbcUtil.selectFirst(url, username, password, sql, params);
                        } else {
                            sqlResult = JdbcUtil.selectFirst(url, username, password, relyDataVO.getValue(), params);
                        }
                        redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_SELECT, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                    } else if (type == 3) { // 新增
                        if (relyDataVO.getEnableReturn().intValue() == 0) {
                            sqlResult = String.valueOf(JdbcUtil.insert(url, username, password, sql, params));
                        } else {
                            JdbcUtil.insert(url, username, password, sql, params);
                            sqlResult = "";
                        }
                        redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_INSERT, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                    } else if (type == 4) { // 修改
                        if (relyDataVO.getAnalysisRely().intValue() == 0) {
                            sqlResult = JdbcUtil.update(url, username, password, sql, params);
                        } else {
                            sqlResult = JdbcUtil.update(url, username, password, relyDataVO.getValue(), params);
                        }
                        redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_UPDATE, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                    } else if (type == 5) { // 删除
                        if (relyDataVO.getAnalysisRely().intValue() == 0) {
                            sqlResult = JdbcUtil.delete(url, username, password, sql, params);
                        } else {
                            sqlResult = JdbcUtil.delete(url, username, password, relyDataVO.getValue(), params);
                        }
                        redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_DELETE, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                    } else { // 脚本
                        if (relyDataVO.getAnalysisRely().intValue() == 0) {
                            sqlResult = JdbcUtil.script(sql, url, username, password, true);
                        } else {
                            sqlResult = JdbcUtil.script(relyDataVO.getValue(), url, username, password, true);
                        }
                        redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_SCRIPT, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                    }
                    s = s.replace(findStr, sqlResult);
                }

// 进入普通依赖数据模式
            } else {
                long start = TimeUtil.now();
                LOG.info("--------------------------------------进入普通依赖数据模式");
                InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                interfaceCaseRelyDataDTO.setRelyName(relyName);
                InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                if (null == interfaceCaseRelyDataVO) {
                    RelyDataVO relyDataVO = relyDataService.findRelyDataByName(relyName);
                    if (null == relyDataVO) {
                        String nf = String.format("dependency [%s] not found", relyName);
                        LOG.error(nf);
                        throw new ParseException(nf);
                    } else {
                        int type = relyDataVO.getType();
                        if (type == 0) {
                            String value = relyDataVO.getValue();
                            s = s.replace(findStr, value);
                            redisUtil.stackPush(chainNo, chainNode(RelyType.CONST, relyDataVO.getId(), relyName, value, TimeUtil.now()-start, null));
                        } else if (type >= 2 && type <= 6) {
                            Integer datasourceId = relyDataVO.getDatasourceId();
                            if (null == datasourceId) {
                                String nf = String.format("data source not found, the datasource id was [%s]", datasourceId);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                            DbVO dbVO = dbService.findDbById(datasourceId);
                            int status = dbVO.getStatus();
                            if (status == 1) {
                                String nf = String.format("data source access denied, the datasource id was [%s]", datasourceId);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                            DbConnection datasource = env.datasource(dbVO, runEnv);
                            String url = datasource.getUrl();
                            String username = datasource.getUsername();
                            String password = datasource.getPassword();
                            String sql = relyDataVO.getValue();
                            if (relyDataVO.getValue() != null) {
                                LOG.info("开始解析SQL，解析前SQL={}", sql);
                                sql = parseDependency(sql, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                                LOG.info("解析SQL完成，解析后SQL={}", sql);
                            }
                            LOG.info("SQL执行参数，SQL={}", sql);
                            String sqlResult;
                            if (type == 2) { // 查询
                                if (relyDataVO.getAnalysisRely().intValue() == 0) {
                                    sqlResult = JdbcUtil.selectFirst(url, username, password, sql, null);
                                } else {
                                    sqlResult = JdbcUtil.selectFirst(url, username, password, relyDataVO.getValue(), null);
                                }
                                redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_SELECT, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                            } else if (type == 3) { // 新增
                                if (relyDataVO.getEnableReturn().intValue() == 0) {
                                    sqlResult = String.valueOf(JdbcUtil.insert(url, username, password, sql, null));
                                } else {
                                    JdbcUtil.insert(url, username, password, sql, null);
                                    sqlResult = "";
                                }
                                redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_INSERT, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                            } else if (type == 4) { // 修改
                                if (relyDataVO.getAnalysisRely().intValue() == 0) {
                                    sqlResult = JdbcUtil.update(url, username, password, sql, null);
                                } else {
                                    sqlResult = JdbcUtil.update(url, username, password, relyDataVO.getValue(), null);
                                }
                                redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_UPDATE, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                            } else if (type == 5) { // 删除
                                if (relyDataVO.getAnalysisRely().intValue() == 0) {
                                    sqlResult = JdbcUtil.delete(url, username, password, sql, null);
                                } else {
                                    sqlResult = JdbcUtil.delete(url, username, password, relyDataVO.getValue(), null);
                                }
                                redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_DELETE, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                            } else { // 脚本
                                if (relyDataVO.getAnalysisRely().intValue() == 0) {
                                    sqlResult = JdbcUtil.script(sql, url, username, password, true);
                                } else {
                                    sqlResult = JdbcUtil.script(relyDataVO.getValue(), url, username, password, true);
                                }
                                redisUtil.stackPush(chainNo, chainNode(RelyType.SQL_SCRIPT, relyDataVO.getId(), relyName, sqlResult, TimeUtil.now()-start, null));
                            }
                            s = s.replace(findStr, sqlResult);
                        } else {
                            String nf = String.format("dependency [%s] not found, make sure it's not method", relyName);
                            LOG.error(nf);
                            throw new ParseException(nf);
                        }
                    }
                } else {
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId,
                            "系统调度", null, chainNo, suiteId, isFailedRetry, suiteLogDetailNo,
                            globalHeaders, globalParams, globalData, (byte)4, casePreNo, false));
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        String nf = String.format("dependency related case [%s] execution failed or error, " +
                                "the execution log id was [%s]", caseId, executeLogId);
                        LOG.error(nf);
                        throw new BusinessException(nf);
                    }
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    LOG.info("依赖用例responseBody={}", responseBody);
                    LOG.info("依赖用例responseHeaders={}", responseHeaders);
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                String nf = String.format("case dependency [%s] json-path extract content was null, " +
                                        "the case id was [%s], the execution log id was [%s], the expression was [%s]", relyName, caseId, executeLogId, expression);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                            if (jsonPathArray.size() == 1) {
                                Object o = jsonPathArray.get(0);
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_JSON, executeLogId, relyName, o.toString(), TimeUtil.now()-start, expression));
                                s = s.replace(findStr, o.toString());
                            } else {
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_JSON, executeLogId, relyName, JSON.toJSONString(jsonPathArray), TimeUtil.now()-start, expression));
                                s = s.replace(findStr, JSON.toJSONString(jsonPathArray));
                            }
                            LOG.info("jsonPath提取值并替换后的结果={}", s);
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            if (xpathArray.isEmpty()) {
                                String nf = String.format("case dependency [%s] xpath extract content was null, " +
                                        "the case id was [%s], the execution log id was [%s], the expression was [%s]", relyName, caseId, executeLogId, expression);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            }
                            if (xpathArray.size() == 1) {
                                Object o = xpathArray.get(0);
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HTML, executeLogId, relyName, o.toString(), TimeUtil.now()-start, expression));
                                s = s.replace(findStr, o.toString());
                            } else {
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HTML, executeLogId, relyName, JSON.toJSONString(xpathArray), TimeUtil.now()-start, expression));
                                s = s.replace(findStr, JSON.toJSONString(xpathArray));
                            }
                            LOG.info("xml提取值并替换后的结果={}", s);
                        } else if (contentType == 2) { // headers
                            ArrayList headerArray = JSONObject.parseObject(ParseUtil.parseJson(responseHeaders, expression), ArrayList.class);
                            if (headerArray == null || headerArray.isEmpty()) {
                                String nf = String.format("case dependency [%s] header's json-path extract content was null, " +
                                        "the case id was [%s], the execution log id was [%s], the expression was [%s]", relyName, caseId, executeLogId, expression);
                                LOG.error(nf);
                                throw new ParseException(nf);
                            } else {
                                if (headerArray.size() == 1) {
                                    Object o = headerArray.get(0);
                                    redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HEADER, executeLogId, relyName, o.toString(), TimeUtil.now()-start, expression));
                                    s = s.replace(findStr, o.toString());
                                } else {
                                    redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HEADER, executeLogId, relyName, JSON.toJSONString(headerArray), TimeUtil.now()-start, expression));
                                    s = s.replace(findStr, JSON.toJSONString(headerArray));
                                }
                                LOG.info("header提取值并替换后的结果={}", s);
                            }
                        } else {
                            throw new BusinessException("not supported the extract type");
                        }
                    } catch (BusinessException e) {
                        String nf = "not supported the extract type";
                        LOG.error(nf);
                        throw new BusinessException(nf);
                    } catch (Exception e) {
                        String nf = String.format("parse dependency error, errorMsg [%s]", ExceptionUtil.msg(e));
                        LOG.error(nf);
                        throw new ParseException(nf);
                    }
                }
            }
        }
        return s;
    }

    /**
     * 解析处理器
     * @param s 原文
     * @param suiteLogDetailNo 非空时使用测试套件域，否则使用临时域
     * @param casePreNo 组合用例的前置用例缓存redis hash key
     * @param chainNo 链路跟踪redis key
     * @return 解析后的字符串
     * @throws ParseException 找不到处理器
     */
    public String parseProcessor(String s, String suiteLogDetailNo, String casePreNo, String chainNo) throws ParseException {
        LOG.info("--------------------------------------开始处理器提取解析流程--------------------------------------");
        LOG.info("--------------------------------------待解析字符串原文={}", s);
        if (s == null || s.isEmpty()) {
            return s;
        }
        Pattern pattern = Pattern.compile(PROCESSOR_REGEX);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            long start = TimeUtil.now();
            String findStr = matcher.group();
            String postProcessorName = findStr.substring(2, findStr.length() - 1);
            InterfaceProcessorVO postProcessor = interfaceProcessorService.findInterfaceProcessorByName(postProcessorName);
            LOG.info("处理器名称={}", postProcessorName);
            Object redisResult;
            if (casePreNo == null) {
                if (suiteLogDetailNo == null) {
                    LOG.info("suiteLogDetailNo == null && casePreNo == null，使用临时变量域");
                    redisResult = redisUtil.hashGet(NoUtil.TEMP_POST_PROCESSOR_NO, postProcessorName);
                } else {
                    LOG.info("suiteLogDetailNo != null && casePreNo == null，使用测试套件域");
                    redisResult = redisUtil.hashGet(suiteLogDetailNo, postProcessorName);
                }
            } else {
                LOG.info("casePreNo != null，使用前置用例域");
                redisResult = redisUtil.hashGet(casePreNo, postProcessorName);
            }
            if (redisResult == null) {
                String nf = String.format("processor [%s] not found", postProcessorName);
                LOG.error(nf);
                throw new ParseException(nf);
            }
            String redisResultStr = redisResult.toString();
            redisUtil.stackPush(chainNo, chainNode(RelyType.READ_PROCESSOR, null, postProcessorName, redisResultStr, TimeUtil.now()-start, null));
            s = s.replace(findStr, redisResultStr);

            // 写入处理器日志表
            InterfaceProcessorLogDO interfaceProcessorLogDO = new InterfaceProcessorLogDO();
            interfaceProcessorLogDO.setName(postProcessor.getName());
            interfaceProcessorLogDO.setValue(redisResultStr);
            interfaceProcessorLogDO.setCreatedTime(new Date());
            interfaceProcessorLogDO.setStatus((byte) 0);
            interfaceProcessorLogDO.setWr((byte)0);
            interfaceProcessorLogService.saveInterfaceProcessorLog(interfaceProcessorLogDO);
        }
        LOG.info("--------------------------------------结束处理器提取解析流程--------------------------------------");
        LOG.info("--------------------------------------解析后的字符串为={}", s);
        return s;
    }

    /**
     * 提取文本中的依赖(仅名称)
     * @param text 字符串文本
     * @return 依赖名称列表
     */
    public ArrayList<String> extractDependencyName(String text) {
        ArrayList<String> list = new ArrayList<>();
        if (text != null) {
            // 去除处理器，否则若依赖中包含处理器将解析出错
            text = text.replaceAll(PROCESSOR_REGEX, "");
            Matcher matcher = Pattern.compile(DEPENDENCY_REGEX).matcher(text);
            while(matcher.find()) {
                String finds = matcher.group();
                String dependencyExpression = finds.substring(2, finds.length() - 1);
                if (Pattern.matches(DEPENDENCY_REGEX_INDEX, dependencyExpression)) { // 数组下标 带[]
                    String dependencyName = dependencyExpression.substring(0, dependencyExpression.indexOf("["));
                    list.add(dependencyName);
                } else if (Pattern.matches(DEPENDENCY_REGEX_PARAMS, dependencyExpression)) { // 方法或者sql 带（）
                    String dependencyName = dependencyExpression.substring(0, dependencyExpression.indexOf("("));
                    list.add(dependencyName);
                } else { // 普通模式
                    list.add(dependencyExpression);
                }
            }
        }
        return list;
    }

    /**
     * 提取文本中的处理器名称
     * @param text 字符串文本
     * @return 依赖名称列表
     */
    public ArrayList<String> extractProcessorName(String text) {
        ArrayList<String> list = new ArrayList<>();
        if (text != null) {
            Matcher matcher = Pattern.compile(PROCESSOR_REGEX).matcher(text);
            while(matcher.find()) {
                String finds = matcher.group();
                String dependencyExpression = finds.substring(2, finds.length() - 1);
                list.add(dependencyExpression);
            }
        }
        return list;
    }
}
