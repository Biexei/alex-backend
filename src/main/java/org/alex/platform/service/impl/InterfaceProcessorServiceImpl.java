package org.alex.platform.service.impl;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.InterfaceProcessorMapper;
import org.alex.platform.pojo.InterfaceProcessorDO;
import org.alex.platform.pojo.InterfaceProcessorDTO;
import org.alex.platform.pojo.InterfaceProcessorVO;
import org.alex.platform.service.InterfaceProcessorService;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InterfaceProcessorServiceImpl implements InterfaceProcessorService {
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceProcessorServiceImpl.class);
    @Autowired
    private InterfaceProcessorMapper interfaceProcessorMapper;
    @Autowired
    private InterfaceCaseMapper interfaceCaseMapper;

    /**
     * 根据后置处理器名称查询详情
     *
     * @param name name
     * @return PostProcessorVO
     */
    @Override
    public InterfaceProcessorVO findInterfaceProcessorByName(String name) {
        return interfaceProcessorMapper.selectInterfaceProcessorByName(name);
    }

    /**
     * 根据后置处理器主键查询详情
     *
     * @param processorId postProcessorId
     * @return PostProcessorVO
     */
    @Override
    public InterfaceProcessorVO findInterfaceProcessorById(Integer processorId) {
        return interfaceProcessorMapper.selectInterfaceProcessorById(processorId);
    }

    /**
     * 获取测试用例包含的所有后置处理器
     *
     * @param caseId 测试用例编号
     * @return 后置处理器list
     */
    @Override
    public List<Integer> findInterfaceProcessorIdByCaseId(Integer caseId) {
        return interfaceProcessorMapper.selectInterfaceProcessorIdByCaseId(caseId);
    }

    /**
     * 检查名称唯一性
     *
     * @param processorId processorId
     * @param name            name
     * @return List<PostProcessorVO>
     */
    @Override
    public List<InterfaceProcessorVO> checkInterfaceProcessorName(Integer processorId, String name) {
        return interfaceProcessorMapper.checkInterfaceProcessorName(processorId, name);
    }

    /**
     * 获取后置处理器列表
     *
     * @param interfaceProcessorDTO postProcessorDTO
     * @return List<PostProcessorVO>
     */
    @Override
    public List<InterfaceProcessorVO> findInterfaceProcessorList(InterfaceProcessorDTO interfaceProcessorDTO) {
        return interfaceProcessorMapper.selectInterfaceProcessorList(interfaceProcessorDTO);
    }

    /**
     * 新增后置处理器
     *
     * @param interfaceProcessorDO postProcessorDO
     * @return PostProcessorDO 自增对象
     * @throws BusinessException name唯一性和caseId存在性检查
     */
    @Override
    public InterfaceProcessorDO saveInterfaceProcessor(InterfaceProcessorDO interfaceProcessorDO) throws BusinessException {
        // 数据校验
        this.checkDO(interfaceProcessorDO);

        Date date = new Date();
        interfaceProcessorDO.setCreatedTime(date);
        interfaceProcessorDO.setUpdateTime(date);
        // 0.检查缺省值
        Byte haveDefaultValue = interfaceProcessorDO.getHaveDefaultValue();
        String defaultValue = interfaceProcessorDO.getDefaultValue();
        if (haveDefaultValue == 0) { // 存在缺省值, 则缺省值不允许为空
            if (defaultValue == null) {
                throw new BusinessException("缺省值不能为空");
            }
        } else if (haveDefaultValue == 1) { // 不存在缺省值, 将defaultValue置为null
            interfaceProcessorDO.setDefaultValue(null);
        } else {
            throw new BusinessException("haveDefaultValue参数值非法");
        }
        // 1.检查caseId合法性
        Integer caseId = interfaceProcessorDO.getCaseId();
        if (caseId == null) {
            LOG.error("新增后置处理器时，后置处理器用例编号为空");
            throw new BusinessException("后置处理器用例编号不能为空");
        }
        if (interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId) == null) {
            LOG.error("新增后置处理器时，后置处理器用例编号不存在");
            throw new BusinessException("后置处理器用例编号不存在");
        }
        // 2.检查name唯一性
        String name = interfaceProcessorDO.getName();
        if (findInterfaceProcessorByName(name) != null) {
            LOG.info("新增后置处理器时，后置处理器名称重复，name={}", name);
            throw new BusinessException("后置处理器名称重复");
        }
        interfaceProcessorMapper.insertInterfaceProcessor(interfaceProcessorDO);
        return interfaceProcessorDO;
    }

    /**
     * 修改后置处理器
     *
     * @param interfaceProcessorDO postProcessorDO
     * @throws BusinessException name唯一性和caseId存在性检查
     */
    @Override
    public void modifyInterfaceProcessor(InterfaceProcessorDO interfaceProcessorDO) throws BusinessException {
        // 数据校验
        this.checkDO(interfaceProcessorDO);

        interfaceProcessorDO.setUpdateTime(new Date());
        // 0.检查缺省值
        Byte haveDefaultValue = interfaceProcessorDO.getHaveDefaultValue();
        String defaultValue = interfaceProcessorDO.getDefaultValue();
        if (haveDefaultValue == 0) { // 存在缺省值, 则缺省值不允许为空
            if (defaultValue == null) {
                throw new BusinessException("缺省值不能为空");
            }
        } else if (haveDefaultValue == 1) { // 不存在缺省值, 将defaultValue置为null
            interfaceProcessorDO.setDefaultValue(null);
        } else {
            throw new BusinessException("haveDefaultValue参数值非法");
        }
        // 1.检查caseId合法性
        Integer caseId = interfaceProcessorDO.getCaseId();
        if (caseId == null) {
            LOG.error("修改后置处理器时，后置处理器用例编号为空");
            throw new BusinessException("后置处理器用例编号不能为空");
        }
        if (interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId) == null) {
            LOG.error("修改后置处理器时，后置处理器用例编号不存在");
            throw new BusinessException("后置处理器用例编号不存在");
        }
        // 2.检查name唯一性
        String name = interfaceProcessorDO.getName();
        Integer postProcessorId = interfaceProcessorDO.getProcessorId();
        if (!checkInterfaceProcessorName(postProcessorId, name).isEmpty()) {
            LOG.info("修改后置处理器时，后置处理器名称重复，name={}", name);
            throw new BusinessException("后置处理器名称重复");
        }
        interfaceProcessorMapper.updateInterfaceProcessor(interfaceProcessorDO);
    }

    /**
     * 删除后置处理器
     *
     * @param processorId processorId
     */
    @Override
    public void removeInterfaceProcessorById(Integer processorId) {
        interfaceProcessorMapper.deleteInterfaceProcessorById(processorId);
        LOG.debug("删除后置处理器，postProcessorId={}", processorId);
    }

    /**
     * 根据caseId删除后置处理器
     *
     * @param caseId caseId
     */
    @Override
    public void removeInterfaceProcessorByCaseId(Integer caseId) {
        interfaceProcessorMapper.deleteInterfaceProcessorByCaseId(caseId);
        LOG.debug("删除后置处理器，caseId={}", caseId);
    }

    public void checkDO(InterfaceProcessorDO interfaceProcessorDO) throws ValidException {
        // 数据校验
        Integer caseId = interfaceProcessorDO.getCaseId();
        ValidUtil.notNUll(caseId, "用例编号不能为空");

        String name = interfaceProcessorDO.getName();
        ValidUtil.notNUll(name, "处理器名称不能为空");
        ValidUtil.notEmpty(name, "处理器名称不能为空");
        ValidUtil.length(name, 50, "处理器名称必须小于等于50");
        // 名称必须为字母数字下划线
        if (!name.matches("\\w+")) {
            throw new ValidException("处理器名称必须为字母数字下划线");
        }

        Byte type = interfaceProcessorDO.getType();
        ValidUtil.notNUll(type, "处理器提取类型不能为空");
        ValidUtil.size(type, 0, 6, "处理器提取类型方式为0~6");

        String expression = interfaceProcessorDO.getExpression();
        ValidUtil.notNUll(expression, "处理器提取表达式不能为空");
        ValidUtil.notEmpty(expression, "处理器提取表达式不能为空");
        ValidUtil.length(expression, 50, "处理器提取表达式必须小于等于50");

        if (type == 0) {
            ValidUtil.isJsonPath(expression);
        } else if (type == 1) {
            ValidUtil.isXpath(expression);
        } else if (type >= 4 && type <= 6) {
            ValidUtil.isJsonPath(expression);
        }
    }
}
