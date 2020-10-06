package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.InterfaceCaseRelyDataMapper;
import org.alex.platform.mapper.RelyDataMapper;
import org.alex.platform.pojo.RelyDataDO;
import org.alex.platform.pojo.RelyDataDTO;
import org.alex.platform.pojo.RelyDataVO;
import org.alex.platform.service.RelyDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RelyDataServiceImpl implements RelyDataService {
    @Autowired
    RelyDataMapper relyDataMapper;
    @Autowired
    InterfaceCaseRelyDataMapper interfaceCaseRelyDataMapper;
    private static final Logger LOG = LoggerFactory.getLogger(RelyDataServiceImpl.class);

    @Override
    public void saveRelyData(RelyDataDO relyDataDO) throws BusinessException {
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
    public void modifyRelyData(RelyDataDO relyDataDO) throws BusinessException {
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
}
