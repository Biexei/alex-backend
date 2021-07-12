package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.common.Env;
import org.alex.platform.common.Result;
import org.alex.platform.core.http.ExecuteHandler;
import org.alex.platform.core.http.Request;
import org.alex.platform.core.parser.Parser;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.StabilityCaseLogMapper;
import org.alex.platform.mapper.StabilityCaseMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.MailService;
import org.alex.platform.service.ProjectService;
import org.alex.platform.service.StabilityCaseService;
import org.alex.platform.util.AssertUtil;
import org.alex.platform.util.NoUtil;
import org.alex.platform.util.ParseUtil;
import org.alex.platform.util.ValidUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    @Autowired
    MailService mailService;
    @Autowired
    StabilityCaseLogMapper stabilityCaseLogMapper;

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


    @Override
    public void executable(Integer id) throws BusinessException {

        // 最大可靠性测试数量为4
        Integer countExecuting = stabilityCaseLogMapper.countExecuting();
        if (countExecuting > 3) {
            throw new BusinessException("超过最大可执行任务：4");
        }

        // 不可重复创建相同任务
        Integer countCaseExecuting = stabilityCaseLogMapper.countExecutingByCaseId(id);
        if (countCaseExecuting > 0) {
            throw new BusinessException("当前任务已在执行中");
        }
    }

    /**
     * 执行稳定性测试用例
     * @param id 用例编还
     * @param executeId 执行人编号
     */
    @Override
    public void executeStabilityCaseById(Integer id, Integer executeId) throws BusinessException {

        StabilityCaseInfoVO stabilityCaseInfo = stabilityCaseMapper.selectStabilityCaseById(id);
        Byte protocol = stabilityCaseInfo.getProtocol();
        String desc = stabilityCaseInfo.getDesc();
        Integer caseId = stabilityCaseInfo.getCaseId();
        Byte executeType = stabilityCaseInfo.getExecuteType(); // 调度方式0执行总次数1截止至指定时间
        Integer step = stabilityCaseInfo.getStep();
        Integer executeTimes = stabilityCaseInfo.getExecuteTimes();
        Date executeEndTime = stabilityCaseInfo.getExecuteEndTime();
        boolean onErrorStop = stabilityCaseInfo.getOnErrorStop() == 0;
        boolean onFailedStop = stabilityCaseInfo.getOnFailedStop() == 0;
        String emailAddress = stabilityCaseInfo.getEmailAddress();
        Byte logRecordContent = stabilityCaseInfo.getLogRecordContent();
        Byte runEnv = stabilityCaseInfo.getRunEnv();

        String stabilityTestLogNo = NoUtil.genStabilityLogNo();
        String logPath = "src\\main\\resources\\stabilityLog\\" + stabilityTestLogNo + ".log";
        FileWriter fw;
        try {
            fw = new FileWriter(logPath);
        } catch (Exception e) {
            throw new BusinessException("可靠性测试日志创建失败");
        }

        this.log(fw, "Stability Test Starting...");
        this.log(fw, String.format("run environment was %s", runEnv));
        this.log(fw, "---------Stability Test Case Info---------");
        this.log(fw, stabilityCaseInfo.toString());
        this.log(fw, "---------Stability Test Case Info---------");

        // 写入执行日志
        Date exeDate = new Date();
        StabilityCaseLogDO stabilityCaseLogDO = new StabilityCaseLogDO();
        stabilityCaseLogDO.setStabilityTestLogNo(stabilityTestLogNo);
        stabilityCaseLogDO.setStabilityTestId(id);
        stabilityCaseLogDO.setStabilityTestDesc(desc);
        stabilityCaseLogDO.setLogPath(logPath);
        stabilityCaseLogDO.setStatus((byte)0); // 0进行中1停止2完成
        stabilityCaseLogDO.setCreatedTime(exeDate);
        stabilityCaseLogDO.setRunEnv(runEnv);
        stabilityCaseLogDO.setExecuteId(executeId);
        stabilityCaseLogMapper.insertStabilityCaseLog(stabilityCaseLogDO);
        Integer logId = stabilityCaseLogDO.getStabilityTestLogId();
        // 修改最近执行时间
        stabilityCaseMapper.updateStabilityCaseLastExecuteTime(id, exeDate);

        JSONObject response = new JSONObject(); //接收接口运行结果
        int executeStatus = 0; //接受每次发送请求的状态
        String executeMsg = "";
        int logStatus = 0; //确定最终记录的日志状态

        if (protocol == 0) { //http(s)
            InterfaceCaseInfoVO interfaceCaseInfoVO = interfaceCaseService.findInterfaceCaseByCaseId(caseId);
            this.log(fw, "---------Http Case Info---------");
            this.log(fw, interfaceCaseInfoVO.toString());
            this.log(fw, "---------Http Case Info---------");
            this.log(fw, "\r\n\r\n\n");

            // 获取调度方式
            if (executeType == 0) {
                for (int i = 1; i <= executeTimes; i++) {
                    this.log(fw, String.format("Current loop count: %s", i));
                    try {
                        response = this.doHttpRequest(interfaceCaseInfoVO, runEnv, logRecordContent, fw);
                    } catch (BusinessException e) {
                        response.put("executeStatus", 2);
                        response.put("executeMsg", e.getMessage());
                        this.log(fw, String.format("Request Error: %s", e.getMessage()));
                    }
                    executeStatus = response.getIntValue("executeStatus");
                    executeMsg = response.getString("executeMsg");
                    if (executeStatus == 1) {
                        if (onFailedStop) {
                            LOG.error("稳定性测试因执行失败终止");
                            LOG.error("Failed msg：{}", executeMsg);
                            this.log(fw, "稳定性测试因执行失败终止");
                            this.log(fw, String.format("Failed msg：%s", executeMsg));
                            logStatus = 1;
                            break;
                        }
                        this.log(fw, String.format("Warning msg：%s", executeMsg));
                    } else if (executeStatus == 2) {
                        if (onErrorStop) {
                            LOG.error("稳定性测试因执行错误终止");
                            LOG.error("Error msg：{}", executeMsg);
                            this.log(fw, "稳定性测试因执行错误终止");
                            this.log(fw, String.format("Error msg：%s", executeMsg));
                            logStatus = 2;
                            break;
                        }
                        this.log(fw, String.format("Warning msg：%s", executeMsg));
                    }
                    try {
                        Thread.sleep(step);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.log(fw, "\r\n\r\n\n");
                    try {
                        fw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                LOG.info("next version");
            }
        } else if (protocol == 1) { //ws(s)
            LOG.debug("next version");
        } else { //dubbo
            LOG.debug("next version");
        }
        this.log(fw, "Stability Test Ending...");
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发送邮件
        this.sendEmail(fw, emailAddress, executeStatus, executeMsg);
        // 写日志
        StabilityCaseLogDO logDO = new StabilityCaseLogDO();
        logDO.setStabilityTestLogId(logId);
        if (logStatus == 0) {
            logDO.setStatus((byte)2);
        } else {
            logDO.setStatus((byte)1);
        }
        stabilityCaseLogMapper.updateStabilityCaseLog(logDO);
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private JSONObject doHttpRequest(InterfaceCaseInfoVO interfaceCaseInfoVO, Byte runEnv, Byte logRecordContent, FileWriter fw) throws BusinessException {
        int executeStatus; //0成功 1失败 2错误
        String executeMsg;

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

            this.log(fw, String.format("Request url: %s", url));

            if (responseEntity == null) {
                executeStatus = 2;
                executeMsg = "请求错误";
            } else {
                Integer responseCode = Request.code(responseEntity);
                String responseHeaders = Request.headersPretty(responseEntity);
                String responseBody = Request.body(responseEntity);

                this.log(fw, String.format("Response code: %s", responseCode));
                if (logRecordContent != null) {
                    if (logRecordContent == 0) {
                        this.log(fw, String.format("Response headers: %s", responseHeaders));
                    } else if (logRecordContent == 1) {
                        this.log(fw, String.format("Response body: %s", responseBody));
                    } else {
                        this.log(fw, String.format("Response headers: %s", responseHeaders));
                        this.log(fw, String.format("Response body: %s", responseBody));
                    }
                }
                this.log(fw, String.format("Run time: %sms", runTime));

                List<InterfaceAssertVO> asserts = interfaceCaseInfoVO.getAsserts();
                JSONObject assertObject = this.doHttpAssert(asserts, responseCode, responseHeaders, responseBody, runTime);
                executeStatus = assertObject.getIntValue("assertStatus");
                executeMsg = assertObject.getString("assertMsg");
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
        JSONObject result = new JSONObject();
        result.put("executeStatus", executeStatus);
        result.put("executeMsg", executeMsg);
        return result;
    }

    @SuppressWarnings({"rawtypes"})
    private JSONObject doHttpAssert(List<InterfaceAssertVO> asserts, Integer responseCode, String responseHeaders, String responseBody, Long runTime) {

        int assertStatus; //0成功 1失败 2错误
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
                    assertMsg = e.getMessage();
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
                    assertMsg = String.format("断言未通过，预期结果为：%s，操作符为：%s，实际结果为：%s", exceptedResult,
                            this.getAssertOperatorByCode(operator), actualResult);
                }
            } catch (Exception e) {
                assertStatus = 2;
                assertMsg = e.getMessage();
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

    /**
     * 写日志
     * @param fw FileWriter
     * @param text text
     */
    private void log(FileWriter fw, String text) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (text.startsWith("\r\n")) {
                fw.write(text);
            } else {
                fw.write(format.format(new Date()) + ":   " + text + "\r\n");
            }
        } catch (IOException e) {
            LOG.error("稳定性测试写入日志失败，错误消息为{}", e.getMessage());
        }
    }

    /**
     * 根据断言码值获取断言操作符号
     * @param code 码值
     * @return 断言操作符号
     */
    private String getAssertOperatorByCode(Byte code) {
        switch (code) {
            case 0:
                return "=";
            case 1:
                return "<";
            case 2:
                return ">";
            case 3:
                return "<=";
            case 4:
                return ">=";
            case 5:
                return "in";
            case 6:
                return "!=";
            case 7:
                return "re";
            case 8:
                return "isNull";
            case 9:
                return "notNull";
            default:
                return "contains";
        }
    }

    /**
     * 发送邮件
     * @param fw FileWriter
     * @param emailAddress emailAddress
     * @param executeStatus executeStatus
     * @param executeMsg executeMsg
     */
    private void sendEmail(FileWriter fw, String emailAddress, int executeStatus, String executeMsg) {
        try {
            if (!StringUtils.isBlank(emailAddress)) {
                this.log(fw, "------开始发送邮件------");
                if (executeStatus == 0) {
                    mailService.send("稳定性测试成功", executeMsg, emailAddress);
                } else if (executeStatus == 1) {
                    mailService.send("稳定性测试失败", executeMsg, emailAddress);
                } else {
                    mailService.send("稳定性测试错误", executeMsg, emailAddress);
                }
                this.log(fw, "------成功发送邮件------");
            }
        } catch (BusinessException e) {
            LOG.error("Send email exception, {}", e.getMessage());
            this.log(fw, "------发送邮件失败------");
            this.log(fw, String.format("Send email exception, %s", e.getMessage()));
        }
    }

}
