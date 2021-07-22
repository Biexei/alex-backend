package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.core.parser.Parser;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceCaseRelyDataMapper;
import org.alex.platform.mapper.RelyDataMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.service.InterfacePreCaseService;
import org.alex.platform.service.RelyDataService;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RelyDataServiceImpl implements RelyDataService {
    @Autowired
    RelyDataMapper relyDataMapper;
    @Autowired
    LoginUserInfo loginUserInfo;
    @Autowired
    InterfaceCaseRelyDataMapper interfaceCaseRelyDataMapper;
    @Autowired
    Parser parser;
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfacePreCaseService interfacePreCaseService;
    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(RelyDataServiceImpl.class);

    @Override
    public void saveRelyData(RelyDataDO relyDO) throws BusinessException {
        RelyDataDO relyDataDO = relyDataDOChecker(relyDO);
        String name = relyDataDO.getName();
        // name不能在在于t_interface_case_rely_data
        if (null != interfaceCaseRelyDataMapper.selectIfRelyDataByName(name)) {
            LOG.warn("依赖名称已存在于接口依赖，relyName={}", name);
            throw new BusinessException("依赖名称已存在于接口依赖");
        }
        // name不能在在于t_rely_data
        if (null != relyDataMapper.selectRelyDataByName(name)) {
            LOG.warn("依赖名称已存在于其它依赖，relyName={}", name);
            throw new BusinessException("依赖名称已存在于其它依赖");
        }
        Date date = new Date();
        relyDataDO.setCreatedTime(date);
        relyDataDO.setUpdateTime(date);
        relyDataMapper.insertRelyData(relyDataDO);
    }

    @Override
    public void modifyRelyData(RelyDataDO relyDO, HttpServletRequest request) throws BusinessException {
        RelyDataDO relyDataDO = relyDataDOChecker(relyDO);
        String name = relyDataDO.getName();

        // 获取当前编辑人userId
        int userId = loginUserInfo.getUserId(request);
        Integer relyId = relyDataDO.getId();
        RelyDataVO relyDataVO = this.findRelyDataById(relyId);
        Byte type = relyDataVO.getType();
        Byte modifiable = relyDataVO.getOthersModifiable();
        Integer creatorId = relyDataVO.getCreatorId();
        if (type != 1) { // 反射方法
            if (modifiable == null || creatorId == null) {
                throw new BusinessException("仅允许创建人修改");
            }
            if (creatorId != userId) {
                if (modifiable.intValue() == 1) {
                    throw new BusinessException("仅允许创建人修改");
                }
                // 当前编辑人与创建人不一致时，不允许修改othersModifiable和othersDeletable字段
                relyDataVO.setOthersDeletable(null);
                relyDataVO.setOthersModifiable(null);
            }
        }

        // name不能在在于t_interface_case_rely_data
        if (null != interfaceCaseRelyDataMapper.selectIfRelyDataByName(name)) {
            LOG.warn("依赖名称已存在于接口依赖，relyName={}", name);
            throw new BusinessException("依赖名称已存在于接口依赖");
        }
        // name不能在在于t_rely_data
        if (!relyDataMapper.checkName(relyDataDO).isEmpty()) {
            LOG.warn("依赖名称已存在于其它依赖，relyName={}", name);
            throw new BusinessException("依赖名称已存在于其它依赖");
        }
        Date date = new Date();
        relyDataDO.setUpdateTime(date);
        // type 依赖类型 0固定值 1反射方法 2sql
        relyDataMapper.updateRelyData(relyDataDO);
    }

    /**
     * 查看其它依赖详情
     * @param id 编号
     * @return RelyDataVO
     */
    @Override
    public RelyDataVO findRelyDataById(Integer id) {
        return relyDataMapper.selectRelyDataById(id);
    }

    /**
     * 根据名称查看详情
     * @param name 名称
     * @return RelyDataVO
     */
    @Override
    public RelyDataVO findRelyDataByName(String name) {
        return relyDataMapper.selectRelyDataByName(name);
    }

    /**
     * 查看其它依赖列表
     * @param relyDataDTO relyDataDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo<RelyDataVO>
     */
    @Override
    public PageInfo<RelyDataVO> findRelyDataList(RelyDataDTO relyDataDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(relyDataMapper.selectRelyDataList(relyDataDTO));
    }

    /**
     * 删除其它依赖
     * @param id 编号
     * @throws BusinessException BusinessException
     */
    @Override
    public void removeRelyDataById(Integer id, HttpServletRequest request) throws BusinessException {
        RelyDataVO relyDataVO = relyDataMapper.selectRelyDataById(id);
        // 获取当前删除人userId
        int userId = loginUserInfo.getUserId(request);
        Byte deletable = relyDataVO.getOthersDeletable();
        Integer creatorId = relyDataVO.getCreatorId();
        if (creatorId == null) {
            throw new BusinessException("仅允许创建人删除");
        }
        if (creatorId != userId && deletable.intValue() == 1) {
            throw new BusinessException("仅允许创建人删除");
        }
        // 依赖类型 0固定值 1反射方法 2sql 反射方法不允许删除
        if (relyDataVO.getType() == 1) {
            LOG.warn("预置方法不允许删除");
            throw new BusinessException("预置方法不允许删除");
        }
        relyDataMapper.deleteRelyDataById(id);
    }

    /**
     * DO校验器 参数校验以及参数处理
     * @param relyDataDO relyDataDO
     * @return DO
     * @throws ValidException 参数校验
     */
    private RelyDataDO relyDataDOChecker(RelyDataDO relyDataDO) throws BusinessException {
        String name = relyDataDO.getName();
        Byte type = relyDataDO.getType();
        String value = relyDataDO.getValue();
        // 固定值、反射方法内部数据不做校验
        if (type > 1) { //0固定值 1反射方法 2sql-select 3sql-insert 4sql-update 5sql-delete 6sql-script

            ArrayList<String> dependencyNameList = parser.extractDependencyName(value);

            for (String dependencyName : dependencyNameList) {
                // 依赖值中不能包含当前依赖名称
                if (dependencyName.equals(name)) {
                    throw new BusinessException("content prohibited current dependency");
                }

                // 依赖值中不能包含SQL依赖
                RelyDataVO relyDataVO = relyDataMapper.selectRelyDataByName(dependencyName);
                if (relyDataVO != null) {
                    Byte relyType = relyDataVO.getType();
                    if (relyType.intValue() > 1) {
                        throw new BusinessException("content prohibited SQL dependency");
                    }

                    InterfaceCaseRelyDataVO interfaceCaseRelyDataVO = interfaceCaseRelyDataMapper.selectIfRelyDataByName(dependencyName);
                    if (null != interfaceCaseRelyDataVO) {
                        // 如果是接口依赖，确保接口依赖本身的headers、params、form-data、form-data-encoded、raw、断言预期结果不包含当前依赖名称
                        Integer caseId = interfaceCaseRelyDataVO.getRelyCaseId();
                        InterfaceCaseInfoVO caseInfo = interfaceCaseService.findInterfaceCaseByCaseId(caseId);
                        String headers = caseInfo.getHeaders();
                        String params = caseInfo.getParams();
                        String formData = caseInfo.getFormData();
                        String formDataEncoded = caseInfo.getFormDataEncoded();
                        String raw = caseInfo.getRaw();
                        List<InterfaceAssertVO> asserts = caseInfo.getAsserts();
                        if (parser.extractDependencyName(headers).contains(name)) {
                            throw new BusinessException("内容中的接口依赖的header不能引用当前依赖名称");
                        }
                        if (parser.extractDependencyName(params).contains(name)) {
                            throw new BusinessException("内容中的接口依赖的params不能引用当前依赖名称");
                        }
                        if (parser.extractDependencyName(formData).contains(name)) {
                            throw new BusinessException("内容中的接口依赖的formData不能引用当前依赖名称");
                        }
                        if (parser.extractDependencyName(formDataEncoded).contains(name)) {
                            throw new BusinessException("内容中的接口依赖的formDataEncoded不能引用当前依赖名称");
                        }
                        if (parser.extractDependencyName(raw).contains(name)) {
                            throw new BusinessException("内容中的接口依赖的raw不能引用当前依赖名称");
                        }
                        for (InterfaceAssertVO interfaceAssertVO : asserts) {
                            String exceptedResult = interfaceAssertVO.getExceptedResult();
                            if (parser.extractDependencyName(exceptedResult).contains(name)) {
                                throw new BusinessException("内容中的接口依赖的断言预期结果不能引用当前依赖名称");
                            }
                        }
                        // 如果是接口依赖，确保接口依赖前置用例的headers、params、form-data、form-data-encoded、raw、断言预期结果不包含当前依赖名称
                        List<Integer> preCaseIdList = interfacePreCaseService.findInterfacePreIdByParentId(caseId);
                        for (Integer preCaseId : preCaseIdList) {
                            InterfaceCaseInfoVO preCaseInfo = interfaceCaseService.findInterfaceCaseByCaseId(preCaseId);
                            String preHeaders = preCaseInfo.getHeaders();
                            String preParams = preCaseInfo.getParams();
                            String preFormData = preCaseInfo.getFormData();
                            String preFormDataEncoded = preCaseInfo.getFormDataEncoded();
                            String preRaw = preCaseInfo.getRaw();
                            List<InterfaceAssertVO> preCaseInfoAsserts = preCaseInfo.getAsserts();
                            if (parser.extractDependencyName(preHeaders).contains(name)) {
                                throw new BusinessException("内容中的接口依赖的前置用例的header不能引用当前依赖名称");
                            }
                            if (parser.extractDependencyName(preParams).contains(name)) {
                                throw new BusinessException("内容中的接口依赖的前置用例的params不能引用当前依赖名称");
                            }
                            if (parser.extractDependencyName(preFormData).contains(name)) {
                                throw new BusinessException("内容中的接口依赖的前置用例的formData不能引用当前依赖名称");
                            }
                            if (parser.extractDependencyName(preFormDataEncoded).contains(name)) {
                                throw new BusinessException("内容中的接口依赖的前置用例的formDataEncoded不能引用当前依赖名称");
                            }
                            if (parser.extractDependencyName(preRaw).contains(name)) {
                                throw new BusinessException("内容中的接口依赖的前置用例的raw不能引用当前依赖名称");
                            }
                            for (InterfaceAssertVO interfaceAssertVO : preCaseInfoAsserts) {
                                String exceptedResult = interfaceAssertVO.getExceptedResult();
                                if (parser.extractDependencyName(exceptedResult).contains(name)) {
                                    throw new BusinessException("内容中的接口依赖的前置用例的断言预期结果不能引用当前依赖名称");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (type != 3) { // 非新增语句时，将enable_return 设为 null
            relyDataDO.setEnableReturn(null);
        }
        if (type != 1) {
            ValidUtil.notNUll(relyDataDO.getOthersModifiable(), "请选择是否允许其他人编辑");
            ValidUtil.notNUll(relyDataDO.getOthersDeletable(), "请选择是否允许其他人删除");
            ValidUtil.size(relyDataDO.getOthersModifiable(), 0, 1, "请选择是否允许其他人编辑");
            ValidUtil.size(relyDataDO.getOthersDeletable(), 0, 1, "请选择是否允许其他人删除");
        }
        if (type < 2) { // 0固定值 1反射方法 2sql-select 3sql-insert 4sql-update 5sql-delete 6sql-script
            relyDataDO.setAnalysisRely(null);
        } else {
            ValidUtil.notNUll(relyDataDO.getAnalysisRely(), "是否解析依赖不能为空");
        }
        // 校验参数
        Byte analysisRely = relyDataDO.getAnalysisRely();
        Byte enableReturn = relyDataDO.getEnableReturn();
        if (analysisRely != null) {
            ValidUtil.size(analysisRely, 0, 1, "参数非法");
        }
        if (enableReturn != null) {
            ValidUtil.size(enableReturn, 0, 1, "参数非法");
        }
        return relyDataDO;
    }
}
