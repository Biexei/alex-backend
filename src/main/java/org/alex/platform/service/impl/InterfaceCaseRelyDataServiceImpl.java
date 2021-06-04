package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.InterfaceCaseRelyDataMapper;
import org.alex.platform.mapper.RelyDataMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.alex.platform.service.InterfaceCaseRelyDataService;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.ProjectService;
import org.alex.platform.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    LoginUserInfo loginUserInfo;
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
    public void modifyIfRelyData(InterfaceCaseRelyDataDO ifRelyDataDO, HttpServletRequest request) throws BusinessException {
        // 获取当前编辑人userId
        int userId = loginUserInfo.getUserId(request);
        Integer relyId = ifRelyDataDO.getRelyId();
        InterfaceCaseRelyDataVO ifRelyData = this.findIfRelyData(relyId);
        Byte modifiable = ifRelyData.getOthersModifiable();
        Integer creatorId = ifRelyData.getCreatorId();
        if (modifiable == null ||  creatorId == null) {
            throw new BusinessException("仅允许创建人修改");
        }
        if (creatorId != userId) {
            if (modifiable.intValue() == 1) {
                throw new BusinessException("仅允许创建人修改");
            }
            // 当前编辑人与创建人不一致时，不允许修改othersModifiable和othersDeletable字段
            ifRelyDataDO.setOthersDeletable(null);
            ifRelyDataDO.setOthersModifiable(null);
        }

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
    public void removeIfRelyData(Integer relyId, HttpServletRequest request) throws BusinessException {
        // 获取当前删除人userId
        int userId = loginUserInfo.getUserId(request);
        InterfaceCaseRelyDataVO ifRelyData = this.findIfRelyData(relyId);
        Byte deletable = ifRelyData.getOthersDeletable();
        Integer creatorId = ifRelyData.getCreatorId();
        if (deletable == null ||  creatorId == null) {
            throw new BusinessException("仅允许创建人删除");
        }
        if (creatorId != userId && deletable.intValue() == 1) {
            throw new BusinessException("仅允许创建人删除");
        }
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
        LOG.info("------------------------------进入无下标取值模式------------------------------");
        // 根据caseId调用相应case
        LOG.info("根据caseId调用相应case，caseId={}", caseId);

        String chainNo = NoUtil.genChainNo();
        Integer executeLogId = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId, executor,
                null, chainNo, null, (byte) 1, null, null,
                null, null, (byte)1, null));
        // redisUtil.stackPush(chainNo, executeLogId); delete

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
                if (jsonPathArray.size() == 1) {
                    Object o = jsonPathArray.get(0);
                    String s = o == null ? null : o.toString();
                    LOG.info("提取内容为：{}",s);
                    return s;
                } else {
                    LOG.info("提取内容为：{}", JSON.toJSONString(jsonPathArray));
                    return JSON.toJSONString(jsonPathArray);
                }
            } else if (contentType == 1) { // html
                LOG.info("------------------------------根据xpath取值");
                ArrayList xpathArray = JSONObject.parseObject(ParseUtil.parseXml(responseBody, expression), ArrayList.class);
                if (xpathArray.isEmpty()) {
                    LOG.warn("预检接口依赖，提取方式为xpath，提取内容为空，expression={}", expression);
                    throw new ParseException(expression + "提取内容为空");
                }
                if (xpathArray.size() == 1) {
                    Object o = xpathArray.get(0);
                    String s = o == null ? null : o.toString();
                    LOG.info("提取内容为：{}",s);
                    return s;
                } else {
                    LOG.info("提取内容为：{}", JSON.toJSONString(xpathArray));
                    return JSON.toJSONString(xpathArray);
                }
            } else if (contentType == 2) { // headers
                LOG.info("------------------------------根据header取值");
                JSONArray headerArray = (JSONArray) JSONObject.parseObject(responseHeaders,
                        HashMap.class).get(expression);
                if (headerArray == null || headerArray.isEmpty()) {
                    LOG.warn("预检接口依赖，提取方式为headers，未找到请求头，expression={}",expression);
                    throw new ParseException("未找到请求头:" + expression);
                } else {
                    if (headerArray.size() == 1) {
                        Object o = headerArray.get(0);
                        String s = o == null ? null : o.toString();
                        LOG.info("提取内容为：{}",s);
                        return s;
                    } else {
                        LOG.info("提取内容为：{}", JSON.toJSONString(headerArray));
                        return JSON.toJSONString(headerArray);
                    }
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
