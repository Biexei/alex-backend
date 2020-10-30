package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.InterfaceCaseRelyDataMapper;
import org.alex.platform.mapper.RelyDataMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.alex.platform.service.InterfaceCaseRelyDataService;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.ProjectService;
import org.alex.platform.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class InterfaceCaseRelyDataServiceImpl implements InterfaceCaseRelyDataService {
    @Autowired
    InterfaceCaseRelyDataMapper ifRelyDataMapper;
    @Autowired
    InterfaceCaseService ifCaseService;
    @Autowired
    RelyDataMapper relyDataMapper;
    @Autowired
    InterfaceCaseRelyDataService ifCaseRelyDataService;
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;
    @Autowired
    ProjectService projectService;
    @Autowired
    RedisUtil redisUtil;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceCaseRelyDataServiceImpl.class);

    /**
     * 新增接口依赖
     *
     * @param ifRelyDataDO ifRelyDataDO
     * @throws BusinessException 用例编号不存在/依赖名称已存在与其它依赖/依赖名称已存在与接口依赖检查
     */
    @Override
    public void saveIfRelyData(InterfaceCaseRelyDataDO ifRelyDataDO) throws BusinessException {
        // 检查提取表达式
        Byte contentType = ifRelyDataDO.getContentType(); // 提取数据类型   0json/1html/2header/
        String extractExpression = ifRelyDataDO.getExtractExpression();
        if (contentType == 0) {
            ValidUtil.isJsonPath(extractExpression);
        } else if (contentType == 1) {
            ValidUtil.isXpath(extractExpression);
        }
        // 判断relyCaseId是否存在
        Integer caseId = ifRelyDataDO.getRelyCaseId();
        if (ifCaseService.findInterfaceCaseByCaseId(caseId) == null) {
            LOG.warn("新增接口依赖，用例编号不存在，caseId={}", caseId);
            throw new BusinessException("用例编号不存在");
        }
        // 判断relyName是否已存在
        String relyName = ifRelyDataDO.getRelyName();
        if (relyDataMapper.selectRelyDataByName(relyName) != null) {
            LOG.warn("新增接口依赖，依赖名称已存在于其它依赖，relyName={}", relyName);
            throw new BusinessException("依赖名称已存在于其它依赖");
        }
        if (this.findIfRelyDataByName(relyName) != null) {
            LOG.warn("新增接口依赖，依赖名称已存在于接口依赖，relyName={}", relyName);
            throw new BusinessException("依赖名称已存在于接口依赖");
        }
        ifRelyDataMapper.insertIfRelyData(ifRelyDataDO);
    }

    /**
     * 修改接口依赖
     *
     * @param ifRelyDataDO ifRelyDataDO
     * @throws BusinessException 用例编号不存在/依赖名称已存在与其它依赖/依赖名称已存在与接口依赖检查
     */
    @Override
    public void modifyIfRelyData(InterfaceCaseRelyDataDO ifRelyDataDO) throws BusinessException {
        // 检查提取表达式
        Byte contentType = ifRelyDataDO.getContentType(); // 提取数据类型   0json/1html/2header/
        String extractExpression = ifRelyDataDO.getExtractExpression();
        if (contentType == 0) {
            ValidUtil.isJsonPath(extractExpression);
        } else if (contentType == 1) {
            ValidUtil.isXpath(extractExpression);
        }
        // 判断relyCaseId是否存在
        Integer caseId = ifRelyDataDO.getRelyCaseId();
        if (ifCaseService.findInterfaceCaseByCaseId(caseId) == null) {
            LOG.warn("修改接口依赖，用例编号不存在，caseId={}", caseId);
            throw new BusinessException("用例编号不存在");
        }
        // 判断relyName是否已存在
        String relyName = ifRelyDataDO.getRelyName();
        if (relyDataMapper.selectRelyDataByName(relyName) != null) {
            LOG.warn("修改接口依赖，依赖名称已存在于其它依赖，relyName={}", relyName);
            throw new BusinessException("依赖名称已存在于其它依赖");
        }
        if (!ifRelyDataMapper.checkRelyName(ifRelyDataDO).isEmpty()) {
            LOG.warn("修改接口依赖，依赖名称已存在于接口依赖，relyName={}", relyName);
            throw new BusinessException("依赖名称已存在于接口依赖");
        }
        ifRelyDataMapper.updateIfRelyData(ifRelyDataDO);
    }

    /**
     * 查看接口依赖列表
     *
     * @param ifRelyDataDTO ifRelyDataDTO
     * @param pageNum       pageNum
     * @param pageSize      pageSize
     * @return PageInfo<InterfaceCaseRelyDataVO>
     */
    @Override
    public PageInfo<InterfaceCaseRelyDataVO> findIfRelyDataList(InterfaceCaseRelyDataDTO ifRelyDataDTO,
                                                                Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        ArrayList<InterfaceCaseRelyDataVO> list = new ArrayList<>();
        for (InterfaceCaseRelyDataVO interfaceCaseRelyDataVO : ifRelyDataMapper.selectIfRelyDataList(ifRelyDataDTO)) {
            // 查询项目编号
            Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
            Integer projectId = interfaceCaseService.findInterfaceCaseByCaseId(caseId).getProjectId();
            // 查询项目URL
            ProjectDO projectDO = new ProjectDO();
            projectDO.setProjectId(projectId);
            String domain = projectService.findProject(projectDO).getDomain();
            String url = interfaceCaseRelyDataVO.getCaseUrl();
            interfaceCaseRelyDataVO.setCaseUrl(domain + url);
            list.add(interfaceCaseRelyDataVO);
        }
        return new PageInfo(list);
    }

    /**
     * 获取接口依赖详情
     *
     * @param relyId 依赖编号
     * @return InterfaceCaseRelyDataVO
     */
    @Override
    public InterfaceCaseRelyDataVO findIfRelyData(Integer relyId) {
        return ifRelyDataMapper.selectIfRelyDataById(relyId);
    }

    /**
     * 根据依赖名称查询
     *
     * @param relyName 依赖名称
     * @return InterfaceCaseRelyDataVO
     */
    @Override
    public InterfaceCaseRelyDataVO findIfRelyDataByName(String relyName) {
        return ifRelyDataMapper.selectIfRelyDataByName(relyName);
    }

    /**
     * 删除接口依赖
     *
     * @param relyId 依赖编号
     */
    @Override
    public void removeIfRelyData(Integer relyId) {
        ifRelyDataMapper.deleteIfRelyData(relyId);
    }

    /**
     * 执行接口依赖
     *
     * @param relyId 依赖编号
     * @return 执行结果
     * @throws ParseException    ParseException
     * @throws SqlException      SqlException
     * @throws BusinessException BusinessException
     */
    @Override
    public String checkRelyResult(Integer relyId, String executor) throws ParseException, SqlException, BusinessException {
        LOG.info("------------------------------执行接口预检操作------------------------------");
        InterfaceCaseRelyDataVO interfaceCaseRelyData = ifCaseRelyDataService.findIfRelyData(relyId);
        if (null == interfaceCaseRelyData) {
            LOG.warn("预检接口依赖，未找到该接口依赖，relyId={}", relyId);
            throw new ParseException("未找到该接口依赖");
        }
        Integer caseId = interfaceCaseRelyData.getRelyCaseId();
        String relyName = interfaceCaseRelyData.getRelyName();

        if (relyName.indexOf("[") != -1 && relyName.endsWith("]")) {
            LOG.info("------------------------------进入下标取值模式------------------------------");
            // 判断出现次数,首次出现和最后一次出现位置不一致，则说明[>1 ]>1
            if (relyName.indexOf("[") != relyName.lastIndexOf("[") ||
                    relyName.indexOf("]") != relyName.lastIndexOf("]")) {
                LOG.warn("预检接口依赖，数组取值语法错误，relyName={}", relyName);
                throw new ParseException("数组取值语法错误");
            }
            String indexStr = relyName.substring(relyName.indexOf("[") + 1, relyName.length() - 1);
            try {
                int index = Integer.parseInt(indexStr);
                relyName = relyName.substring(0, relyName.indexOf("["));
                // 查询其所依赖的caseId
                LOG.info("根据relyName获取caseId，relyName={}", relyName);
                InterfaceCaseRelyDataDTO interfaceCaseRelyDataDTO = new InterfaceCaseRelyDataDTO();
                interfaceCaseRelyDataDTO.setRelyName(relyName);
                InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = ifCaseRelyDataService.findIfRelyDataByName(relyName);
                if (null == interfaceCaseRelyDataVO) {
                    LOG.warn("预检接口依赖，未找到该依赖数值，relyName={}", relyName);
                    throw new ParseException("未找到该依赖数值");
                }
                // 根据caseId调用相应case
                LOG.info("根据caseId调用相应case，caseId={}", caseId);

                String chainNo = NoUtil.genChainNo();
                Integer executeLogId = interfaceCaseService.executeInterfaceCase(caseId, executor, null, chainNo, null, (byte) 1, null);
                // 缓存
                redisUtil.stackPush(chainNo, executeLogId);

                LOG.info("调用成功，executeLogId={}", executeLogId);
                // 获取case执行结果, 不等于0, 则用例未通过
                if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                    LOG.warn("预检接口依赖，前置用例执行未通过，executeLogId={}", executeLogId);
                    throw new BusinessException("前置用例执行未通过");
                }
                // 根据executeLogId查询对应的执行记录
                InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
                String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
                String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
                LOG.info("responseBody={}，responseHeaders={}", responseBody, responseHeaders);
                // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
                int contentType = (int) interfaceCaseRelyDataVO.getContentType();
//                if (contentType != 2) {
//                    LOG.warn("预检接口依赖，仅header支持下标取值");
//                    throw new ParseException("只有依赖数据提取类型为header时才支持指定下标，" +
//                            "否则请自行调整jsonpath/xpath表达式，使提取结果唯一");
//                }
                String expression = interfaceCaseRelyDataVO.getExtractExpression();
                try {
                    if (contentType == 0) { // json
                        LOG.info("------------------------------根据jsonPath取值");
                        ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                        if (jsonPathArray.isEmpty()) {
                            LOG.warn("预检接口依赖，提取方式为jsonPath，提取内容为空，expression={}", expression);
                            throw new ParseException(expression + "提取内容为空");
                        }
                        LOG.info("提取内容为：{}",jsonPathArray.get(0).toString());
                        return jsonPathArray.get(0).toString();
                    } else if (contentType == 1) { // html
                        LOG.info("------------------------------根据xpath取值");
                        ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                        if (xpathArray.isEmpty()) {
                            LOG.warn("预检接口依赖，提取方式为xpath，提取内容为空，expression={}", expression);
                            throw new ParseException(expression + "提取内容为空");
                        }
                        LOG.info("提取内容为：{}",xpathArray.get(0).toString());
                        return xpathArray.get(0).toString();
                    } else if (contentType == 2) { // headers
                        LOG.info("------------------------------根据header取值");
                        JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders, HashMap.class).get(expression);
                        if (null == headerArray) {
                            LOG.warn("预检接口依赖，提取方式为headers，未找到请求头，expression={}", expression);
                            throw new ParseException("未找到请求头:" + expression);
                        }
                        try {
                            LOG.info("提取内容为：{}",headerArray.get(0).toString());
                            return headerArray.get(index).toString();
                        } catch (Exception e) {
                            LOG.warn("预检接口依赖，提取方式为headers，数组下标越界，expression={}， index={}", index, expression);
                            throw new ParseException("数组下标越界");
                        }
                    } else {
                        LOG.warn("不支持该种取值方式, contentType={}", contentType);
                        throw new BusinessException("不支持该种取值方式");
                    }
                } catch (BusinessException e) {
                    throw new BusinessException("不支持该种取值方式");
                } catch (Exception e) {
                    LOG.error("预检接口依赖出现异常，errorMsg={}", ExceptionUtil.msg(e));
                    throw new ParseException(e.getMessage());
                }
            } catch (NumberFormatException e) {
                LOG.error("预检接口依赖，数组下标只能为数字，errorMsg={}", ExceptionUtil.msg(e));
                throw new ParseException("数组下标只能为数字");
            }
        } else {
            LOG.info("------------------------------进入无下标取值模式------------------------------");
            // 根据caseId调用相应case
            LOG.info("根据caseId调用相应case，caseId={}", caseId);

            String chainNo = NoUtil.genChainNo();
            Integer executeLogId = interfaceCaseService.executeInterfaceCase(caseId, executor, null, chainNo, null, (byte) 1, null);
            redisUtil.stackPush(chainNo, executeLogId);

            LOG.info("调用成功，executeLogId={}", executeLogId);
            // 获取case执行结果, 不等于0, 则用例未通过
            if (executeLogService.findExecute(executeLogId).getStatus() != 0) {
                LOG.warn("预检接口依赖，前置用例执行未通过，executeLogId={}", executeLogId);
                throw new BusinessException("前置用例执行未通过");
            }
            // 根据executeLogId查询对应的执行记录
            InterfaceCaseExecuteLogVO interfaceCaseExecuteLogVO = executeLogService.findExecute(executeLogId);
            String responseBody = interfaceCaseExecuteLogVO.getResponseBody();
            String responseHeaders = interfaceCaseExecuteLogVO.getResponseHeaders();
            LOG.info("responseBody={}，responseHeaders={}", responseBody, responseHeaders);
            // 根据contentType来确定对何字段进行替换, 提取数据类型   0json/1html/2header/
            int contentType = (int) interfaceCaseRelyData.getContentType();
            String expression = interfaceCaseRelyData.getExtractExpression();
            try {
                if (contentType == 0) { // json
                    LOG.info("------------------------------根据jsonPath取值");
                    ArrayList jsonPathArray = JSONObject.parseObject(ParseUtil.parseJson(responseBody, expression), ArrayList.class);
                    if (jsonPathArray.isEmpty()) {
                        LOG.warn("预检接口依赖，提取方式为jsonPath，提取内容为空，expression={}", expression);
                        throw new ParseException(expression + "提取内容为空");
                    }
                    LOG.info("提取内容为：{}",jsonPathArray.get(0).toString());
                    return jsonPathArray.get(0).toString();
                } else if (contentType == 1) { // html
                    LOG.info("------------------------------根据xpath取值");
                    ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                    if (xpathArray.isEmpty()) {
                        LOG.warn("预检接口依赖，提取方式为xpath，提取内容为空，expression={}", expression);
                        throw new ParseException(expression + "提取内容为空");
                    }
                    LOG.info("提取内容为：{}",xpathArray.get(0).toString());
                    return xpathArray.get(0).toString();
                } else if (contentType == 2) { // headers
                    LOG.info("------------------------------根据header取值");
                    JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders,
                            HashMap.class).get(expression);
                    if (headerArray == null) {
                        LOG.warn("预检接口依赖，提取方式为headers，未找到请求头，expression={}",expression);
                        throw new ParseException("未找到请求头:" + expression);
                    } else {
                        LOG.info("提取内容为：{}",headerArray.get(0).toString());
                        return headerArray.get(0).toString();
                    }
                } else {
                    LOG.warn("不支持该种取值方式, contentType={}", contentType);
                    throw new BusinessException("不支持该种取值方式");
                }
            } catch (BusinessException e) {
                LOG.warn("不支持该种取值方式, contentType={}", contentType);
                throw new BusinessException("不支持该种取值方式");
            } catch (Exception e) {
                LOG.error("预检接口依赖出现异常，errorMsg={}", ExceptionUtil.msg(e));
                throw new ParseException(e.getMessage());
            }
        }
    }
}
