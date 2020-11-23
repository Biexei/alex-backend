package org.alex.platform.service.impl;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.InterfacePreCaseMapper;
import org.alex.platform.pojo.InterfaceCaseDO;
import org.alex.platform.pojo.InterfacePreCaseDO;
import org.alex.platform.service.InterfacePreCaseService;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InterfacePreCaseServiceImpl implements InterfacePreCaseService {
    @Autowired
    InterfacePreCaseMapper interfacePreCaseMapper;
    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;
    private static final Logger LOG = LoggerFactory.getLogger(InterfacePreCaseServiceImpl.class);

    /**
     * 保存前置用例
     * @param interfacePreCaseDO interfacePreCaseDO
     */
    @Override
    public void saveInterfacePreCase(InterfacePreCaseDO interfacePreCaseDO) throws BusinessException {
        this.checkDO(interfacePreCaseDO);
        // 查询是否存在于用例表
        Integer preCaseId = interfacePreCaseDO.getPreCaseId();
        InterfaceCaseDO interfaceCaseDO = interfaceCaseMapper.selectInterfaceCase(preCaseId);
        if (interfaceCaseDO == null) {
            LOG.error("参数非法,前置用例={}不存在", preCaseId);
            throw new BusinessException("参数非法,前置用例不存在");
        }
        Date date = new Date();
        interfacePreCaseDO.setCreatedTime(date);
        interfacePreCaseDO.setUpdateTime(date);
        interfacePreCaseMapper.insertInterfacePreCase(interfacePreCaseDO);
    }

    /**
     * 修改某个前置用例记录
     * @param interfacePreCaseDO interfacePreCaseDO
     */
    @Override
    public void modifyInterfacePreCase(InterfacePreCaseDO interfacePreCaseDO) throws BusinessException {
        this.checkDO(interfacePreCaseDO);
        // 查询是否存在于用例表
        Integer preCaseId = interfacePreCaseDO.getPreCaseId();
        InterfaceCaseDO interfaceCaseDO = interfaceCaseMapper.selectInterfaceCase(preCaseId);
        if (interfaceCaseDO == null) {
            LOG.error("参数非法,前置用例={}不存在", preCaseId);
            throw new BusinessException("参数非法,前置用例不存在");
        }
        interfacePreCaseDO.setUpdateTime(new Date());
        if(interfacePreCaseDO.getPreCaseId().equals(interfacePreCaseDO.getParentCaseId())) {
            LOG.warn("前置用例应不能等于用例自身编号");
            throw new BusinessException("请重新选择前置用例编号");
        }
        interfacePreCaseMapper.updateInterfacePreCase(interfacePreCaseDO);
    }

    /**
     * 查询用例所有的前置用例
     * @param parentCaseId 父用例ID
     * @return List<InterfacePreCaseDO>
     */
    @Override
    public List<InterfacePreCaseDO> findInterfacePreCaseByParentId(Integer parentCaseId) {
        return interfacePreCaseMapper.selectInterfacePreCaseByParentId(parentCaseId);
    }

    /**
     * 删除某个前置用例
     * @param id id
     */
    @Override
    public void removeInterfacePreCaseById(Integer id) {
        interfacePreCaseMapper.deleteInterfacePreCaseById(id);
    }

    /**
     * 删除用例所有的前置用例
     * @param parentCaseId 父用例ID
     */
    @Override
    public void removeInterfacePreCaseByParentId(Integer parentCaseId) {
        interfacePreCaseMapper.deleteInterfacePreCaseByParentId(parentCaseId);
    }

    @Override
    public List<Integer> findInterfacePreCaseIdByParentId(Integer id) {
        return interfacePreCaseMapper.selectInterfacePreCaseIdByParentId(id);
    }

    private void checkDO(InterfacePreCaseDO interfacePreCaseDO) throws ValidException {
        ValidUtil.notNUll(interfacePreCaseDO.getPreCaseId(), "前置用例编号不能为空");
    }
}
