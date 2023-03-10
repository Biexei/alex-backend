package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.core.parser.Parser;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.InterfaceCaseExecuteLogMapper;
import org.alex.platform.mapper.InterfaceCaseSuiteMapper;
import org.alex.platform.mapper.InterfaceSuiteCaseRefMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.alex.platform.service.InterfaceSuiteLogService;
import org.alex.platform.service.InterfaceSuiteProcessorService;
import org.alex.platform.util.ExceptionUtil;
import org.alex.platform.util.JsonUtil;
import org.alex.platform.util.NoUtil;
import org.alex.platform.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@SuppressWarnings({"unchecked","rawtypes"})
public class InterfaceSuiteCaseRefServiceImpl implements InterfaceSuiteCaseRefService {
    @Autowired
    InterfaceSuiteCaseRefMapper interfaceSuiteCaseRefMapper;
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceCaseSuiteMapper interfaceCaseSuiteMapper;
    @Autowired
    InterfaceCaseExecuteLogMapper interfaceCaseExecuteLogMapper;
    @Autowired
    InterfaceSuiteLogService interfaceSuiteLogService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    InterfaceSuiteProcessorService interfaceSuiteProcessorService;
    @Autowired
    Parser parser;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceSuiteCaseRefServiceImpl.class);

    /**
     * ????????????????????????
     *
     * @param interfaceSuiteCaseRefDOList interfaceSuiteCaseRefDOList
     */
    @Override
    public void saveSuiteCase(List<InterfaceSuiteCaseRefDO> interfaceSuiteCaseRefDOList) {
        List<InterfaceSuiteCaseRefDO> list = new ArrayList<>();
        for (InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO :
                interfaceSuiteCaseRefDOList) {
            Integer caseId = interfaceSuiteCaseRefDO.getCaseId();
            Integer suiteId = interfaceSuiteCaseRefDO.getSuiteId();
            InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO = new InterfaceSuiteCaseRefDTO();
            interfaceSuiteCaseRefDTO.setCaseId(caseId);
            interfaceSuiteCaseRefDTO.setSuiteId(suiteId);
            List<InterfaceSuiteCaseRefVO> refVOList = interfaceSuiteCaseRefMapper.selectSuiteCaseList(interfaceSuiteCaseRefDTO);
            if (refVOList.isEmpty()) {
                list.add(interfaceSuiteCaseRefDO);
            }
        }
        if (!list.isEmpty()) {
            interfaceSuiteCaseRefMapper.insertSuiteCase(list);
        }
    }

    /**
     * ??????????????????
     * @param interfaceSuiteCaseRefDO interfaceSuiteCaseRefDO
     */
    @Override
    public void saveSuiteCaseSingle(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO) {
        interfaceSuiteCaseRefMapper.insertSuiteCaseSingle(interfaceSuiteCaseRefDO);
    }

    /**
     * ???????????????????????????
     *
     * @param interfaceSuiteCaseRefDO interfaceSuiteCaseRefDO
     */
    @Override
    public void modifySuiteCase(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO) {
        interfaceSuiteCaseRefMapper.modifySuiteCase(interfaceSuiteCaseRefDO);
    }

    /**
     * ??????????????????????????????
     *
     * @param incrementKey incrementKey
     */
    @Override
    public void removeSuiteCase(Integer incrementKey) {
        interfaceSuiteCaseRefMapper.deleteSuiteCase(incrementKey);
    }

    /**
     * ????????????????????????????????????
     *
     * @param interfaceSuiteCaseRefDO interfaceSuiteCaseRefDO
     */
    @Override
    public void removeSuiteCaseByObject(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO) {
        interfaceSuiteCaseRefMapper.deleteSuiteCaseByObject(interfaceSuiteCaseRefDO);
    }

    /**
     * ?????????????????????????????????
     *
     * @param interfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO
     * @param pageNum                  pageNum
     * @param pageSize                 pageSize
     * @return PageInfo<InterfaceSuiteCaseRefVO>
     */
    @Override
    public PageInfo<InterfaceSuiteCaseRefVO> findSuiteCaseList(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(interfaceSuiteCaseRefMapper.selectSuiteCaseList(interfaceSuiteCaseRefDTO));
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param interfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO
     * @return List<InterfaceSuiteCaseRefVO>
     */
    @Override
    public List<InterfaceSuiteCaseRefVO> findAllSuiteCase(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO) {
        return interfaceSuiteCaseRefMapper.selectSuiteCaseList(interfaceSuiteCaseRefDTO);
    }

    /**
     * ??????????????????
     *
     * @param suiteId ??????????????????
     * @throws BusinessException BusinessException
     */
    @Override
    public String executeSuiteCaseById(Integer suiteId, String executor) throws BusinessException {
        // ???????????????????????????????????????
        InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO = new InterfaceSuiteCaseRefDTO();
        interfaceSuiteCaseRefDTO.setSuiteId(suiteId);
        List<InterfaceSuiteCaseRefVO> suiteCaseList = interfaceSuiteCaseRefMapper.selectSuiteCaseList(interfaceSuiteCaseRefDTO);
        // ?????????????????????????????? 0??????1??????
        InterfaceCaseSuiteVO interfaceCaseSuiteVO = interfaceCaseSuiteMapper.selectInterfaceCaseSuiteById(suiteId);
        Byte type = interfaceCaseSuiteVO.getExecuteType();
        Byte runDev = interfaceCaseSuiteVO.getRunDev();
        AtomicBoolean isRetry = new AtomicBoolean(interfaceCaseSuiteVO.getIsRetry() == 0);

        Date startTime = new Date();
        long begin = System.currentTimeMillis();
        String suiteLogNo = NoUtil.genSuiteLogNo();
        String suiteLogDetailNo = NoUtil.genSuiteLogDetailNo(suiteLogNo);
        String suiteLogProgressNo = NoUtil.genSuiteLogProgressNo(suiteLogNo);
        AtomicInteger totalRunCase = new AtomicInteger();
        Integer totalCase = suiteCaseList.size();
        AtomicInteger totalSkip = new AtomicInteger();
        AtomicInteger totalSuccess = new AtomicInteger();
        AtomicInteger totalFailed = new AtomicInteger();
        AtomicInteger totalError = new AtomicInteger();
        AtomicInteger totalRetry = new AtomicInteger();

        // ??????????????????????????????????????????????????????????????????
        InterfaceSuiteLogDO initDO = new InterfaceSuiteLogDO();
        initDO.setSuiteId(suiteId);
        initDO.setSuiteLogNo(suiteLogNo);
        initDO.setTotalCase(totalCase);
        initDO.setStartTime(startTime);
        initDO.setExecuteType(type);
        initDO.setRunDev(runDev);
        initDO.setExecutor(executor);
        initDO.setIsRetry(interfaceCaseSuiteVO.getIsRetry());
        initDO.setProgress((byte)0);
        initDO.setPercentage(0);
        InterfaceSuiteLogDO saveDO = interfaceSuiteLogService.saveIfSuiteLog(initDO);
        int id = saveDO.getId();


        // ?????????????????????????????????
        LOG.info("-----------------------?????????????????????????????????-----------------------");
        try {
            this.executeRely(suiteId, (byte) 0, suiteLogDetailNo);
        } catch (Exception e) {
            this.updateProgressFailed(id);
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
            throw new BusinessException("???????????????????????????");
        }
        LOG.info("-----------------------?????????????????????????????????-----------------------");

        HashMap globalHeaders;
        HashMap globalParams;
        HashMap globalData;
        try {
            globalHeaders = this.globalHeaders(suiteId, suiteLogDetailNo);
            globalParams = this.globalParams(suiteId, suiteLogDetailNo);
            globalData = this.globalData(suiteId, suiteLogDetailNo);
        } catch (Exception e) {
            this.updateProgressFailed(saveDO.getId());
            this.updateProgressPercentage(saveDO.getId(), 0);
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
            throw new BusinessException("???????????????????????????");
        }

        if (type == 0) { // ??????
            LOG.info("-----------------------?????????????????????????????????suiteId={}-----------------------", suiteId);
            suiteCaseList.parallelStream().forEach(suiteCase -> {
                Integer caseId = suiteCase.getCaseId();
                boolean doRetryFlag = false;
                if (isRetry.get()) {
                    doRetryFlag = true;
                }
                try {
                    // ??????
                    if (suiteCase.getCaseStatus() == 1) {
                        totalSkip.getAndIncrement();
                    } else {
                        Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(
                                caseId, executor, suiteLogNo, NoUtil.genChainNo(), suiteId, (byte) 1, suiteLogDetailNo,
                                globalHeaders, globalParams, globalData, (byte)2, null, false));
                        InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = interfaceCaseExecuteLogMapper.selectExecute(executeLogId);
                        Byte status = interfaceCaseExecuteLogVO.getStatus();
                        if (status == 0) {
                            totalSuccess.getAndIncrement();
                        } else if (status == 1) {
                            // ??????????????????
                            if (doRetryFlag) {
                                totalRetry.getAndIncrement();
                                Integer retryExecuteLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(
                                        caseId, executor, suiteLogNo, NoUtil.genChainNo(), suiteId, (byte) 0, suiteLogDetailNo,
                                        globalHeaders, globalParams, globalData, (byte)2, null, false));
                                InterfaceCaseExecuteLogVO retryVO = interfaceCaseExecuteLogMapper.selectExecute(retryExecuteLogId);
                                Byte retryStatus = retryVO.getStatus();
                                if (retryStatus == 0) {
                                    totalSuccess.getAndIncrement();
                                } else if (retryStatus == 1) {
                                    totalFailed.getAndIncrement();
                                } else {
                                    totalError.getAndIncrement();
                                }
                            } else {
                                totalFailed.getAndIncrement();
                            }
                        } else {
                            totalError.getAndIncrement();
                        }
                        totalRunCase.getAndIncrement();
                    }
                } catch (Exception e) {
                    this.updateProgressFailed(id);
                    int percent = this.updateProgressCache(suiteLogProgressNo, suiteCaseList.size(),
                            totalRunCase, totalSkip);
                    this.updateProgressPercentage(id, percent);
                    LOG.error("???????????????????????????????????????errorMsg={}", ExceptionUtil.msg(e));
                    throw new RuntimeException(e.getMessage());
                }
                int total = suiteCaseList.size();
                this.updateProgressCache(suiteLogProgressNo, total, totalRunCase, totalSkip);
            });
        } else { // ??????
            LOG.info("-----------------------?????????????????????????????????suiteId={}-----------------------", suiteId);
            for (InterfaceSuiteCaseRefVO suiteCase : suiteCaseList) {
                Integer caseId = suiteCase.getCaseId();
                boolean doRetryFlag = false;
                if (isRetry.get()) {
                    doRetryFlag = true;
                }
                // ??????
                if (suiteCase.getCaseStatus() == 1) {
                    totalSkip.getAndIncrement();
                } else {
                    Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId,
                            executor, suiteLogNo, NoUtil.genChainNo(), suiteId, (byte) 1, suiteLogDetailNo,
                            globalHeaders, globalParams, globalData, (byte)2, null, false));
                    InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = interfaceCaseExecuteLogMapper.selectExecute(executeLogId);
                    Byte status = interfaceCaseExecuteLogVO.getStatus();
                    if (status == 0) {
                        totalSuccess.getAndIncrement();
                    } else if (status == 1) {
                        if (doRetryFlag) {
                            totalRetry.getAndIncrement();
                            Integer retryExecuteLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId,
                                    executor, suiteLogNo, NoUtil.genChainNo(), suiteId, (byte) 0, suiteLogDetailNo,
                                    globalHeaders, globalParams, globalData, (byte)2, null, false));
                            InterfaceCaseExecuteLogVO retryVO = interfaceCaseExecuteLogMapper.selectExecute(retryExecuteLogId);
                            Byte retryStatus = retryVO.getStatus();
                            if (retryStatus == 0) {
                                totalSuccess.getAndIncrement();
                            } else if (retryStatus == 1) {
                                totalFailed.getAndIncrement();
                            } else {
                                totalError.getAndIncrement();
                            }
                        } else {
                            totalFailed.getAndIncrement();
                        }
                    } else {
                        totalError.getAndIncrement();
                    }
                    totalRunCase.getAndIncrement();
                }
                int total = suiteCaseList.size();
                this.updateProgressCache(suiteLogProgressNo, total, totalRunCase, totalSkip);
            }
        }

        // ?????????????????????????????????
        long end = System.currentTimeMillis();
        Date endTime = new Date();
        InterfaceSuiteLogDO interfaceSuiteLogDO = new InterfaceSuiteLogDO();
        interfaceSuiteLogDO.setId(saveDO.getId()); //????????????key
        interfaceSuiteLogDO.setSuiteId(suiteId);
        interfaceSuiteLogDO.setSuiteLogNo(suiteLogNo);
        interfaceSuiteLogDO.setRunTime((end - begin));
        interfaceSuiteLogDO.setTotalSuccess(totalSuccess.intValue());
        interfaceSuiteLogDO.setTotalFailed(totalFailed.intValue());
        interfaceSuiteLogDO.setTotalError(totalError.intValue());
        interfaceSuiteLogDO.setTotalSkip(totalSkip.intValue());
        interfaceSuiteLogDO.setTotalRunCase(totalRunCase.intValue());
        interfaceSuiteLogDO.setTotalCase(totalCase);
        interfaceSuiteLogDO.setStartTime(startTime);
        interfaceSuiteLogDO.setEndTime(endTime);
        interfaceSuiteLogDO.setExecuteType(type);
        interfaceSuiteLogDO.setRunDev(runDev);
        interfaceSuiteLogDO.setExecutor(executor);
        interfaceSuiteLogDO.setIsRetry(interfaceCaseSuiteVO.getIsRetry());
        interfaceSuiteLogDO.setProgress((byte)1);
        interfaceSuiteLogDO.setPercentage(100);
        // ???????????????????????????????????????????????????0????????????
        if (isRetry.get()) {
            interfaceSuiteLogDO.setTotalRetry(totalRetry.intValue());
        } else {
            interfaceSuiteLogDO.setTotalRetry(null);
        }
        interfaceSuiteLogService.modifyIfSuiteLog(interfaceSuiteLogDO);
        LOG.info("????????????????????????????????????suiteLogNo={}???success={}???failed={}???error={}, skip={}, ????????????={}, ????????????={}",
                suiteLogNo, totalSuccess.intValue(), totalFailed.intValue(), totalError.intValue()
                , totalSkip.intValue(), type, runDev);

        // ?????????????????????????????????
        LOG.info("-----------------------?????????????????????????????????-----------------------");
        try {
            this.executeRely(suiteId, (byte) 1, suiteLogDetailNo);
        } catch (Exception e) {
            this.updateProgressFailed(id);
            redisUtil.set(suiteLogProgressNo, 99, 60*60*12);
            this.updateProgressPercentage(id, 99);
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
            throw new BusinessException("???????????????????????????");
        }
        LOG.info("-----------------------?????????????????????????????????-----------------------");
        LOG.info("-----------------------????????????????????????-----------------------");

        // ???????????????????????????
        redisUtil.del(suiteLogDetailNo);

        // ??????????????????
        redisUtil.del(suiteLogProgressNo);

        return suiteLogNo;
    }

    /**
     * ????????????????????????????????????????????????????????????
     * @param suiteId suiteId
     * @param caseId caseId
     * @param executor executor
     * @return ????????????
     * @throws BusinessException BusinessException
     */
    @Override
    public Byte executeCaseInSuite(Integer suiteId, Integer caseId, String executor) throws BusinessException {
        String suiteLogNo = NoUtil.genSuiteLogNo();
        String suiteLogDetailNo = NoUtil.genSuiteLogDetailNo(suiteLogNo);

        // ?????????????????????????????????
        LOG.info("-----------------------??????????????????={}???????????????={}?????????????????????????????????-----------------------", suiteId, caseId);
        try {
            this.executeRely(suiteId, (byte) 0, suiteLogDetailNo);
        } catch (Exception e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
            throw new BusinessException("???????????????????????????");
        }
        LOG.info("-----------------------??????????????????={}???????????????={}?????????????????????????????????-----------------------", suiteId, caseId);

        HashMap globalHeaders;
        HashMap globalParams;
        HashMap globalData;
        try {
            globalHeaders = this.globalHeaders(suiteId, suiteLogDetailNo);
            globalParams = this.globalParams(suiteId, suiteLogDetailNo);
            globalData = this.globalData(suiteId, suiteLogDetailNo);
        } catch (Exception e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
            throw new BusinessException("???????????????????????????");
        }

        Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId,
                executor, null, NoUtil.genChainNo(), suiteId, (byte) 1, suiteLogDetailNo,
                globalHeaders, globalParams, globalData, (byte)3, null, false));
        InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = interfaceCaseExecuteLogMapper.selectExecute(executeLogId);
        Byte status = interfaceCaseExecuteLogVO.getStatus();

        // ?????????????????????????????????
        LOG.info("-----------------------??????????????????={}???????????????={}?????????????????????????????????-----------------------", suiteId, caseId);
        try {
            this.executeRely(suiteId, (byte) 1, suiteLogDetailNo);
        } catch (Exception e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
            throw new BusinessException("???????????????????????????");
        }
        LOG.info("-----------------------??????????????????={}???????????????={}?????????????????????????????????-----------------------", suiteId, caseId);
        LOG.info("-----------------------????????????????????????-----------------------");

        // ???????????????????????????
        redisUtil.del(suiteLogDetailNo);

        return status;
    }


    /**
     * ????????????
     * @param suiteId suiteId
     * @param processorType processorType 0???????????????1???????????????
     * @param suiteLogDetailNo suiteLogDetailNo
     * @throws BusinessException BusinessException
     * @throws ParseException ParseException
     * @throws SqlException SqlException
     */
    private void executeRely(Integer suiteId, Byte processorType, String suiteLogDetailNo) throws BusinessException, ParseException, SqlException {
        InterfaceSuiteProcessorDTO interfaceSuiteProcessorDTO = new InterfaceSuiteProcessorDTO();
        interfaceSuiteProcessorDTO.setProcessorType(processorType);
        interfaceSuiteProcessorDTO.setSuiteId(suiteId);
        interfaceSuiteProcessorDTO.setType((byte) 0);
        List<InterfaceSuiteProcessorVO> processorList = interfaceSuiteProcessorService.findAllInterfaceSuiteProcessorList(interfaceSuiteProcessorDTO);
        if (!processorList.isEmpty()) {
            InterfaceSuiteProcessorVO interfaceSuiteProcessorVO = processorList.get(0);
            // ?????????????????????
            String value = interfaceSuiteProcessorVO.getValue();
            parser.parseDependency(value, NoUtil.genChainNo(), suiteId, (byte) 1, suiteLogDetailNo,
                    null, null, null, null);
        }
    }

    /**
     * ????????????????????????
     * @param suiteId suiteId
     * @param type type
     * @param suiteLogDetailNo suiteLogDetailNo
     * @return value
     * @throws BusinessException BusinessException
     * @throws ParseException ParseException
     * @throws SqlException SqlException
     */
    private String getPreProcessor(Integer suiteId, byte type, String suiteLogDetailNo) throws BusinessException, ParseException, SqlException {
        InterfaceSuiteProcessorDTO interfaceSuiteProcessorDTO = new InterfaceSuiteProcessorDTO();
        interfaceSuiteProcessorDTO.setProcessorType((byte) 0);
        interfaceSuiteProcessorDTO.setSuiteId(suiteId);
        interfaceSuiteProcessorDTO.setType(type);
        List<InterfaceSuiteProcessorVO> processorList = interfaceSuiteProcessorService.findAllInterfaceSuiteProcessorList(interfaceSuiteProcessorDTO);
        if (!processorList.isEmpty()) {
            InterfaceSuiteProcessorVO interfaceSuiteProcessorVO = processorList.get(0);
            // ??????0????????????1?????????2??????params3??????data
            String value = interfaceSuiteProcessorVO.getValue();
            if (value != null) {
                value = parser.parseDependency(value, NoUtil.genChainNo(), suiteId, (byte) 1,
                        suiteLogDetailNo,null, null, null, null);
            }
            return value;
        }
        return null;
    }

    /**
     * ??????global headers hashMap
     * @param suiteId suiteId
     * @param suiteLogDetailNo suiteLogDetailNo
     * @return ??????global headers hashMap
     * @throws BusinessException BusinessException
     * @throws ParseException ParseException
     * @throws SqlException SqlException
     */
    private HashMap globalHeaders(Integer suiteId, String suiteLogDetailNo) throws BusinessException, ParseException, SqlException {
        return JsonUtil.jsonString2HashMap(this.getPreProcessor(suiteId, (byte) 1, suiteLogDetailNo));
    }

    /**
     * ??????global params hashMap
     * @param suiteId suiteId
     * @param suiteLogDetailNo suiteLogDetailNo
     * @return ??????global headers hashMap
     * @throws BusinessException BusinessException
     * @throws ParseException ParseException
     * @throws SqlException SqlException
     */
    private HashMap globalParams(Integer suiteId, String suiteLogDetailNo) throws BusinessException, ParseException, SqlException {
        return JsonUtil.jsonString2HashMap(this.getPreProcessor(suiteId, (byte) 2, suiteLogDetailNo));
    }

    /**
     * ??????global data hashMap
     * @param suiteId suiteId
     * @param suiteLogDetailNo suiteLogDetailNo
     * @return ??????global headers hashMap
     * @throws BusinessException BusinessException
     * @throws ParseException ParseException
     * @throws SqlException SqlException
     */
    private HashMap globalData(Integer suiteId, String suiteLogDetailNo) throws BusinessException, ParseException, SqlException {
        return JsonUtil.jsonString2HashMap(this.getPreProcessor(suiteId, (byte) 3, suiteLogDetailNo));
    }

    /**
     * ????????????????????????????????????
     * @param suiteLogId ????????????????????????
     */
    private void updateProgressFailed(Integer suiteLogId) {
        InterfaceSuiteLogDO logDO = new InterfaceSuiteLogDO();
        logDO.setId(suiteLogId);
        logDO.setProgress((byte)2);
        interfaceSuiteLogService.modifyIfSuiteLog(logDO);
    }

    /**
     * ???????????????????????????????????????
     * @param suiteLogId ????????????????????????
     * @param percentage ?????????
     */
    private void updateProgressPercentage(Integer suiteLogId, int percentage) {
        InterfaceSuiteLogDO logDO = new InterfaceSuiteLogDO();
        logDO.setId(suiteLogId);
        logDO.setPercentage(percentage);
        interfaceSuiteLogService.modifyIfSuiteLog(logDO);
    }

    /**
     * ??????????????????
     * @param suiteLogProgressNo suiteLogProgressNo
     * @param total ?????????
     * @param totalRunCase ?????????
     * @param totalSkip ?????????
     */
    private int updateProgressCache(String suiteLogProgressNo, int total, AtomicInteger totalRunCase, AtomicInteger totalSkip) {
        float run = totalRunCase.floatValue() + totalSkip.floatValue();
        if (total == 0 || run == 0) {
            redisUtil.set(suiteLogProgressNo, 0, 60*60*12);
            return 0;
        } else {
            int rate = (int) (run/total*100);
            redisUtil.set(suiteLogProgressNo, rate,60*60*12);
            return rate;
        }
    }
}
