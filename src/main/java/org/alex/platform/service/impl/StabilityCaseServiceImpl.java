package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.common.Env;
import org.alex.platform.core.http.ExecuteHandler;
import org.alex.platform.core.http.Request;
import org.alex.platform.core.parser.Parser;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.StabilityCaseMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.ProjectService;
import org.alex.platform.service.StabilityCaseService;
import org.alex.platform.util.AssertUtil;
import org.alex.platform.util.ExceptionUtil;
import org.alex.platform.util.ParseUtil;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class StabilityCaseServiceImpl implements StabilityCaseService {

    private static final Logger LOG = LoggerFactory.getLogger(StabilityCaseServiceImpl.class);

    @Autowired
    StabilityCaseMapper stabilityCaseMapper;
    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    ProjectService projectService;
    @Autowired
    Env env;
    @Autowired
    ExecuteHandler executeHandler;
    @Autowired
    Parser parser;

    /**
     * 新增稳定性测试用例
     * @param stabilityCaseDO stabilityCaseDO
     * @throws ValidException ValidException
     */
    @Override
    public void saveStabilityCase(StabilityCaseDO stabilityCaseDO) throws ValidException {
        Date date = new Date();
        stabilityCaseDO.setCreatedTime(date);
        stabilityCaseDO.setUpdateTime(date);
        Byte executeType = stabilityCaseDO.getExecuteType(); // 调度方式0执行总次数   1截止至指定时间
        Byte protocol = stabilityCaseDO.getProtocol(); // 0http(s)1ws(s)2dubbo
        Integer caseId = stabilityCaseDO.getCaseId();
        if (executeType == 0) {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteTimes(), "请输入执行次数");
            stabilityCaseDO.setExecuteEndTime(null);
        } else {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteEndTime(), "请选择任务终止时间");
            stabilityCaseDO.setExecuteTimes(null);
        }
        if (protocol == 0) {
            ValidUtil.notNUll(interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId), "接口测试用例编号不存在");
        }
        stabilityCaseMapper.insertStabilityCase(stabilityCaseDO);
    }

    /**
     * 修改稳定性测试用例
     * @param stabilityCaseDO stabilityCaseDO
     * @throws ValidException ValidException
     */
    @Override
    public void modifyStabilityCase(StabilityCaseDO stabilityCaseDO) throws ValidException {
        stabilityCaseDO.setUpdateTime(new Date());
        Byte executeType = stabilityCaseDO.getExecuteType(); // 调度方式0执行总次数   1截止至指定时间
        Byte protocol = stabilityCaseDO.getProtocol(); // 0http(s)1ws(s)2dubbo
        Integer caseId = stabilityCaseDO.getCaseId();
        if (executeType == 0) {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteTimes(), "请输入执行次数");
            stabilityCaseDO.setExecuteEndTime(null);
        } else {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteEndTime(), "请选择任务终止时间");
            stabilityCaseDO.setExecuteTimes(null);
        }
        if (protocol == 0) {
            ValidUtil.notNUll(interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId), "接口测试用例编号不存在");
        }
        stabilityCaseMapper.updateStabilityCase(stabilityCaseDO);
    }

    /**
     * 查看稳定性测试用例详情
     * @param id id
     * @return StabilityCaseListVO
     */
    @Override
    public StabilityCaseInfoVO findStabilityCaseById(Integer id) {
        StabilityCaseInfoVO stabilityCaseInfoVO = stabilityCaseMapper.selectStabilityCaseById(id);
        Byte protocol = stabilityCaseInfoVO.getProtocol();
        Integer caseId = stabilityCaseInfoVO.getCaseId();
        if (protocol == 0) { //http(s)
            InterfaceCaseInfoVO interfaceCaseInfoVO = interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId);
            if (interfaceCaseInfoVO != null) {
                stabilityCaseInfoVO.setCaseDesc(interfaceCaseInfoVO.getDesc());
            }
        }
        return stabilityCaseInfoVO;
    }

    /**
     * 查看稳定性测试用例列表
     * @param stabilityCaseDTO stabilityCaseDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo<StabilityCaseListVO>
     */
    @Override
    public PageInfo<StabilityCaseListVO> findStabilityCaseList(StabilityCaseDTO stabilityCaseDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(stabilityCaseMapper.selectStabilityCaseList(stabilityCaseDTO));
    }

    /**
     * 删除稳定性测试用例
     * @param id id
     */
    @Override
    public void removeStabilityCaseById(Integer id) {
        stabilityCaseMapper.deleteStabilityCaseById(id);
    }

    /**
     * 执行稳定性测试用例
     * @param id 用例编还
     */
    @Override
    public void executeStabilityCaseById(Integer id) {
        StabilityCaseInfoVO stabilityCaseInfo = stabilityCaseMapper.selectStabilityCaseById(id);
        Byte protocol = stabilityCaseInfo.getProtocol();
        Integer caseId = stabilityCaseInfo.getCaseId();
        Byte executeType = stabilityCaseInfo.getExecuteType(); // 调度方式0执行总次数1截止至指定时间
        Integer step = stabilityCaseInfo.getStep();
        Integer executeTimes = stabilityCaseInfo.getExecuteTimes();
        Date executeEndTime = stabilityCaseInfo.getExecuteEndTime();
        Byte onErrorStop = stabilityCaseInfo.getOnErrorStop();
        Byte onFailedStop = stabilityCaseInfo.getOnFailedStop();
        String emailAddress = stabilityCaseInfo.getEmailAddress();
        Byte logRecordContent = stabilityCaseInfo.getLogRecordContent();

        if (protocol == 0) { //http(s)
            InterfaceCaseInfoVO interfaceCaseInfoVO = interfaceCaseService.findInterfaceCaseByCaseId(caseId);
        } else if (protocol == 1) { //ws(s)

        } else { //dubbo

        }
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private void doHttpRequest(InterfaceCaseInfoVO interfaceCaseInfoVO, Byte runEnv) throws BusinessException {
        int executeStatus = 0; //0成功 1失败 2错误
        String executeMsg = "";

        String url;
        long runTime;
        if (interfaceCaseInfoVO == null) {
            throw new BusinessException("用例信息不存在");
        }
        Integer projectId = interfaceCaseInfoVO.getProjectId();
        if (projectId != null) {
            ProjectVO projectVO = projectService.findProjectById(projectId);
            url = env.domain(projectVO, runEnv) + interfaceCaseInfoVO.getUrl();
        } else {
            url = interfaceCaseInfoVO.getUrl();
        }

        Byte bodyType = interfaceCaseInfoVO.getBodyType();
        String headers = executeHandler.kvCast(interfaceCaseInfoVO.getHeaders());
        String params = executeHandler.kvCast(interfaceCaseInfoVO.getParams());
        String formData = executeHandler.kvCast(interfaceCaseInfoVO.getFormData());
        String formDataEncoded = executeHandler.kvCast(interfaceCaseInfoVO.getFormDataEncoded());
        String raw = interfaceCaseInfoVO.getRaw();
        String rawType = interfaceCaseInfoVO.getRawType();
        Byte method = interfaceCaseInfoVO.getMethod();

        // 发送请求
        ResponseEntity responseEntity;

        try {
            // 解析headers中的依赖
            headers = parser.parseDependency(headers, null, null, (byte) 1, null, null, null, null, null);
            // 解析params中的依赖
            params = parser.parseDependency(params, null, null, (byte) 1, null, null, null, null, null);
            if (bodyType == 0) { //form-data
                formData = parser.parseDependency(formData, null, null, (byte) 1, null, null, null, null, null);
            } else if (bodyType == 1) { //x-www-form-encoded
                formDataEncoded = parser.parseDependency(formDataEncoded, null, null, (byte) 1, null, null, null, null, null);
            } else if (bodyType == 2) { //raw
                raw = parser.parseDependency(raw, null, null, (byte) 1, null, null, null, null, null);
            } else if (bodyType == 9) { //none
                LOG.info("bodyType is none, not parse the dependency");
            } else {
                throw new BusinessException("not supported the bodyType");
            }

            // 类型转换
            HashMap headersMap = JSONObject.parseObject(headers, HashMap.class);
            HashMap paramsMap = JSONObject.parseObject(params, HashMap.class);
            HashMap formDataMap = JSONObject.parseObject(formData, HashMap.class);
            HashMap formDataEncodedMap = JSONObject.parseObject(formDataEncoded, HashMap.class);

            // 处理URL参数
            HashMap<String, Object> urlParamsWrapper = Request.pathVariableParser(url, paramsMap);
            url = (String) urlParamsWrapper.get("url");
            paramsMap = (HashMap<String, String>) urlParamsWrapper.get("params");

            // 确定请求方式
            HttpMethod methodEnum = executeHandler.httpMethod(method);

            // 发送请求
            long start = System.currentTimeMillis();
            responseEntity = executeHandler.doRequest(bodyType, methodEnum, url, headersMap, paramsMap, formDataMap, formDataEncodedMap, raw, rawType);
            runTime = System.currentTimeMillis() - start;

            if (responseEntity == null) {
                executeStatus = 2;
                executeMsg = "请求错误";
            } else {
                Integer responseCode = Request.code(responseEntity);
                String responseHeaders = Request.headersPretty(responseEntity);
                String responseBody = Request.body(responseEntity);
                List<InterfaceAssertVO> asserts = interfaceCaseInfoVO.getAsserts();
                this.doAssert(asserts, responseCode, responseHeaders, responseBody, runTime);
            }
        } catch (ResourceAccessException e) {
            executeStatus = 2;
            e.printStackTrace();
            executeMsg = "connection timed out, try to check timeout setting and proxy server";
        } catch (Exception e) {
            executeStatus = 2;
            e.printStackTrace();
            executeMsg = e.getMessage();
        }
    }

    @SuppressWarnings({"rawtypes"})
    private JSONObject doAssert(List<InterfaceAssertVO> asserts, Integer responseCode, String responseHeaders, String responseBody, Long runTime) {

        int assertStatus = 0; //0成功 1失败 2错误
        String assertMsg = null;
        List<Integer> statusList = new ArrayList<>(); // 用来接收用例每个断言的结果

        for (InterfaceAssertVO interfaceAssertVO : asserts) {
            Byte type = interfaceAssertVO.getType();
            String expression = interfaceAssertVO.getExpression();
            Byte operator = interfaceAssertVO.getOperator();
            String exceptedResult = interfaceAssertVO.getExceptedResult();

            if (null != exceptedResult) {
                try {
                    exceptedResult = parser.parseDependency(exceptedResult, null, null, (byte) 1, null, null, null, null, null);
                } catch (ParseException | BusinessException | SqlException e) {
                    assertMsg = assertMsg + e.getMessage();
                    assertStatus = 2;
                    statusList.add(assertStatus);
                    continue;
                }
            }

            // 执行断言
            String actualResult;
            ArrayList resultList = null;
            boolean isPass;
            try {
                if (type == 0) { // json
                    actualResult = ParseUtil.parseJson(responseBody, expression);
                    resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                } else if (type == 1) { // html
                    actualResult = ParseUtil.parseXml(responseBody, expression);
                    resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                } else if (type == 2) { // header
                    actualResult = ParseUtil.parseJson(responseHeaders, expression);
                    resultList = JSONObject.parseObject(actualResult, ArrayList.class);
                } else if (type == 3) { // responseCode
                    actualResult = String.valueOf(responseCode);
                } else if (type == 4) { // runtime
                    actualResult = String.valueOf(runTime);
                } else {
                    throw new BusinessException("not supported assert type");
                }

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
                    assertStatus = 0;
                } else {
                    assertStatus = 1;
                    assertMsg = assertMsg + String.format("断言未通过，预期结果为：%s，操作符为：%s，实际结果为：%s", exceptedResult, operator, actualResult);
                }
            } catch (Exception e) {
                assertStatus = 2;
                assertMsg = assertMsg + e.getMessage();
            }
            statusList.add(assertStatus); // 将每次断言status都加入集合
        }

        if (statusList.contains(2)) {
            assertStatus = 2;
        } else {
            if (!statusList.contains(1)) {
                assertStatus = 0;
            } else {
                assertStatus = 1;
            }
        }
        JSONObject result = new JSONObject();
        result.put("assertStatus", assertStatus);
        result.put("assertMsg", assertMsg);
        return result;
    }
}
