package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.InterfaceSuiteCaseRefMapper;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDTO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefVO;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceSuiteCaseRefServiceImpl implements InterfaceSuiteCaseRefService {
    @Autowired
    InterfaceSuiteCaseRefMapper  interfaceSuiteCaseRefMapper;

    @Override
    public void saveSuiteCase(List<InterfaceSuiteCaseRefDO> interfaceSuiteCaseRefDOList) {
        interfaceSuiteCaseRefMapper.insertSuiteCase(interfaceSuiteCaseRefDOList);
    }

    @Override
    public void removeSuiteCase(Integer incrementKey) {
        interfaceSuiteCaseRefMapper.deleteSuiteCase(incrementKey);
    }

    @Override
    public PageInfo<InterfaceSuiteCaseRefVO> findSuiteCaseList(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(interfaceSuiteCaseRefMapper.selectSuiteCaseList(interfaceSuiteCaseRefDTO));
    }
}
