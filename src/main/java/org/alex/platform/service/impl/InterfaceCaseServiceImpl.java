package org.alex.platform.service.impl;

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
            if (method == 0) { //get
                if (params == null || "".equals(params)) { // 若请求参数为空
                    responseEntity = RestUtil.get(url, headersMap);
                } else { // 若请求参数不为空
                    HashMap paramsMap = JSONObject.parseObject(params, HashMap.class);
                    responseEntity = RestUtil.get(url, headersMap, paramsMap);
                }
            } else if (method == 1) { //post
                if (StringUtils.isEmpty(json) && StringUtils.isEmpty(data)) {
                    throw new BusinessException("data/json只能任传其一");
                }
                if (StringUtils.isNotEmpty(json) && StringUtils.isNotEmpty(data)) {
                    throw new BusinessException("data/json只能任传其一");
                }
                if (StringUtils.isNotEmpty(params)) {
                    if (StringUtils.isNotEmpty(data)) {
                        HashMap paramsMap = JSONObject.parseObject(params, HashMap.class);
                        HashMap dataMap = JSONObject.parseObject(data, HashMap.class);
                        responseEntity = RestUtil.post(url, headersMap, paramsMap, dataMap);
                    } else {
                        new BusinessException("请求方式为post时，且params不为空时则data不能为空");
                    }
                } else {
                    if (StringUtils.isNotEmpty(data)) { // data为空 json不为空
                        HashMap dataMap = JSONObject.parseObject(data, HashMap.class);
                        responseEntity = RestUtil.postData(url, headersMap, dataMap);
                    } else { // data不为空 json为空
                        responseEntity = RestUtil.postJson(url, headersMap, json);
                    }
                }
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
            executeLogService.saveExecuteLog(executeLogDO);
        } else { //请求成功记录执行日志和断言日志
            // 只有接口调用未出现异常才统计code、header、body
            if (responseEntity != null) {
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
            }
        }
    }
}
