package org.alex.platform.service.impl;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceAssertMapper;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.pojo.InterfaceAssertDO;
import org.alex.platform.pojo.InterfaceAssertVO;
import org.alex.platform.service.InterfaceAssertService;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InterfaceAssertServiceImpl implements InterfaceAssertService {
    @Autowired
    InterfaceAssertMapper interfaceAssertMapper;
    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceAssertServiceImpl.class);

    /**
     * 新增断言
     *
     * @param interfaceAssertDO interfaceAssertDO
     * @throws BusinessException 断言排序重复/用例编号不存在检查
     */
    @Override
    public void saveAssert(InterfaceAssertDO interfaceAssertDO) throws BusinessException {
        // 校验数据
        this.checkDO(interfaceAssertDO);

        Integer caseId = interfaceAssertDO.getCaseId();
        //判断caseId下是否已经存在相同order
        InterfaceAssertDO assertDO = new InterfaceAssertDO();
        assertDO.setOrder(interfaceAssertDO.getOrder());
        assertDO.setCaseId(interfaceAssertDO.getCaseId());
        //判断caseId是否存在
        if (interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId) == null) {
            LOG.warn("新增断言，用例编号不存在，caseId={}", caseId);
            throw new BusinessException("用例编号不存在");
        }
        Date date = new Date();
        interfaceAssertDO.setCreatedTime(date);
        interfaceAssertDO.setUpdateTime(date);
        if (interfaceAssertDO.getType() == 3) { //提取数据类型   0json/1html/2header/3responseCode
            interfaceAssertDO.setExpression(null);
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
        // 校验数据
        this.checkDO(interfaceAssertDO);

        //判断caseId是否存在
        Integer caseId = interfaceAssertDO.getCaseId();
        if (interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId) == null) {
            LOG.warn("修改断言，用例编号不存在，caseId={}", caseId);
            throw new BusinessException("用例编号不存在");
        }
        if (interfaceAssertDO.getType() == 3) { //提取数据类型   0json/1html/2header/3responseCode
            interfaceAssertDO.setExpression(null);
        }
        interfaceAssertDO.setUpdateTime(new Date());
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

    public void checkDO(InterfaceAssertDO interfaceAssertDO) throws ValidException {
        Integer caseId = interfaceAssertDO.getCaseId();
        ValidUtil.notNUll(caseId, "用例编号不能为空");

        String assertName = interfaceAssertDO.getAssertName();
        ValidUtil.notNUll(assertName, "断言名称不能为空");
        ValidUtil.notEmpty(assertName, "断言名称不能为空");
        ValidUtil.length(assertName, 100, "断言名称长度必须小于100");

        Byte type = interfaceAssertDO.getType();
        ValidUtil.notNUll(type, "提取数据类型不能为空");
        ValidUtil.size(type, 0, 4,"提取数据类型必须为0~4");

        String expression = interfaceAssertDO.getExpression();
        if (type < 3) { // 提取数据类型   0json/1html/2header/3responseCode/4runtime
            ValidUtil.notNUll(expression, "提取表达式不能为空");
            ValidUtil.notEmpty(expression, "提取表达式不能为空");
            ValidUtil.length(expression, 50, "提取表达式长度必须小于50");
        }

        Byte operator = interfaceAssertDO.getOperator();
        ValidUtil.notNUll(operator, "操作符不能为空");
        ValidUtil.size(operator, 0, 12,"操作符必须为0~12");

        String exceptedResult = interfaceAssertDO.getExceptedResult();
        if (operator != 8 && operator != 9 && operator != 11 && operator != 12) {
            ValidUtil.notNUll(exceptedResult, "预期结果不能为空");
            ValidUtil.notEmpty(exceptedResult, "预期结果不能为空");
        }
        ValidUtil.length(exceptedResult, 0, 1000,"预期结果长度必须小于1000");

        Integer order = interfaceAssertDO.getOrder();
        ValidUtil.notNUll(order, "排序不能为空");

        // 检查 0json/1html/2header/3responsecode
        if (type == 0) {
            ValidUtil.isJsonPath(expression);
        } else if (type == 1) {
            ValidUtil.isXpath(expression);
        } else if (type == 3) {
            try {
                Integer.valueOf(exceptedResult);
            } catch (NumberFormatException e) {
                throw new ValidException("响应状态码预期结果错误");
            }
        }
    }
}
