package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceCaseRelyDataMapper;
import org.alex.platform.mapper.RelyDataMapper;
import org.alex.platform.pojo.RelyDataDO;
import org.alex.platform.pojo.RelyDataDTO;
import org.alex.platform.pojo.RelyDataVO;
import org.alex.platform.service.RelyDataService;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RelyDataServiceImpl implements RelyDataService {
    @Autowired
    RelyDataMapper relyDataMapper;
    @Autowired
    InterfaceCaseRelyDataMapper interfaceCaseRelyDataMapper;
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
    public void modifyRelyData(RelyDataDO relyDO) throws BusinessException {
        RelyDataDO relyDataDO = relyDataDOChecker(relyDO);
        String name = relyDataDO.getName();
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
    public void removeRelyDataById(Integer id) throws BusinessException {
        RelyDataVO relyDataVO = relyDataMapper.selectRelyDataById(id);
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
        // 依赖内容都不能包含依赖本身, 反射方法内部数据不做校验
        if (relyDataDO.getType() != 1) { //0固定值 1反射方法 2sql-select 3sql-insert 4sql-update 5sql-delete 6sql-script
            String sql = relyDataDO.getValue();
            Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
            Matcher matcher = pattern.matcher(sql);
            while (matcher.find()) {
                String findStr = matcher.group();
                String relyName = findStr.substring(2, findStr.length() - 1);
                if (relyName.contains("(") && relyName.contains(")")) {
                    int index = relyName.indexOf("(");
                    relyName = relyName.substring(0, index);
                }
                if (relyName.equals(name)) {
                    if (relyDataDO.getAnalysisRely().intValue() == 0) {// 使能解析依赖才检查
                        throw new BusinessException("禁止引用自身");
                    }
                }
                // 为防止嵌套引用，故暂不支持引用接口依赖
                // name不能在在于t_interface_case_rely_data
                if (null != interfaceCaseRelyDataMapper.selectIfRelyDataByName(relyName)) {
                    LOG.warn("禁止引用接口依赖：" + relyName);
                    throw new BusinessException("禁止引用接口依赖" );
                }

            }
        }
        if (type != 3) { // 非新增语句时，将enable_return 设为 null
            relyDataDO.setEnableReturn(null);
        }
        if (type == 1) { // 0固定值 1反射方法 2sql-select 3sql-insert 4sql-update 5sql-delete 6sql-script
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
