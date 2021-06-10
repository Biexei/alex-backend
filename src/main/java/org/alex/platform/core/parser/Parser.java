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
        Pattern p = Pattern.compile("\\$\\{.+?}");
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            String relyName = findStr.substring(2, findStr.length() - 1);
            String relyExpress = relyName; // 带索引的
            LOG.info("relyName={}", relyName);
// 进入数组下标取值模式
            if (Pattern.matches("[a-zA-Z]+\\[[0-9]+]", relyName)) {
                long start = TimeUtil.now();
                LOG.info("--------------------------------------进入数组下标取值模式");
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
                    InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                    interfaceCaseRelyDataDTO.setRelyName(relyName);
                    LOG.info("根据relyName查询用例信息，relyName={}", relyName);
                    InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                    if (null == interfaceCaseRelyDataVO) {
                        LOG.warn("未找到对应的用例信息，relyName={}", relyName);
                        throw new ParseException("未找到该依赖数值, ${" + relyName + "}");
                    }
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    LOG.info("获取到的用例编号={}", caseId);
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(
                            caseId, "系统调度", null, chainNo, suiteId,
                            isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, (byte)4, casePreNo));

                    LOG.info("执行用例编号={}，执行日志编号={}", caseId, executeLogId);
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        LOG.warn("前置用例执行未通过，执行用例编号={}，执行日志编号={}", caseId, executeLogId);
                        throw new BusinessException("前置用例执行未通过");
                    }
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    LOG.warn("前置用例responseBody={}", responseBody);
                    LOG.warn("前置用例responseHeaders={}", responseHeaders);
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            if (jsonPathArray.isEmpty()) {
                                LOG.warn("jsonPath提取内容为空，jsonPath={}", expression);
                                throw new ParseException(expression + "提取内容为空");
                            }
                            try {
                                String value = jsonPathArray.get(index).toString();
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_JSON, executeLogId, relyExpress, value, TimeUtil.now()-start, expression));
                                s = s.replace(findStr, value);
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
                                String value = xpathArray.get(index).toString();
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HTML, executeLogId, relyExpress, value, TimeUtil.now()-start, expression));
                                s = s.replace(findStr, value);
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
                                String value = headerArray.get(index).toString();
                                redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HEADER, executeLogId, relyExpress, value, TimeUtil.now()-start, expression));
                                s = s.replace(findStr, value);
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
// 进入预置方法/动态SQL模式
            } else if (Pattern.matches("\\w+\\((,?|(\".*\")?|\\s?)+\\)$", relyName)) {
                long start = TimeUtil.now();
                LOG.info("--------------------------------------进入预置方法/动态SQL模式");
                if (relyName.indexOf("(") != relyName.lastIndexOf("(") ||
                        relyName.indexOf(")") != relyName.lastIndexOf(")")) {
                    LOG.warn("预置方法/动态SQL语法错误， string={}", relyName);
                    throw new ParseException("预置方法/动态SQL语法错误");
                }
                String methodName = relyName.substring(0, relyName.indexOf("("));
                LOG.warn("预置方法名称/动态SQL依赖名称={}", methodName);
                String[] params = relyName.substring(relyName.indexOf("(") + 1, relyName.length() - 1)
                        .replaceAll(",\\s+", ",").split(",");
                RelyDataVO relyDataVO = relyDataService.findRelyDataByName(methodName);
                if (null == relyDataVO) {
                    LOG.warn("未找到该预置方法/动态SQL依赖名称， string={}", methodName);
                    throw new ParseException("未找到该预置方法/动态SQL依赖名称");
                }
                byte type = relyDataVO.getType();
                if (type == 1) { //反射方法
                    LOG.info("--------------------------------------进入预置方法模式");
                    if (params.length == 1 && "".equals(params[0])) {
                        params = new String[0];
                    }
                    try {
                        Class<?> clazz = Class.forName("org.alex.platform.common.InvokeCenter");
                        Constructor<?> constructor = clazz.getConstructor(byte.class);
                        Class[] paramsList = new Class[params.length];
                        for (int i = 0; i < params.length; i++) {
                            paramsList[i] = String.class;
                            params[i] = params[i].substring(1, params[i].length() - 1);
                        }
                        LOG.info("方法名称={}，方法参数={}", methodName, Arrays.toString(params));
                        Method method = clazz.getMethod(methodName, paramsList);
                        String value = (String) method.invoke(constructor.newInstance(runEnv), params);
                        redisUtil.stackPush(chainNo, chainNode(RelyType.INVOKE, relyDataVO.getId(), relyName, value, TimeUtil.now()-start, null));
                        s = s.replace(findStr, value);
                        LOG.info("预置方法执行并替换后的结果={}", s);
                    } catch (Exception e) {
                        LOG.error("未找到依赖方法或者入参错误, errorMsg={}", ExceptionUtil.msg(e));
                        throw new ParseException("未找到依赖方法或者入参错误, errorMsg=" + ExceptionUtil.msg(e));
                    }
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
                        LOG.warn("SQL依赖名称未找到对应的数据源");
                        throw new ParseException("SQL依赖名称未找到对应的数据源");
                    }
                    DbVO dbVO = dbService.findDbById(datasourceId);
                    int status = dbVO.getStatus();
                    if (status == 1) {
                        LOG.warn("数据源已被禁用，dbName={}", dbVO.getName());
                        throw new ParseException("数据源已被禁用");
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
                        LOG.warn("未找到该依赖数值，relyName={}", relyName);
                        throw new ParseException("未找到该依赖数值${" + relyName + "}");
                    } else {
                        int type = relyDataVO.getType();
                        if (type == 0) {
                            String value = relyDataVO.getValue();
                            if (relyDataVO.getAnalysisRely().intValue() == 0) { // 使能解析依赖
                                value = parseDependency(value, chainNo, suiteId, isFailedRetry, suiteLogDetailNo, globalHeaders, globalParams, globalData, casePreNo);
                            }
                            s = s.replace(findStr, value);
                            redisUtil.stackPush(chainNo, chainNode(RelyType.CONST, relyDataVO.getId(), relyName, value, TimeUtil.now()-start, null));
                        } else if (type >= 2 && type <= 6) {
                            Integer datasourceId = relyDataVO.getDatasourceId();
                            if (null == datasourceId) {
                                LOG.warn("SQL依赖名称未找到对应的数据源");
                                throw new ParseException("SQL依赖名称未找到对应的数据源");
                            }
                            DbVO dbVO = dbService.findDbById(datasourceId);
                            int status = dbVO.getStatus();
                            if (status == 1) {
                                LOG.warn("数据源已被禁用，dbName={}", dbVO.getName());
                                throw new ParseException("数据源已被禁用");
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
                            LOG.warn("未找到该依赖数值，relyName={}，请确保该依赖不是方法", relyName);
                            throw new ParseException("未找到该依赖数值${" + relyName + "}，请确保该依赖不是方法");
                        }
                    }
                } else {
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId,
                            "系统调度", null, chainNo, suiteId, isFailedRetry, suiteLogDetailNo,
                            globalHeaders, globalParams, globalData, (byte)4, casePreNo));
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        LOG.warn("前置用例执行未通过");
                        throw new BusinessException("前置用例执行未通过");
                    }
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    LOG.warn("前置用例responseBody={}", responseBody);
                    LOG.warn("前置用例responseHeaders={}", responseHeaders);
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
                                Object o = jsonPathArray.get(0);
                                if (o == null) {
                                    throw new BusinessException(jsonPathArray + "提取内容为空");
                                }
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
                                LOG.warn("xpath提取内容为空，jsonPath={}", expression);
                                throw new ParseException(expression + "提取内容为空");
                            }
                            if (xpathArray.size() == 1) {
                                Object o = xpathArray.get(0);
                                if (o == null) {
                                    throw new BusinessException(xpathArray + "提取内容为空");
                                }
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
                                LOG.warn("未找到请求头，header={}", expression);
                                throw new ParseException("未找到请求头:" + expression);
                            } else {
                                if (headerArray.size() == 1) {
                                    Object o = headerArray.get(0);
                                    if (o == null) {
                                        throw new BusinessException(headerArray + "提取内容为空");
                                    }
                                    redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HEADER, executeLogId, relyName, o.toString(), TimeUtil.now()-start, expression));
                                    s = s.replace(findStr, o.toString());
                                } else {
                                    redisUtil.stackPush(chainNo, chainNode(RelyType.INTERFACE_HEADER, executeLogId, relyName, JSON.toJSONString(headerArray), TimeUtil.now()-start, expression));
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
        Pattern pattern = Pattern.compile("#\\{.+?}");
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
                LOG.error("未找到处理器#{" + postProcessorName + "}");
                throw new ParseException("未找到处理器#{" + postProcessorName + "}");
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
}
