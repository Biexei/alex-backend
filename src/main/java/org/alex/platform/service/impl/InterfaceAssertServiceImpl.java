package org.alex.platform.service.impl;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.InterfaceAssertMapper;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.pojo.InterfaceAssertDO;
import org.alex.platform.pojo.InterfaceAssertVO;
import org.alex.platform.service.InterfaceAssertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterfaceAssertServiceImpl implements InterfaceAssertService {
    @Autowired
    InterfaceAssertMapper interfaceAssertMapper;

    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;

    /**
     * 新增断言
     *
     * @param interfaceAssertDO interfaceAssertDO
     * @throws BusinessException 断言排序重复/用例编号不存在检查
     */
    @Override
    public void saveAssert(InterfaceAssertDO interfaceAssertDO) throws BusinessException {
        Integer caseId = interfaceAssertDO.getCaseId();
        //判断caseId下是否已经存在相同order
        InterfaceAssertDO assertDO = new InterfaceAssertDO();
        assertDO.setOrder(interfaceAssertDO.getOrder());
        assertDO.setCaseId(interfaceAssertDO.getCaseId());
        if (!interfaceAssertMapper.selectAssertList(assertDO).isEmpty()) {
            throw new BusinessException("断言排序重复");
        }
        //判断caseId是否存在
        if (interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId) == null) {
            throw new BusinessException("用例编号不存在");
        }
        interfaceAssertMapper.insertAssert(interfaceAssertDO);
    }

    /**
     * 修改断言
     *
     * @param interfaceAssertDO interfaceAssertDO
     * @throws BusinessException 断言排序重复/用例编号不存在检查
     */
    @Override
    public void modifyAssert(InterfaceAssertDO interfaceAssertDO) throws BusinessException {
        //判断caseId是否存在
        Integer caseId = interfaceAssertDO.getCaseId();
        if (interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId) == null) {
            throw new BusinessException("用例编号不存在");
        }
        //判断排序重复
        if (!interfaceAssertMapper.checkAssertType(interfaceAssertDO).isEmpty()) {
            throw new BusinessException("断言排序重复");
        }
        interfaceAssertMapper.updateAssert(interfaceAssertDO);
    }

    /**
     * 删除测试用例所有的断言
     *
     * @param caseId 测试用例编号
     */
    @Override
    public void removeAssertByCaseId(Integer caseId) {
        interfaceAssertMapper.deleteAssertByCaseId(caseId);
    }

    /**
     * 根据编号删除断言
     *
     * @param assertId 断言编号
     */
    @Override
    public void removeAssertByAssertId(Integer assertId) {
        interfaceAssertMapper.deleteAssertByAssertId(assertId);
    }
}
