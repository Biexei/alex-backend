package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.*;
import org.alex.platform.util.AssertUtil;
import org.alex.platform.util.JdbcUtil;
import org.alex.platform.util.ParseUtil;
import org.alex.platform.util.RestUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 执行指定用例编号
     * @param interfaceCaseId 需要执行的用例编号
     * @return 执行日志编号
     */
    @Override
    public Integer executeInterfaceCase(Integer interfaceCaseId) throws ParseException, BusinessException, SqlException {
        String exceptionMessage = null;
        // 运行结果 0成功 1失败 2错误
        Byte caseStatus = 0;

        // 1.获取case详情
        InterfaceCaseInfoVO interfaceCaseInfoVO = this.findInterfaceCaseByCaseId(interfaceCaseId);
        Integer projectId = interfaceCaseInfoVO.getProjectId();
        String url = projectService.findModulesById(projectId).getDomain() + interfaceCaseInfoVO.getUrl();
        String desc = interfaceCaseInfoVO.getDesc();
        String headers = interfaceCaseInfoVO.getHeaders();
        // 清洗
        headers = this.parseRelyData(headers);
        String params = interfaceCaseInfoVO.getParams();
        params = this.parseRelyData(params);
        String data = interfaceCaseInfoVO.getData();
        data = this.parseRelyData(data);
        String json = interfaceCaseInfoVO.getJson();
        json = this.parseRelyData(json);
        Byte method = interfaceCaseInfoVO.getMethod();
        List<InterfaceAssertVO> asserts = interfaceCaseInfoVO.getAsserts();

        // 2.执行case
        // a.获取请求方式  0get,1post,2update,3put,4delete
        ResponseEntity responseEntity = null;
        try {
            HashMap headersMap = JSONObject.parseObject(headers, HashMap.class);
            HashMap paramsMap = JSONObject.parseObject(params, HashMap.class);
            if (method == 0) { //get
                responseEntity = RestUtil.get(url, headersMap, paramsMap);
            } else if (method == 1) { //post
                HashMap dataMap = JSONObject.parseObject(data, HashMap.class);
                responseEntity = RestUtil.post(url, headersMap, paramsMap, dataMap, json);
            } else if (method == 2) { //update
                throw new BusinessException("暂不支持update请求方式");
            } else if (method == 3) { //put
                throw new BusinessException("暂不支持put请求方式");
            } else if (method == 4) { //delete
                throw new BusinessException("暂不支持delete请求方式");
            } else {
                throw new BusinessException("不支持的请求方式");
            }
        } catch (Exception e) {
            // 出现异常则追加错误信息，并将case状态设置为2错误
            caseStatus = 2;
            e.printStackTrace();
            exceptionMessage = e.getMessage();
        }

        Integer responseCode = null;
        String responseHeaders = null;
        String responseBody = null;

        if (responseEntity == null) { // responseEntity == null, 则请求失败，只保存执行日志
            // 3.保存日志
            InterfaceCaseExecuteLogDO executeLogDO = new InterfaceCaseExecuteLogDO();
            executeLogDO.setCaseId(interfaceCaseId);
            executeLogDO.setCaseDesc(desc);
            executeLogDO.setRequestHeaders(headers);
            executeLogDO.setRequestParams(params);
            executeLogDO.setRequestData(data);
            executeLogDO.setRequestJson(json);
            executeLogDO.setResponseCode(null);
            executeLogDO.setResponseHeaders(null);
            executeLogDO.setResponseBody(null);
            // 后续加入拦截器后根据token反查
            executeLogDO.setExecuter("根据token反查");
            executeLogDO.setStatus((byte) 2);
            executeLogDO.setCreatedTime(new Date());
            executeLogDO.setErrorMessage(exceptionMessage);
            InterfaceCaseExecuteLogDO executedLogDO = executeLogService.saveExecuteLog(executeLogDO);
            // 返回自增id
            return executedLogDO.getId();
        } else { //请求成功记录执行日志和断言日志
            // 只有接口调用未出现异常才统计code、header、body
            responseCode = RestUtil.code(responseEntity);
            responseHeaders = RestUtil.headers(responseEntity);
            responseBody = RestUtil.body(responseEntity);
            // 3.保存日志
            InterfaceCaseExecuteLogDO executeLogDO = new InterfaceCaseExecuteLogDO();
            executeLogDO.setCaseId(interfaceCaseId);
            executeLogDO.setCaseDesc(desc);
            executeLogDO.setRequestHeaders(headers);
            executeLogDO.setRequestParams(params);
            executeLogDO.setRequestData(data);
            executeLogDO.setRequestJson(json);
            executeLogDO.setResponseCode(responseCode);
            executeLogDO.setResponseHeaders(responseHeaders);
            executeLogDO.setResponseBody(responseBody);
            // 后续加入拦截器后根据token反查
            executeLogDO.setExecuter("后根据token反查");
            executeLogDO.setStatus(caseStatus);
            executeLogDO.setCreatedTime(new Date());
            InterfaceCaseExecuteLogDO executedLogDO = executeLogService.saveExecuteLog(executeLogDO);
            // 4.保存断言日志表，获取运行日志自增id然后在断言日志表中写入断言信息，断言日志都成功后再将日志修改状态为0成功
            // 日志自增id
            int executedLogId = executedLogDO.getId();
            // 遍历用例关联的断言,并写入断言日志表
            // 获取每次执行断言的状态
            List<Byte> statusList = new ArrayList<>();
            for (InterfaceAssertVO interfaceAssertVO : asserts) {
                // 获取断言基本信息
                Integer assertId = interfaceAssertVO.getAssertId();
                String assertName = interfaceAssertVO.getAssertName();
                Byte type = interfaceAssertVO.getType();
                String expression = interfaceAssertVO.getExpression();
                Byte operator = interfaceAssertVO.getOperator();
                // 清洗断言预期结果
                String exceptedResult = interfaceAssertVO.getExceptedResult();
                exceptedResult = this.parseRelyData(exceptedResult);
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
                assertLogDO.setExceptedResult(exceptedResult);
                assertLogDO.setOrder(order);
                // 根据type 提取数据类型   0json/1html/2header/3responseCode 来制定断言方案
                String actualResult = null;
                // 是否通过 0通过 1失败 2错误
                Byte assertStatus = 0;
                // 断言出错异常信息
                String assertErrorMessage = null;
                try {
                    if (type == 0) { // json
                        actualResult = ParseUtil.parseJson(responseBody, expression);
                    } else if (type == 1) { // html
                        actualResult = ParseUtil.parseXml(responseBody, expression);
                    } else if (type == 2) { // header
                        actualResult = ParseUtil.parseHttpHeader(responseEntity, expression);
                    } else if (type == 3) { // responseCode
                        actualResult = String.valueOf(ParseUtil.parseHttpCode(responseEntity));
                    }
                    System.out.println("预期结果为：" + exceptedResult);
                    System.out.println("操作符为：" + operator);
                    System.out.println("实际结果为：" + actualResult);
                    boolean isPass = AssertUtil.asserts(actualResult, operator, exceptedResult);
                    if (isPass) {
                        assertStatus = 0;
                    } else {
                        assertStatus = 1;
                    }
                } catch (ParseException | BusinessException e) {
                    assertStatus = 2;
                    assertErrorMessage = e.getMessage();
                }
                assertLogDO.setActualResult(actualResult);
                assertLogDO.setStatus(assertStatus);
                // 将每次断言status都加入集合
                statusList.add(assertStatus);
                assertLogDO.setErrorMessage(assertErrorMessage);
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
            return executedLogId;
        }
    }

    /**
     * 字符解析
     * @param s 入参字符串
     * @return 解析后的字符串
     * @throws ParseException 解析异常
     * @throws BusinessException 业务异常
     */
    public String parseRelyData(String s) throws ParseException, BusinessException, SqlException {
        Pattern p = Pattern.compile("\\$\\{.+?\\}");
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            // 获取relyName
            String relyName = findStr.substring(2, findStr.length() - 1);
            System.out.println("----------------------------");
            System.out.println(relyName);
// 进入普通依赖数据模式-再进入根据数组下标模式
            if (relyName.indexOf("[") != -1 && relyName.endsWith("]")) {
                System.out.println("进入普通依赖数据模式-再进入根据数组下标模式");
                // 判断出现次数,首次出现和最后一次出现位置不一致，则说明[>1 ]>1
                if (relyName.indexOf("[") != relyName.lastIndexOf("[") ||
                        relyName.indexOf("]") != relyName.lastIndexOf("]")) {
                    throw new ParseException("数组取值语法错误");
                }
                String indexStr = relyName.substring(relyName.indexOf("[") + 1, relyName.length() - 1);
                try {
                    int index = Integer.parseInt(indexStr);
                    relyName = relyName.substring(0, relyName.indexOf("["));
                    // 查询其所依赖的caseId
                    InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                    interfaceCaseRelyDataDTO.setRelyName(relyName);
                    InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                    if (null == interfaceCaseRelyDataVO) {
                        throw new ParseException("未找到该依赖数值");
                    }
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    // 根据caseId调用相应case
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(caseId);
                    // 获取case执行结果, 不等于0, 则用例未通过
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        throw new BusinessException("relyName关联的前置用例执行失败!");
                    }
                    // 根据executeLogId查询对应的执行记录
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
                    if (contentType != 2) {
                        throw new ParseException("只有依赖数据提取类型为header时才支持指定下标，" +
                                "否则请自行调整jsonpath/xpath表达式，使提取结果唯一");
                    }
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            s = s.replace(findStr, (String) jsonPathArray.get(0));
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            s = s.replace(findStr, (String) xpathArray.get(0));
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(expression);
                            try {
                                s = s.replace(findStr, (String) headerArray.get(index));
                            } catch (Exception e) {
                                throw new ParseException("数组下标越界");
                            }
                        } else {
                            throw new BusinessException("不支持该contentType");
                        }
                    } catch (BusinessException e) {
                        throw new BusinessException("不支持该contentType");
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ParseException(e.getMessage());
                    }
                } catch (NumberFormatException e) {
                    throw new ParseException("数组下标只能为数字");
                }
