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
import org.alex.platform.mapper.StabilityCaseLogMapper;
import org.alex.platform.mapper.StabilityCaseMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.MailService;
import org.alex.platform.service.ProjectService;
import org.alex.platform.service.StabilityCaseService;
import org.alex.platform.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class StabilityCaseServiceImpl implements StabilityCaseService {
    @Value("${myself.path.stability-log-path}")
    private String stabilityLogPath;

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
    @Autowired
    RedisUtil redisUtil;

    /**
     * ???????????????????????????
     * @param stabilityCaseDO stabilityCaseDO
     * @throws ValidException ValidException
     */
    @Override
    public void saveStabilityCase(StabilityCaseDO stabilityCaseDO) throws ValidException {
        Date date = new Date();
        stabilityCaseDO.setCreatedTime(date);
        stabilityCaseDO.setUpdateTime(date);
        Byte executeType = stabilityCaseDO.getExecuteType(); // ????????????0???????????????   1?????????????????????
        Byte protocol = stabilityCaseDO.getProtocol(); // 0http(s)1ws(s)2dubbo
        Integer caseId = stabilityCaseDO.getCaseId();
        if (executeType == 0) {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteTimes(), "?????????????????????");
            stabilityCaseDO.setExecuteEndTime(null);
        } else {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteEndTime(), "???????????????????????????");
            stabilityCaseDO.setExecuteTimes(null);
        }
        if (protocol == 0) {
            ValidUtil.notNUll(interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId), "?????????????????????????????????");
        }
        stabilityCaseMapper.insertStabilityCase(stabilityCaseDO);
    }

    /**
     * ???????????????????????????
     * @param stabilityCaseDO stabilityCaseDO
     * @throws ValidException ValidException
     */
    @Override
    public void modifyStabilityCase(StabilityCaseDO stabilityCaseDO) throws ValidException {
        stabilityCaseDO.setUpdateTime(new Date());
        Byte executeType = stabilityCaseDO.getExecuteType(); // ????????????0???????????????   1?????????????????????
        Byte protocol = stabilityCaseDO.getProtocol(); // 0http(s)1ws(s)2dubbo
        Integer caseId = stabilityCaseDO.getCaseId();
        if (executeType == 0) {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteTimes(), "?????????????????????");
            stabilityCaseDO.setExecuteEndTime(null);
        } else {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteEndTime(), "???????????????????????????");
            stabilityCaseDO.setExecuteTimes(null);
        }
        if (protocol == 0) {
            ValidUtil.notNUll(interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId), "?????????????????????????????????");
        }
        stabilityCaseMapper.updateStabilityCase(stabilityCaseDO);
    }

    /**
     * ?????????????????????????????????
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
     * ?????????????????????????????????
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
     * ???????????????????????????
     * @param id id
     */
    @Override
    public void removeStabilityCaseById(Integer id) {
        stabilityCaseMapper.deleteStabilityCaseById(id);
    }


    @Override
    public void executable(Integer id) throws BusinessException {

        // ??????????????????????????????2
        Integer countExecuting = stabilityCaseLogMapper.countExecuting();
        if (countExecuting > 1) {
            throw new BusinessException("??????????????????????????????2");
        }

        // ??????????????????????????????
        Integer countCaseExecuting = stabilityCaseLogMapper.countExecutingByCaseId(id);
        if (countCaseExecuting > 0) {
            throw new BusinessException("???????????????????????????");
        }
    }

    /**
     * ???????????????????????????
     * @param stabilityTestLogId stabilityTestLogId
     * @param executeId executeId
     * @throws BusinessException BusinessException
     */
    @Override
    public void stopStabilityCaseByLogId(Integer stabilityTestLogId, Integer executeId) throws BusinessException {
        StabilityCaseLogInfoVO stabilityCaseLogInfoVO = stabilityCaseLogMapper.selectStabilityCaseLogById(stabilityTestLogId);
        if (stabilityCaseLogInfoVO != null) {
            String stabilityTestLogNo = stabilityCaseLogInfoVO.getStabilityTestLogNo();
            Byte status = stabilityCaseLogInfoVO.getStatus();
            if (status != null) {
                if (status == 0) {
                    this.setStabilityTestStatus(stabilityTestLogNo, false);
                    // ????????????????????????????????????1
                    StabilityCaseLogDO stabilityCaseLogDO = new StabilityCaseLogDO();
                    stabilityCaseLogDO.setStatus((byte)1);
                    stabilityCaseLogDO.setStabilityTestLogId(stabilityTestLogId);
                    stabilityCaseLogMapper.updateStabilityCaseLog(stabilityCaseLogDO);
                    LOG.warn("????????????????????????????????????????????????[{}], ????????????[{}]", stabilityTestLogId, executeId);
                }
            }
        } else {
            throw new BusinessException("?????????????????????");
        }
    }

    /**
     * ???????????????????????????
     * @param id ????????????
     * @param executeId ???????????????
     */
    @Override
    public void executeStabilityCaseById(Integer id, Integer executeId) throws BusinessException {

        StabilityCaseInfoVO stabilityCaseInfo = stabilityCaseMapper.selectStabilityCaseById(id);
        Byte protocol = stabilityCaseInfo.getProtocol();
        String desc = stabilityCaseInfo.getDesc();
        Integer caseId = stabilityCaseInfo.getCaseId();
        Byte executeType = stabilityCaseInfo.getExecuteType(); // ????????????0???????????????1?????????????????????
        Integer step = stabilityCaseInfo.getStep();
        Integer executeTimes = stabilityCaseInfo.getExecuteTimes();
        Date executeEndTime = stabilityCaseInfo.getExecuteEndTime();
        boolean onErrorStop = stabilityCaseInfo.getOnErrorStop() == 0;
        boolean onFailedStop = stabilityCaseInfo.getOnFailedStop() == 0;
        String emailAddress = stabilityCaseInfo.getEmailAddress();
        Byte logRecordContent = stabilityCaseInfo.getLogRecordContent();
        Byte runEnv = stabilityCaseInfo.getRunEnv();

        String stabilityTestLogNo = NoUtil.genStabilityLogNo();
        String logBasePath = stabilityLogPath;
        File file = new File(logBasePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String logPath = logBasePath + stabilityTestLogNo + ".log";
        FileWriter fw;
        try {
            fw = new FileWriter(logPath);
        } catch (Exception e) {
            throw new BusinessException("?????????????????????????????????");
        }

        this.log(fw, stabilityTestLogNo,"Stability Test Starting...");
        this.log(fw, stabilityTestLogNo, String.format("Run environment was %s", runEnv));
        this.log(fw, stabilityTestLogNo, "---------Stability Test Case Info---------");
        this.log(fw, stabilityTestLogNo, stabilityCaseInfo.toString());
        this.log(fw, stabilityTestLogNo, "---------Stability Test Case Info---------");

        // ??????????????????
        Date exeDate = new Date();
        StabilityCaseLogDO stabilityCaseLogDO = new StabilityCaseLogDO();
        stabilityCaseLogDO.setStabilityTestLogNo(stabilityTestLogNo);
        stabilityCaseLogDO.setStabilityTestId(id);
        stabilityCaseLogDO.setStabilityTestDesc(desc);
        stabilityCaseLogDO.setLogPath(logPath);
        stabilityCaseLogDO.setStatus((byte)0); // 0?????????1??????2??????
        stabilityCaseLogDO.setCreatedTime(exeDate);
        stabilityCaseLogDO.setRunEnv(runEnv);
        stabilityCaseLogDO.setExecuteId(executeId);
        stabilityCaseLogMapper.insertStabilityCaseLog(stabilityCaseLogDO);
        Integer logId = stabilityCaseLogDO.getStabilityTestLogId();

        // ????????????????????????????????????????????????
        this.setStabilityTestStatus(stabilityTestLogNo, true);

        // ????????????????????????
        stabilityCaseMapper.updateStabilityCaseLastExecuteTime(id, exeDate);

        JSONObject response = new JSONObject(); //????????????????????????
        int executeStatus = 0; //?????????????????????????????????
        String executeMsg = "";
        int logStatus = 0; //?????????????????????????????????
        int loopCount = 0; //??????????????????
        int successCount = 0; //??????????????????
        int failedCount = 0; //??????????????????
        int errorCount = 0; //??????????????????
        int warningCount = 0; //?????????????????????????????????????????????????????????????????????????????????

        if (protocol == 0) { //http(s)
            InterfaceCaseInfoVO interfaceCaseInfoVO = interfaceCaseService.findInterfaceCaseByCaseId(caseId);
            this.log(fw, stabilityTestLogNo, "---------Http Case Info---------");
            this.log(fw, stabilityTestLogNo, interfaceCaseInfoVO.toString());
            this.log(fw, stabilityTestLogNo, "---------Http Case Info---------");
            if (executeType == 0) {
                this.log(fw, stabilityTestLogNo, String.format("Dispatch type???%s", "???????????????"));
                this.log(fw, stabilityTestLogNo, String.format("The estimation of the request times???%s", executeTimes));
            } else {
                this.log(fw, stabilityTestLogNo, String.format("Dispatch type???%s", "???????????????"));
                this.log(fw, stabilityTestLogNo, String.format("The estimation of the end time???%s",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executeEndTime)));
            }
            this.log(fw, stabilityTestLogNo, "\r\n\r\n\n");

            // ??????????????????
            if (executeType == 0) {
                for (int i = 1; i <= executeTimes; i++) {
                    if (this.getStabilityTestIsRunning(stabilityTestLogNo)) {
                        this.log(fw, stabilityTestLogNo, String.format("Current loop count: %s", ++loopCount));
                        try {
                            response = this.doHttpRequest(interfaceCaseInfoVO, runEnv, logRecordContent, fw, stabilityTestLogNo, exeDate.getTime());
                        } catch (BusinessException e) {
                            response.put("executeStatus", 2);
                            response.put("executeMsg", e.getMessage());
                            this.log(fw, stabilityTestLogNo, String.format("Request Error: %s", e.getMessage()));
                        }
                        executeStatus = response.getIntValue("executeStatus");
                        executeMsg = response.getString("executeMsg");
                        if (executeStatus == 1) {
                            if (onFailedStop) {
                                ++failedCount;
                                LOG.error("????????????????????????????????????");
                                LOG.error("Failed msg???{}", executeMsg);
                                this.log(fw, stabilityTestLogNo, "????????????????????????????????????");
                                this.log(fw, stabilityTestLogNo, String.format("Failed msg???%s", executeMsg));
                                logStatus = 1;
                                // ????????????????????????????????????????????????
                                this.setStabilityTestStatus(stabilityTestLogNo, false);
                                break;
                            }
                            ++warningCount;
                            this.log(fw, stabilityTestLogNo, String.format("Warning[Failed] msg???%s", executeMsg));
                        } else if (executeStatus == 2) {
                            if (onErrorStop) {
                                ++errorCount;
                                LOG.error("????????????????????????????????????");
                                LOG.error("Error msg???{}", executeMsg);
                                this.log(fw, stabilityTestLogNo, "????????????????????????????????????");
                                this.log(fw, stabilityTestLogNo, String.format("Error msg???%s", executeMsg));
                                logStatus = 2;
                                // ????????????????????????????????????????????????
                                this.setStabilityTestStatus(stabilityTestLogNo, false);
                                break;
                            }
                            ++warningCount;
                            this.log(fw, stabilityTestLogNo, String.format("Warning[Error] msg???%s", executeMsg));
                        }
                        try {
                            Thread.sleep(step*1000);
                        } catch (InterruptedException e) {
                            ++errorCount;
                            LOG.error("????????????????????????????????????");
                            LOG.error("Exception msg???{}", e.getMessage());
                            this.log(fw, stabilityTestLogNo, "????????????????????????????????????");
                            this.log(fw, stabilityTestLogNo, String.format("Exception msg???%s", executeMsg));
                            logStatus = 2;
                            e.printStackTrace();
                            // ????????????????????????????????????????????????
                            this.setStabilityTestStatus(stabilityTestLogNo, false);
                            break;
                        }
                        this.log(fw, stabilityTestLogNo, "\r\n\r\n\n");
                        try {
                            fw.flush();
                        } catch (IOException e) {
                            ++errorCount;
                            LOG.error("????????????????????????????????????");
                            LOG.error("Exception msg???{}", e.getMessage());
                            this.log(fw, stabilityTestLogNo, "????????????????????????????????????");
                            this.log(fw, stabilityTestLogNo, String.format("Exception msg???%s", executeMsg));
                            logStatus = 2;
                            e.printStackTrace();
                            // ????????????????????????????????????????????????
                            this.setStabilityTestStatus(stabilityTestLogNo, false);
                            break;
                        }
                    } else {
                        logStatus = 1;
                        this.log(fw, stabilityTestLogNo, String.format("?????????????????????[%s]????????????", executeId));
                        break;
                    }
                    ++successCount;
                }
                // ????????????????????????????????????????????????
                this.setStabilityTestStatus(stabilityTestLogNo, false);
            } else {
                long end = TimeUtil.date2timestamp(executeEndTime);
                while (System.currentTimeMillis() <= end) {
                    if (this.getStabilityTestIsRunning(stabilityTestLogNo)) {
                        this.log(fw, stabilityTestLogNo, String.format("Current loop count: %s", ++loopCount));
                        try {
                            response = this.doHttpRequest(interfaceCaseInfoVO, runEnv, logRecordContent, fw, stabilityTestLogNo, exeDate.getTime());
                        } catch (BusinessException e) {
                            response.put("executeStatus", 2);
                            response.put("executeMsg", e.getMessage());
                            this.log(fw, stabilityTestLogNo, String.format("Request Error: %s", e.getMessage()));
                        }
                        executeStatus = response.getIntValue("executeStatus");
                        executeMsg = response.getString("executeMsg");
                        if (executeStatus == 1) {
                            if (onFailedStop) {
                                ++failedCount;
                                LOG.error("????????????????????????????????????");
                                LOG.error("Failed msg???{}", executeMsg);
                                this.log(fw, stabilityTestLogNo, "????????????????????????????????????");
                                this.log(fw, stabilityTestLogNo, String.format("Failed msg???%s", executeMsg));
                                logStatus = 1;
                                // ????????????????????????????????????????????????
                                this.setStabilityTestStatus(stabilityTestLogNo, false);
                                break;
                            }
                            ++warningCount;
                            this.log(fw, stabilityTestLogNo, String.format("Warning[Failed] msg???%s", executeMsg));
                        } else if (executeStatus == 2) {
                            if (onErrorStop) {
                                ++errorCount;
                                LOG.error("????????????????????????????????????");
                                LOG.error("Error msg???{}", executeMsg);
                                this.log(fw, stabilityTestLogNo, "????????????????????????????????????");
                                this.log(fw, stabilityTestLogNo, String.format("Error msg???%s", executeMsg));
                                logStatus = 2;
                                // ????????????????????????????????????????????????
                                this.setStabilityTestStatus(stabilityTestLogNo, false);
                                break;
                            }
                            ++warningCount;
                            this.log(fw, stabilityTestLogNo, String.format("Warning[Error] msg???%s", executeMsg));
                        }
                        try {
                            Thread.sleep(step*1000);
                        } catch (InterruptedException e) {
                            ++errorCount;
                            LOG.error("????????????????????????????????????");
                            LOG.error("Exception msg???{}", e.getMessage());
                            this.log(fw, stabilityTestLogNo, "????????????????????????????????????");
                            this.log(fw, stabilityTestLogNo, String.format("Exception msg???%s", executeMsg));
                            logStatus = 2;
                            e.printStackTrace();
                            // ????????????????????????????????????????????????
                            this.setStabilityTestStatus(stabilityTestLogNo, false);
                            break;
                        }
                        this.log(fw, stabilityTestLogNo, "\r\n\r\n\n");
                        try {
                            fw.flush();
                        } catch (IOException e) {
                            ++errorCount;
                            LOG.error("????????????????????????????????????");
                            LOG.error("Exception msg???{}", e.getMessage());
                            this.log(fw, stabilityTestLogNo, "????????????????????????????????????");
                            this.log(fw, stabilityTestLogNo, String.format("Exception msg???%s", executeMsg));
                            logStatus = 2;
                            e.printStackTrace();
                            // ????????????????????????????????????????????????
                            this.setStabilityTestStatus(stabilityTestLogNo, false);
                            break;
                        }
                    } else {
                        logStatus = 1;
                        this.log(fw, stabilityTestLogNo, String.format("?????????????????????[%s]????????????", executeId));
                        break;
                    }
                    ++successCount;
                }
            }


        } else if (protocol == 1) { //ws(s)
            LOG.debug("next version");
        } else { //dubbo
            LOG.debug("next version");
        }
        this.log(fw, stabilityTestLogNo, "Stability Test Ending...");
        this.log(fw, stabilityTestLogNo, "\r\n");
        this.log(fw, stabilityTestLogNo, "---------Test Report---------");
        if (executeType == 0) {
            this.log(fw, stabilityTestLogNo, String.format("???????????????%s", "???????????????"));
            this.log(fw, stabilityTestLogNo, String.format("?????????????????????%s", executeTimes));
        } else {
            this.log(fw, stabilityTestLogNo, String.format("???????????????%s", "???????????????"));
            this.log(fw, stabilityTestLogNo, String.format("?????????????????????%s", TimeUtil.format(executeEndTime)));
        }
        String startTimeS = TimeUtil.format(exeDate);
        Date endTime = new Date();
        String endTimeS = TimeUtil.format(endTime);
        this.log(fw, stabilityTestLogNo, "\r\n");
        this.log(fw, stabilityTestLogNo, String.format("?????????????????????%s", startTimeS));
        this.log(fw, stabilityTestLogNo, String.format("?????????????????????%s", endTimeS));
        this.log(fw, stabilityTestLogNo, "\r\n");
        this.log(fw, stabilityTestLogNo, String.format("???????????????%s", loopCount));
        this.log(fw, stabilityTestLogNo, String.format("???????????????%s", warningCount));
        this.log(fw, stabilityTestLogNo, String.format("???????????????%s", successCount));
        this.log(fw, stabilityTestLogNo, String.format("???????????????%s", failedCount));
        this.log(fw, stabilityTestLogNo, String.format("???????????????%s", errorCount));
        this.log(fw, stabilityTestLogNo, "---------Test Report---------");
        // ????????????
        this.sendEmail(fw, emailAddress, executeStatus, executeMsg, stabilityTestLogNo);
        // ?????????
        StabilityCaseLogDO logDO = new StabilityCaseLogDO();
        logDO.setStabilityTestLogId(logId);
        logDO.setStartTime(exeDate);
        logDO.setEndTime(endTime);
        logDO.setRequestCount(loopCount);
        logDO.setSuccessCount(successCount);
        logDO.setWarningCount(warningCount);
        logDO.setFailedCount(failedCount);
        logDO.setErrorCount(errorCount);
        logDO.setResponseTimeQueue(this.getResponseTimeQueue(stabilityTestLogNo).toJSONString());
        // ????????????
        this.delResponseTimeQueue(stabilityTestLogNo);
        if (logStatus == 1) {
            logDO.setStatus((byte) 1);
        } else {
            logDO.setStatus((byte) 2);
        }
        stabilityCaseLogMapper.updateStabilityCaseLog(logDO);
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????????-http
     * @param interfaceCaseInfoVO interfaceCaseInfoVO
     * @param runEnv ????????????
     * @param logRecordContent ??????????????????
     * @param fw ?????????
     * @param stabilityTestLogNo ????????????
     * @param startTime ???????????????????????????????????????
     * @return ????????????
     * @throws BusinessException BusinessException
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    private JSONObject doHttpRequest(InterfaceCaseInfoVO interfaceCaseInfoVO,
                                     Byte runEnv, Byte logRecordContent, FileWriter fw, 
                                     String stabilityTestLogNo, long startTime) throws BusinessException {
        int executeStatus; //0?????? 1?????? 2??????
        String executeMsg;

        String url;
        long runTime;
        if (interfaceCaseInfoVO == null) {
            throw new BusinessException("?????????????????????");
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

        // ????????????
        ResponseEntity responseEntity;

        try {
            // ??????headers????????????
            headers = parser.parseDependency(headers, null, null, (byte) 1, null, null, null, null, null);
            // ??????params????????????
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

            // ????????????
            HashMap headersMap = JSONObject.parseObject(headers, HashMap.class);
            HashMap paramsMap = JSONObject.parseObject(params, HashMap.class);
            HashMap formDataMap = JSONObject.parseObject(formData, HashMap.class);
            HashMap formDataEncodedMap = JSONObject.parseObject(formDataEncoded, HashMap.class);

            // ??????URL??????
            HashMap<String, Object> urlParamsWrapper = Request.pathVariableParser(url, paramsMap);
            url = (String) urlParamsWrapper.get("url");
            paramsMap = (HashMap<String, String>) urlParamsWrapper.get("params");

            // ??????????????????
            HttpMethod methodEnum = executeHandler.httpMethod(method);

            // ????????????
            long start = System.currentTimeMillis();
            responseEntity = executeHandler.doRequest(bodyType, methodEnum, url, headersMap, paramsMap, formDataMap, formDataEncodedMap, raw, rawType);
            runTime = System.currentTimeMillis() - start;

            this.log(fw, stabilityTestLogNo, String.format("Request url: %s", url));

            if (responseEntity == null) {
                executeStatus = 2;
                executeMsg = "????????????";
            } else {
                Integer responseCode = Request.code(responseEntity);
                String responseHeaders = Request.headersPretty(responseEntity);
                String responseBody = Request.body(responseEntity);

                this.log(fw, stabilityTestLogNo, String.format("Response code: %s", responseCode));
                if (logRecordContent != null) {
                    if (logRecordContent == 0) {
                        this.log(fw, stabilityTestLogNo, String.format("Response headers: %s", responseHeaders));
                    } else if (logRecordContent == 1) {
                        this.log(fw, stabilityTestLogNo, String.format("Response body: %s", responseBody));
                    } else {
                        this.log(fw, stabilityTestLogNo, String.format("Response headers: %s", responseHeaders));
                        this.log(fw, stabilityTestLogNo, String.format("Response body: %s", responseBody));
                    }
                }
                this.log(fw, stabilityTestLogNo, String.format("Run time: %sms", runTime));
                this.setResponseTimeQueue(stabilityTestLogNo, runTime, System.currentTimeMillis()-startTime);

                List<InterfaceAssertVO> asserts = interfaceCaseInfoVO.getAsserts();
                JSONObject assertObject = this.doHttpAssert(asserts, responseCode, responseHeaders, responseBody, runTime);
                executeStatus = assertObject.getIntValue("assertStatus");
                executeMsg = assertObject.getString("assertMsg");
            }
        } catch (ResourceAccessException e) {
            executeStatus = 2;
            e.printStackTrace();
            executeMsg = "connection timed out, try to check timeout setting and proxy server, " + e.getMessage();
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

        int assertStatus; //0?????? 1?????? 2??????
        String assertMsg = null;
        List<Integer> statusList = new ArrayList<>(); // ???????????????????????????????????????

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

            // ????????????
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
                    assertMsg = String.format("??????????????????????????????[%s]????????????[%s]???????????????[%s]", exceptedResult,
                            this.getAssertOperatorByCode(operator), actualResult);
                }
            } catch (Exception e) {
                assertStatus = 2;
                assertMsg = e.getMessage();
            }
            statusList.add(assertStatus); // ???????????????status???????????????
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
     * ?????????
     * @param fw FileWriter
     * @param stabilityTestLogNo stabilityTestLogNo
     * @param text text
     */
    private void log(FileWriter fw, String stabilityTestLogNo, String text) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String s;
            if (text.startsWith("\r\n")) {
                s = text;
            } else {
                s = format.format(new Date()) + ":   " + text + "\r\n";
            }
            fw.write(s);
            this.setLogRedisList(stabilityTestLogNo, s);
        } catch (IOException e) {
            LOG.error("???????????????????????????????????????????????????{}", e.getMessage());
        }
    }

    /**
     * ??????????????????????????????????????????
     * @param code ??????
     * @return ??????????????????
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
            case 10:
                return "contains";
            case 11:
                return "isEmpty";
            case 12:
                return "isNotEmpty";
            default:
                return "unknown";
        }
    }

    /**
     * ????????????
     * @param fw FileWriter
     * @param emailAddress emailAddress
     * @param executeStatus executeStatus
     * @param executeMsg executeMsg
     */
    private void sendEmail(FileWriter fw, String emailAddress, int executeStatus,
                           String executeMsg, String stabilityTestLogNo) {
        try {
            if (!StringUtils.isBlank(emailAddress)) {
                this.log(fw, stabilityTestLogNo, "------??????????????????------");
                if (executeStatus == 0) {
                    mailService.send("?????????????????????", executeMsg, emailAddress);
                } else if (executeStatus == 1) {
                    mailService.send("?????????????????????", executeMsg, emailAddress);
                } else {
                    mailService.send("?????????????????????", executeMsg, emailAddress);
                }
                this.log(fw, stabilityTestLogNo, "------??????????????????------");
            }
        } catch (BusinessException e) {
            LOG.error("Send email exception, {}", e.getMessage());
            this.log(fw, stabilityTestLogNo, "------??????????????????------");
            this.log(fw, stabilityTestLogNo, String.format("Send email exception, %s", e.getMessage()));
        }
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     * @param stabilityTestLogNo ????????????
     * @param isRunning ???????????????
     */
    private void setStabilityTestStatus(String stabilityTestLogNo, boolean isRunning) {
        redisUtil.set(stabilityTestLogNo, isRunning);
    }

    /**
     * ??????????????????????????????
     * @param stabilityTestLogNo stabilityTestLogNo
     * @return ???????????????
     */
    private boolean getStabilityTestIsRunning(String stabilityTestLogNo) {
        Object o = redisUtil.get(stabilityTestLogNo);
        if (o == null) {
            return false;
        }
        return (boolean) redisUtil.get(stabilityTestLogNo);
    }

    /**
     * ?????????20???????????????
     * @param stabilityTestLogNo stabilityTestLogNo
     * @param text text
     */
    private void setLogRedisList(String stabilityTestLogNo, String text) {
        String key = NoUtil.genStabilityLogLast10No(stabilityTestLogNo);
        text = text.replaceAll("(\\r\\n|\\n|\\n\\r)","<br/>");
        if (!redisUtil.exist(key)) {
            redisUtil.queuePush(key, text, 60);
        }
        if (redisUtil.lenList(key) >= 20) {
            redisUtil.queuePop(key);
        }
        redisUtil.queuePush(key, text, 60);
    }

    /**
     * ?????????????????????????????????
     * @param stabilityTestLogNo stabilityTestLogNo
     * @param time ??????????????????
     * @param loop ????????????????????????????????????
     */
    private void setResponseTimeQueue(String stabilityTestLogNo, long time, long loop) {
        String key = NoUtil.genStabilityLogResponseTimeQueueNo(stabilityTestLogNo);
        JSONObject var1 = new JSONObject();
        var1.put("Time", time);
        var1.put("Loop", TimeUtil.convert2hms(loop));
        redisUtil.queuePush(key, var1.toString());
    }

    /**
     * ????????????????????????
     * @param stabilityTestLogNo stabilityTestLogNo
     * @return ??????????????????
     */
    public JSONArray getResponseTimeQueue(String stabilityTestLogNo) {
        String key = NoUtil.genStabilityLogResponseTimeQueueNo(stabilityTestLogNo);
        return JSONArray.parseArray(JSON.toJSONString(redisUtil.stackGetAll(key)));
    }

    /**
     * ??????????????????????????????
     */
    public void delResponseTimeQueue(String stabilityTestLogNo) {
        String key = NoUtil.genStabilityLogResponseTimeQueueNo(stabilityTestLogNo);
        redisUtil.del(key);
    }
}
