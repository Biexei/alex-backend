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

import java.util.ArrayList;
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
        // 校验前置用例合法性
        this.validatePreCaseForSave(interfacePreCaseDO);
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
        if (interfacePreCaseDO.getPreCaseId().equals(interfacePreCaseDO.getParentCaseId())) {
            LOG.warn("前置用例应不能等于用例自身编号");
            throw new BusinessException("前置用例不能为本身，请重新选择前置用例");
        }
        // 校验前置用例合法性
        this.validatePreCaseForModify(interfacePreCaseDO);
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

    /**
     * 查询用例的前置用例关联表自增编号集合
     * @param parentCaseId 用例编号
     * @return 前置用例集合
     */
    @Override
    public List<Integer> findInterfacePreIdByParentId(Integer parentCaseId) {
        return interfacePreCaseMapper.selectInterfacePreIdByParentId(parentCaseId);
    }

    /**
     * 查询用例的前置用例编号集合
     * @param parentCaseId 用例编号
     * @return 前置用例集合
     */
    @Override
    public List<Integer> findInterfacePreCaseIdByParentId(Integer parentCaseId) {
        return interfacePreCaseMapper.selectInterfacePreCaseIdByParentId(parentCaseId);
    }

    /**
     * 递归获取所有的前置用例（含前置用例包含的前置用例）集合
     * @param returnResult 用来接收返回数据的集合
     * @param parentCaseId 父用例编号
     * @return 取所有的前置用例（含前置用例包含的前置用例）集合
     */
    public List<Integer> recursionPreCase(List<Integer> returnResult, Integer parentCaseId) {
        List<Integer> list = this.findInterfacePreCaseIdByParentId(parentCaseId);
        returnResult.addAll(list);
        for (Integer pId : list) {
            List<Integer> l = recursionPreCase(new ArrayList<Integer>(), pId);
            returnResult.addAll(l);
        }
        return returnResult;
    }

    private void checkDO(InterfacePreCaseDO interfacePreCaseDO) throws ValidException {
        ValidUtil.notNUll(interfacePreCaseDO.getPreCaseId(), "前置用例编号不能为空");
    }

    /**
     * 校验前置用例（含前置用例的前置用例）是否包含自身(新增用)
     * @param interfacePreCaseDO interfacePreCaseDO
     * @throws BusinessException 前置用例（含前置用例的前置用例）不能包含本身
     */
    private void validatePreCaseForSave(InterfacePreCaseDO interfacePreCaseDO) throws BusinessException {
        Integer parentCase = interfacePreCaseDO.getParentCaseId();
        Integer preCase = interfacePreCaseDO.getPreCaseId();
        // 校验父节点
        List<Integer> allPreCase1 = this.recursionPreCase(new ArrayList<Integer>(), parentCase);
        if (allPreCase1.contains(preCase)) {
            throw new BusinessException("前置用例（含前置用例的前置用例）不能包含本身");
        }
        // 校验自身节点
        List<Integer> allPreCase2 = this.recursionPreCase(new ArrayList<Integer>(), preCase);
        if (allPreCase2.contains(parentCase)) {
            throw new BusinessException("前置用例（含前置用例的前置用例）不能包含本身");
        }
    }

    /**
     * 校验前置用例（含前置用例的前置用例）是否包含自身(修改用)
     * @param interfacePreCaseDO interfacePreCaseDO
     * @throws BusinessException 前置用例（含前置用例的前置用例）不能包含本身
     */
    private void validatePreCaseForModify(InterfacePreCaseDO interfacePreCaseDO) throws BusinessException {
        Integer parentCase = interfacePreCaseDO.getParentCaseId();
        Integer preCase = interfacePreCaseDO.getPreCaseId();
        Integer id = interfacePreCaseDO.getId();
        // 校验父节点
        List<Integer> allPreCase1 = this.recursionPreCase(new ArrayList<Integer>(), parentCase);
        // 移除现有preCaseId
        List<Integer> preCaseIdList = interfacePreCaseMapper.selectInterfacePreCaseIdById(id);
        for (Integer preCaseId : preCaseIdList) {
            if (allPreCase1.contains(preCaseId)) {
                allPreCase1.remove(preCaseId);
            }
        }
        if (allPreCase1.contains(preCase)) {
            throw new BusinessException("前置用例（含前置用例的前置用例）不能包含本身/前置用例重复");
        }
        // 校验自身节点
        List<Integer> allPreCase2 = this.recursionPreCase(new ArrayList<Integer>(), preCase);
        if (allPreCase2.contains(parentCase)) {
            throw new BusinessException("前置用例（含前置用例的前置用例）不能包含本身/前置用例重复");
        }
    }
}
