package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.*;
import org.alex.platform.util.AssertUtil;
import org.alex.platform.util.ParseUtil;
import org.alex.platform.util.RestUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public Integer executeInterfaceCase(Integer interfaceCaseId) {
        String exceptionMessage = null;
        // 运行结果 0成功 1失败 2错误
        Byte caseStatus = 0;

        // 1.获取case详情
        InterfaceCaseInfoVO interfaceCaseInfoVO = this.findInterfaceCaseByCaseId(interfaceCaseId);
        Integer projectId = interfaceCaseInfoVO.getProjectId();
        String url = projectService.findModulesById(projectId).getDomain() + interfaceCaseInfoVO.getUrl();
        String desc = interfaceCaseInfoVO.getDesc();
        String headers = interfaceCaseInfoVO.getHeaders();
        String params = interfaceCaseInfoVO.getParams();
        String data = interfaceCaseInfoVO.getData();
        String json = interfaceCaseInfoVO.getJson();
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
                String exceptedResult = interfaceAssertVO.getExceptedResult();
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

    public void parseRelyData(String s) throws BusinessException, ParseException {
        // 1.检查url/headers/params/data/json/assertList是否存在${xx}格式
        Pattern p = Pattern.compile("\\$\\{.+\\}");// "\\$\\{[a-zA-Z]+\\}"
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            String findStr = matcher.group();
            // 获取relyName
            String relyName = findStr.substring(2, findStr.length()-1);
            // 查询其所依赖的caseId
            InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
            interfaceCaseRelyDataDTO.setRelyName(relyName);
            InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
            Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
            // 根据caseId调用相应case
            Integer executeLogId = this.executeInterfaceCase(caseId);
            // 获取case执行结果, 不等于0, 则用例未通过
            if (executeLogService.findExecute(executeLogId).getStatus() != 0){
                throw new BusinessException("relyName关联的前置用例执行失败!");
            }
            // 根据executeLogId查询对应的执行记录
            InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
            String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
            String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
            // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
            int contentType = (int)interfaceCaseRelyDataVO.getContentType();
            String expression = interfaceCaseRelyDataVO.getExtractExpression();
            try {
                if (contentType == 0) { // json
                    System.out.println(JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class).get(0));
                } else if (contentType == 1) { // html
                    System.out.println(JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class).get(0));
                } else if (contentType == 2) { // headers
                    // 支持cookie[0]格式，即可对数组指定下标取值
                    Pattern pp = Pattern.compile("\\[[0-9]+\\]");
                    Matcher mm = pp.matcher(expression);
                    int a = mm.groupCount();
                    if (mm.groupCount() > 1) {
                        throw new ParseException("headers提取表达式解析异常");
                    }
                    while (mm.find()) {
                        String indexStr = mm.group();
                        int index = Integer.parseInt(indexStr.substring(1, indexStr.length()-1));
                        JSONArray headerList = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(indexStr.substring(1, indexStr.length()-1));
                        System.out.println(headerList.get(index));
                    }
                    JSONArray headerList = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(expression);
                    System.out.println(headerList.get(0));
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
