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

        // 最大可靠性测试数量为2
        Integer countExecuting = stabilityCaseLogMapper.countExecuting();
        if (countExecuting > 1) {
            throw new BusinessException("超过最大可执行任务：2");
        }

        // 不可重复创建相同任务
        Integer countCaseExecuting = stabilityCaseLogMapper.countExecutingByCaseId(id);
        if (countCaseExecuting > 0) {
            throw new BusinessException("当前任务已在执行中");
        }
    }

    /**
     * 强制停止可靠性任务
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
                    // 手动在将数据库状态调整为1
                    StabilityCaseLogDO stabilityCaseLogDO = new StabilityCaseLogDO();
                    stabilityCaseLogDO.setStatus((byte)1);
                    stabilityCaseLogDO.setStabilityTestLogId(stabilityTestLogId);
                    stabilityCaseLogMapper.updateStabilityCaseLog(stabilityCaseLogDO);
                    LOG.warn("可靠性任务被强制停止，日志编号为[{}], 操作人为[{}]", stabilityTestLogId, executeId);
                }
            }
        } else {
            throw new BusinessException("任务编号不存在");
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
            throw new BusinessException("可靠性测试日志创建失败");
        }

        this.log(fw, stabilityTestLogNo,"Stability Test Starting...");
        this.log(fw, stabilityTestLogNo, String.format("Run environment was %s", runEnv));
        this.log(fw, stabilityTestLogNo, "---------Stability Test Case Info---------");
        this.log(fw, stabilityTestLogNo, stabilityCaseInfo.toString());
        this.log(fw, stabilityTestLogNo, "---------Stability Test Case Info---------");

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

        // 将可靠性用例在缓存中设置为运行态
        this.setStabilityTestStatus(stabilityTestLogNo, true);

        // 修改最近执行时间
        stabilityCaseMapper.updateStabilityCaseLastExecuteTime(id, exeDate);

        JSONObject response = new JSONObject(); //接收接口运行结果
        int executeStatus = 0; //接受每次发送请求的状态
        String executeMsg = "";
        int logStatus = 0; //确定最终记录的日志状态
        int loopCount = 0; //记录迭代次数
        int successCount = 0; //记录成功次数
        int failedCount = 0; //记录失败次数
        int errorCount = 0; //记录错误次数
        int warningCount = 0; //记录警告次数（出错或者失败，但设置出错或者失败不中断）

        if (protocol == 0) { //http(s)
            InterfaceCaseInfoVO interfaceCaseInfoVO = interfaceCaseService.findInterfaceCaseByCaseId(caseId);
            this.log(fw, stabilityTestLogNo, "---------Http Case Info---------");
            this.log(fw, stabilityTestLogNo, interfaceCaseInfoVO.toString());
            this.log(fw, stabilityTestLogNo, "---------Http Case Info---------");
            if (executeType == 0) {
                this.log(fw, stabilityTestLogNo, String.format("Dispatch type：%s", "按执行次数"));
                this.log(fw, stabilityTestLogNo, String.format("The estimation of the request times：%s", executeTimes));
            } else {
                this.log(fw, stabilityTestLogNo, String.format("Dispatch type：%s", "按截至时间"));
                this.log(fw, stabilityTestLogNo, String.format("The estimation of the end time：%s",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executeEndTime)));
            }
            this.log(fw, stabilityTestLogNo, "\r\n\r\n\n");

            // 获取调度方式
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
                                LOG.error("稳定性测试因执行失败终止");
                                LOG.error("Failed msg：{}", executeMsg);
                                this.log(fw, stabilityTestLogNo, "稳定性测试因执行失败终止");
                                this.log(fw, stabilityTestLogNo, String.format("Failed msg：%s", executeMsg));
                                logStatus = 1;
                                // 将可靠性用例在缓存中设置为停止态
                                this.setStabilityTestStatus(stabilityTestLogNo, false);
                                break;
                            }
                            ++warningCount;
                            this.log(fw, stabilityTestLogNo, String.format("Warning[Failed] msg：%s", executeMsg));
                        } else if (executeStatus == 2) {
                            if (onErrorStop) {
                                ++errorCount;
                                LOG.error("稳定性测试因执行错误终止");
                                LOG.error("Error msg：{}", executeMsg);
                                this.log(fw, stabilityTestLogNo, "稳定性测试因执行错误终止");
                                this.log(fw, stabilityTestLogNo, String.format("Error msg：%s", executeMsg));
                                logStatus = 2;
                                // 将可靠性用例在缓存中设置为停止态
                                this.setStabilityTestStatus(stabilityTestLogNo, false);
                                break;
                            }
                            ++warningCount;
                            this.log(fw, stabilityTestLogNo, String.format("Warning[Error] msg：%s", executeMsg));
                        }
                        try {
                            Thread.sleep(step*1000);
                        } catch (InterruptedException e) {
                            ++errorCount;
                            LOG.error("稳定性测试因异常执行终止");
                            LOG.error("Exception msg：{}", e.getMessage());
                            this.log(fw, stabilityTestLogNo, "稳定性测试因异常执行终止");
                            this.log(fw, stabilityTestLogNo, String.format("Exception msg：%s", executeMsg));
                            logStatus = 2;
                            e.printStackTrace();
                            // 将可靠性用例在缓存中设置为停止态
                            this.setStabilityTestStatus(stabilityTestLogNo, false);
                            break;
                        }
                        this.log(fw, stabilityTestLogNo, "\r\n\r\n\n");
                        try {
                            fw.flush();
                        } catch (IOException e) {
                            ++errorCount;
                            LOG.error("稳定性测试因异常执行终止");
                            LOG.error("Exception msg：{}", e.getMessage());
                            this.log(fw, stabilityTestLogNo, "稳定性测试因异常执行终止");
                            this.log(fw, stabilityTestLogNo, String.format("Exception msg：%s", executeMsg));
                            logStatus = 2;
                            e.printStackTrace();
                            // 将可靠性用例在缓存中设置为停止态
                            this.setStabilityTestStatus(stabilityTestLogNo, false);
                            break;
                        }
                    } else {
                        logStatus = 1;
                        this.log(fw, stabilityTestLogNo, String.format("任务被用户编号[%s]强制终止", executeId));
                        break;
                    }
                    ++successCount;
                }
                // 将可靠性用例在缓存中设置为停止态
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
                                LOG.error("稳定性测试因执行失败终止");
                                LOG.error("Failed msg：{}", executeMsg);
                                this.log(fw, stabilityTestLogNo, "稳定性测试因执行失败终止");
                                this.log(fw, stabilityTestLogNo, String.format("Failed msg：%s", executeMsg));
                                logStatus = 1;
                                // 将可靠性用例在缓存中设置为停止态
                                this.setStabilityTestStatus(stabilityTestLogNo, false);
                                break;
                            }
                            ++warningCount;
                            this.log(fw, stabilityTestLogNo, String.format("Warning[Failed] msg：%s", executeMsg));
                        } else if (executeStatus == 2) {
                            if (onErrorStop) {
                                ++errorCount;
                                LOG.error("稳定性测试因执行错误终止");
                                LOG.error("Error msg：{}", executeMsg);
                                this.log(fw, stabilityTestLogNo, "稳定性测试因执行错误终止");
                                this.log(fw, stabilityTestLogNo, String.format("Error msg：%s", executeMsg));
                                logStatus = 2;
                                // 将可靠性用例在缓存中设置为停止态
                                this.setStabilityTestStatus(stabilityTestLogNo, false);
                                break;
                            }
                            ++warningCount;
                            this.log(fw, stabilityTestLogNo, String.format("Warning[Error] msg：%s", executeMsg));
                        }
                        try {
                            Thread.sleep(step*1000);
                        } catch (InterruptedException e) {
                            ++errorCount;
                            LOG.error("稳定性测试因异常执行终止");
                            LOG.error("Exception msg：{}", e.getMessage());
                            this.log(fw, stabilityTestLogNo, "稳定性测试因异常执行终止");
                            this.log(fw, stabilityTestLogNo, String.format("Exception msg：%s", executeMsg));
                            logStatus = 2;
                            e.printStackTrace();
                            // 将可靠性用例在缓存中设置为停止态
                            this.setStabilityTestStatus(stabilityTestLogNo, false);
                            break;
                        }
                        this.log(fw, stabilityTestLogNo, "\r\n\r\n\n");
                        try {
                            fw.flush();
                        } catch (IOException e) {
                            ++errorCount;
                            LOG.error("稳定性测试因异常执行终止");
                            LOG.error("Exception msg：{}", e.getMessage());
                            this.log(fw, stabilityTestLogNo, "稳定性测试因异常执行终止");
                            this.log(fw, stabilityTestLogNo, String.format("Exception msg：%s", executeMsg));
                            logStatus = 2;
                            e.printStackTrace();
                            // 将可靠性用例在缓存中设置为停止态
                            this.setStabilityTestStatus(stabilityTestLogNo, false);
                            break;
                        }
                    } else {
                        logStatus = 1;
                        this.log(fw, stabilityTestLogNo, String.format("任务被用户编号[%s]强制终止", executeId));
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
            this.log(fw, stabilityTestLogNo, String.format("调度方式：%s", "按执行次数"));
            this.log(fw, stabilityTestLogNo, String.format("预计执行次数：%s", executeTimes));
        } else {
            this.log(fw, stabilityTestLogNo, String.format("调度方式：%s", "按截至时间"));
            this.log(fw, stabilityTestLogNo, String.format("预计截至时间：%s", TimeUtil.format(executeEndTime)));
        }
        String startTimeS = TimeUtil.format(exeDate);
        Date endTime = new Date();
        String endTimeS = TimeUtil.format(endTime);
        this.log(fw, stabilityTestLogNo, "\r\n");
        this.log(fw, stabilityTestLogNo, String.format("执行开始时间：%s", startTimeS));
        this.log(fw, stabilityTestLogNo, String.format("实际结束时间：%s", endTimeS));
        this.log(fw, stabilityTestLogNo, "\r\n");
        this.log(fw, stabilityTestLogNo, String.format("请求次数：%s", loopCount));
        this.log(fw, stabilityTestLogNo, String.format("警告次数：%s", warningCount));
        this.log(fw, stabilityTestLogNo, String.format("成功次数：%s", successCount));
        this.log(fw, stabilityTestLogNo, String.format("失败次数：%s", failedCount));
        this.log(fw, stabilityTestLogNo, String.format("错误次数：%s", errorCount));
        this.log(fw, stabilityTestLogNo, "---------Test Report---------");
        // 发送邮件
        this.sendEmail(fw, emailAddress, executeStatus, executeMsg, stabilityTestLogNo);
        // 写日志
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
        // 删除缓存
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
     * 稳定性测试-http
     * @param interfaceCaseInfoVO interfaceCaseInfoVO
     * @param runEnv 运行环境
     * @param logRecordContent 日志记录内容
     * @param fw 文件流
     * @param stabilityTestLogNo 执行编号
     * @param startTime 开始执行稳定性测试的时间戳
     * @return 执行结果
     * @throws BusinessException BusinessException
     */
    @SuppressWarnings({"unchecked","rawtypes"})
    private JSONObject doHttpRequest(InterfaceCaseInfoVO interfaceCaseInfoVO,
                                     Byte runEnv, Byte logRecordContent, FileWriter fw, 
                                     String stabilityTestLogNo, long startTime) throws BusinessException {
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

            this.log(fw, stabilityTestLogNo, String.format("Request url: %s", url));

            if (responseEntity == null) {
                executeStatus = 2;
                executeMsg = "请求错误";
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
                    assertMsg = String.format("断言未通过，预期结果[%s]，操作符[%s]，实际结果[%s]", exceptedResult,
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
     * 发送邮件
     * @param fw FileWriter
     * @param emailAddress emailAddress
     * @param executeStatus executeStatus
     * @param executeMsg executeMsg
     */
    private void sendEmail(FileWriter fw, String emailAddress, int executeStatus,
                           String executeMsg, String stabilityTestLogNo) {
        try {
            if (!StringUtils.isBlank(emailAddress)) {
                this.log(fw, stabilityTestLogNo, "------开始发送邮件------");
                if (executeStatus == 0) {
                    mailService.send("稳定性测试成功", executeMsg, emailAddress);
                } else if (executeStatus == 1) {
                    mailService.send("稳定性测试失败", executeMsg, emailAddress);
                } else {
                    mailService.send("稳定性测试错误", executeMsg, emailAddress);
                }
                this.log(fw, stabilityTestLogNo, "------成功发送邮件------");
            }
        } catch (BusinessException e) {
            LOG.error("Send email exception, {}", e.getMessage());
            this.log(fw, stabilityTestLogNo, "------发送邮件失败------");
            this.log(fw, stabilityTestLogNo, String.format("Send email exception, %s", e.getMessage()));
        }
    }

    /**
     * 设置缓存中的执行状态，用于强制停止稳定性测试任务
     * @param stabilityTestLogNo 日志编号
     * @param isRunning 是否在运行
     */
    private void setStabilityTestStatus(String stabilityTestLogNo, boolean isRunning) {
        redisUtil.set(stabilityTestLogNo, isRunning);
    }

    /**
     * 获取缓存中的运行状态
     * @param stabilityTestLogNo stabilityTestLogNo
     * @return 是否在运行
     */
    private boolean getStabilityTestIsRunning(String stabilityTestLogNo) {
        Object o = redisUtil.get(stabilityTestLogNo);
        if (o == null) {
            return false;
        }
        return (boolean) redisUtil.get(stabilityTestLogNo);
    }

    /**
     * 查看近20行执行记录
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
     * 记录每次请求的响应时间
     * @param stabilityTestLogNo stabilityTestLogNo
     * @param time 此次请求耗时
     * @param loop 从开始运行到此次请求耗时
     */
    private void setResponseTimeQueue(String stabilityTestLogNo, long time, long loop) {
        String key = NoUtil.genStabilityLogResponseTimeQueueNo(stabilityTestLogNo);
        JSONObject var1 = new JSONObject();
        var1.put("Time", time);
        var1.put("Loop", TimeUtil.convert2hms(loop));
        redisUtil.queuePush(key, var1.toString());
    }

    /**
     * 返回响应时间队列
     * @param stabilityTestLogNo stabilityTestLogNo
     * @return 响应时间队列
     */
    public JSONArray getResponseTimeQueue(String stabilityTestLogNo) {
        String key = NoUtil.genStabilityLogResponseTimeQueueNo(stabilityTestLogNo);
        return JSONArray.parseArray(JSON.toJSONString(redisUtil.stackGetAll(key)));
    }

    /**
     * 删除缓存响应时间队列
     */
    public void delResponseTimeQueue(String stabilityTestLogNo) {
        String key = NoUtil.genStabilityLogResponseTimeQueueNo(stabilityTestLogNo);
        redisUtil.del(key);
    }
}