// 进入预置函数模式
            } else if (relyName.indexOf("(") != -1 && relyName.endsWith(")")) {
                System.out.println("进入预置函数模式");
                // 判断出现次数,首次出现和最后一次出现位置不一致，则说明(>1 )>1
                if (relyName.indexOf("(") != relyName.lastIndexOf("(") ||
                        relyName.indexOf(")") != relyName.lastIndexOf(")")) {
                    throw new ParseException("依赖函数语法错误");
                }
                // 获取方法名称
                String methodName = relyName.substring(0, relyName.indexOf("("));
                RelyDataVO relyDataVO = relyDataService.findRelyDataByName(methodName);
                if (null == relyDataVO) {
                    throw new ParseException("未找到该依赖方法");
                }
                // 获取参数列表, 去除引号空格
                String[] params = relyName.substring(relyName.indexOf("(") + 1, relyName.length() - 1)
                        .replace("\"", "").replaceAll(",\\s+", ",")
                        .replace("'", "").split(",");
                // 无参方法特殊处理
                if (params.length == 1 && "".equals(params[0])) {
                    params = new String[0];
                }
                // 反射执行对应方法
                try {
                    Class<?> clazz = Class.forName("org.alex.platform.common.RelyMethod");
                    Class[] paramsList = new Class[params.length];
                    for (int i = 0; i < params.length; i++) {
                        paramsList[i] = String.class;
                    }
                    Method method = clazz.getMethod(methodName, paramsList);
                    s = s.replace(findStr, (String) method.invoke(clazz.newInstance(), params));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ParseException("未找到依赖方法或者入参错误");
                }
// 进入普通依赖数据模式
            } else {
                System.out.println("进入普通依赖数据模式");
                // 查询其所依赖的caseId
                InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                interfaceCaseRelyDataDTO.setRelyName(relyName);
                InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                // 判断是否在t_interface_case_rely_data
                if (null == interfaceCaseRelyDataVO) {
                    RelyDataVO relyDataVO = relyDataService.findRelyDataByName(relyName);
                    // 判断是否在t_rely_data
                    if (null == relyDataVO) {
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
                                throw new ParseException("sql未找到对应的数据源");
                            }
                            DbVO dbVO = dbService.findDbById(datasourceId);
                            // 0启动 1禁用
                            int status = dbVO.getStatus();
                            if (status == 1) {
                                throw new ParseException("数据源已被禁用");
                            }
                            String url = dbVO.getUrl();
                            String username = dbVO.getUsername();
                            String password = dbVO.getPassword();
                            // 支持动态sql
                            String sql = parseRelyData(relyDataVO.getValue());
                            String sqlResult = JdbcUtil.selectFirstColumn(url, username, password, sql);
                            s = s.replace(findStr, sqlResult);
                        }
                    }
                } else {
                    Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                    // 根据caseId调用相应case
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(caseId);
                    // 获取case执行结果, 不等于0, 则用例未通过
                    if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                        throw new BusinessException("relyName关联的前置用例执行失败!");
                    }
                    // 根据executeLogId查询对应的执行记录
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                    String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                    String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                    // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                    int contentType = (int) interfaceCaseRelyDataVO.getContentType();
                    String expression = interfaceCaseRelyDataVO.getExtractExpression();
                    try {
                        if (contentType == 0) { // json
                            ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                            s = s.replace(findStr, (String) jsonPathArray.get(0));
                        } else if (contentType == 1) { // html
                            ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                            s = s.replace(findStr, (String) xpathArray.get(0));
                        } else if (contentType == 2) { // headers
                            JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders,
                                    HashMap.class).get(expression);
                            s = s.replace(findStr, (String) headerArray.get(0));
                        } else {
                            throw new BusinessException("不支持该contentType");
                        }
                    } catch (BusinessException e) {
                        throw new BusinessException("不支持该contentType");
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ParseException(e.getMessage());
                    }
                }
            }
        }
        return s;
    }
}
