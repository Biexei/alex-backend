package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.InterfaceCaseRelyDataMapper;
import org.alex.platform.mapper.RelyDataMapper;
import org.alex.platform.pojo.InterfaceCaseRelyDataDO;
import org.alex.platform.pojo.InterfaceCaseRelyDataDTO;
import org.alex.platform.pojo.InterfaceCaseRelyDataVO;
import org.alex.platform.service.InterfaceCaseRelyDataService;
import org.alex.platform.service.InterfaceCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterfaceCaseRelyDataServiceImpl implements InterfaceCaseRelyDataService {
    @Autowired
    InterfaceCaseRelyDataMapper ifRelyDataMapper;
    @Autowired
    InterfaceCaseService ifCaseService;
    @Autowired
    RelyDataMapper relyDataMapper;

    @Override
    public void saveIfRelyData(InterfaceCaseRelyDataDO ifRelyDataDO) throws BusinessException {
        // 判断relyCaseId是否存在
        Integer caseId = ifRelyDataDO.getRelyCaseId();
        if (ifCaseService.findInterfaceCaseByCaseId(caseId) == null) {
            throw new BusinessException("relyCaseId不存在");
        }
        // 判断relyName是否已存在
        String relyName = ifRelyDataDO.getRelyName();
        if (relyDataMapper.selectRelyDataByName(relyName) != null){
            throw new BusinessException("依赖名称已存在与其它依赖");
        }
        if (this.findIfRelyDataByName(relyName) != null) {
            throw new BusinessException("依赖名称已存在与接口依赖");
        }
        ifRelyDataMapper.insertIfRelyData(ifRelyDataDO);
    }

    @Override
    public void modifyIfRelyData(InterfaceCaseRelyDataDO ifRelyDataDO) throws BusinessException {
        // 判断relyCaseId是否存在
        Integer caseId = ifRelyDataDO.getRelyCaseId();
        if (ifCaseService.findInterfaceCaseByCaseId(caseId) == null) {
            throw new BusinessException("relyCaseId不存在");
        }
        // 判断relyName是否已存在
        String relyName = ifRelyDataDO.getRelyName();
        if (relyDataMapper.selectRelyDataByName(relyName) != null){
            throw new BusinessException("依赖名称已存在与其它依赖");
        }
        ifRelyDataMapper.updateIfRelyData(ifRelyDataDO);
    }

    @Override
    public PageInfo<InterfaceCaseRelyDataVO> findIfRelyDataList(InterfaceCaseRelyDataDTO ifRelyDataDTO,
                                                                Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(ifRelyDataMapper.selectIfRelyDataList(ifRelyDataDTO));
    }

    @Override
    public InterfaceCaseRelyDataVO findIfRelyData(Integer relyId) {
        return ifRelyDataMapper.selectIfRelyDataById(relyId);
    }

    @Override
    public InterfaceCaseRelyDataVO findIfRelyDataByName(String relyName) {
        return ifRelyDataMapper.selectIfRelyDataByName(relyName);
    }

    @Override
    public void removeIfRelyData(Integer relyId) {
        ifRelyDataMapper.deleteIfRelyData(relyId);
    }
}
