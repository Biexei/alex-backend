package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.InterfaceCaseSuiteMapper;
import org.alex.platform.pojo.InterfaceCaseSuiteDO;
import org.alex.platform.pojo.InterfaceCaseSuiteDTO;
import org.alex.platform.pojo.InterfaceCaseSuiteVO;
import org.alex.platform.service.InterfaceCaseSuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterfaceCaseSuiteServiceImpl implements InterfaceCaseSuiteService {
    @Autowired
    InterfaceCaseSuiteMapper interfaceCaseSuiteMapper;

    @Override
    public void saveInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO) {
        interfaceCaseSuiteMapper.insertInterfaceCaseSuite(interfaceCaseSuiteDO);
    }

    @Override
    public void modifyInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO) {
        interfaceCaseSuiteMapper.updateInterfaceCaseSuite(interfaceCaseSuiteDO);
    }

    @Override
    public void removeInterfaceCaseSuiteById(Integer suiteId) {
        interfaceCaseSuiteMapper.deleteInterfaceCaseSuiteById(suiteId);
    }

    @Override
    public InterfaceCaseSuiteVO findInterfaceCaseSuiteById(Integer suiteId) {
        return interfaceCaseSuiteMapper.selectInterfaceCaseSuiteById(suiteId);
    }

    @Override
    public PageInfo<InterfaceCaseSuiteVO> findInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO,
                                                                 Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(interfaceCaseSuiteMapper.selectInterfaceCaseSuite(interfaceCaseSuiteDTO));
    }
}
