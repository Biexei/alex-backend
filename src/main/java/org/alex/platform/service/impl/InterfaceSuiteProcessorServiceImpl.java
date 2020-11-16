package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceSuiteProcessorMapper;
import org.alex.platform.pojo.InterfaceSuiteProcessorDO;
import org.alex.platform.pojo.InterfaceSuiteProcessorDTO;
import org.alex.platform.pojo.InterfaceSuiteProcessorVO;
import org.alex.platform.service.InterfaceSuiteProcessorService;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceSuiteProcessorServiceImpl implements InterfaceSuiteProcessorService {
    @Autowired
    InterfaceSuiteProcessorMapper interfaceSuiteProcessorMapper;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceSuiteProcessorServiceImpl.class);

    /**
     * 添加处理器
     * @param interfaceSuiteProcessorDO interfaceSuiteProcessorDO
     * @return 自增DO
     * @throws ValidException 参数校验
     */
    @Override
    public InterfaceSuiteProcessorDO saveInterfaceSuiteProcessor(InterfaceSuiteProcessorDO interfaceSuiteProcessorDO) throws ValidException {
        // 校验参数
        checkDO(interfaceSuiteProcessorDO);
        // 保存
        interfaceSuiteProcessorMapper.insertInterfaceSuiteProcessor(interfaceSuiteProcessorDO);
        return interfaceSuiteProcessorDO;
    }

    /**
     * 修改处理器
     * @param interfaceSuiteProcessorDO interfaceSuiteProcessorDO
     * @throws ValidException 参数校验
     */
    @Override
    public void modifyInterfaceSuiteProcessor(InterfaceSuiteProcessorDO interfaceSuiteProcessorDO) throws ValidException {
        // 校验参数
        checkDO(interfaceSuiteProcessorDO);
        // 修改
        interfaceSuiteProcessorMapper.updateInterfaceSuiteProcessor(interfaceSuiteProcessorDO);
    }

    /**
     * 获取处理器列表不分页
     * @param interfaceSuiteProcessorDTO interfaceSuiteProcessorDTO
     * @return List<InterfaceSuiteProcessorVO>
     */
    @Override
    public List<InterfaceSuiteProcessorVO> findAllInterfaceSuiteProcessorList(InterfaceSuiteProcessorDTO interfaceSuiteProcessorDTO) {
        return interfaceSuiteProcessorMapper.selectInterfaceSuiteProcessorList(interfaceSuiteProcessorDTO);
    }

    /**
     * 获取处理器列表分页
     * @param interfaceSuiteProcessorDTO interfaceSuiteProcessorDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo<InterfaceSuiteProcessorVO>
     */
    @Override
    public PageInfo<InterfaceSuiteProcessorVO> findInterfaceSuiteProcessorList(InterfaceSuiteProcessorDTO interfaceSuiteProcessorDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(interfaceSuiteProcessorMapper.selectInterfaceSuiteProcessorList(interfaceSuiteProcessorDTO));
    }

    /**
     * 根据主键查询
     * @param id id
     * @return InterfaceSuiteProcessorVO
     */
    @Override
    public InterfaceSuiteProcessorVO findInterfaceSuiteProcessorById(Integer id) {
        return interfaceSuiteProcessorMapper.selectInterfaceSuiteProcessorById(id);
    }

    /**
     * 根据测试套件编号查询
     * @param suiteId suiteId
     * @return List<InterfaceSuiteProcessorVO>
     */
    @Override
    public List<InterfaceSuiteProcessorVO> findInterfaceSuiteProcessorBySuiteId(Integer suiteId) {
        return interfaceSuiteProcessorMapper.selectInterfaceSuiteProcessorBySuiteId(suiteId);
    }

    /**
     * 根据主键删除
     * @param id id
     */
    @Override
    public void removeInterfaceSuiteProcessorById(Integer id) {
        interfaceSuiteProcessorMapper.deleteInterfaceSuiteProcessorById(id);
    }

    /**
     * 根据测试套件编号删除
     * @param suiteId suiteId
     */
    @Override
    public void removeInterfaceSuiteProcessorBySuiteId(Integer suiteId) {
        interfaceSuiteProcessorMapper.deleteInterfaceSuiteProcessorBySuiteId(suiteId);
    }

    private void checkDO(InterfaceSuiteProcessorDO interfaceSuiteProcessorDO) throws ValidException {
        Integer suiteId = interfaceSuiteProcessorDO.getSuiteId();
        Byte processorType = interfaceSuiteProcessorDO.getProcessorType();
        Byte type = interfaceSuiteProcessorDO.getType();
        String value = interfaceSuiteProcessorDO.getValue();

        ValidUtil.notNUll(suiteId, "测试套件编号不能为空");

        ValidUtil.notNUll(processorType, "处理器类型不能为空");
        ValidUtil.size(processorType, 0, 1, "处理器类型参数错误");

        ValidUtil.notNUll(type, "类型不能为空");
        ValidUtil.size(type, 0, 3, "类型参数错误");

        ValidUtil.notNUll(value, "处理器值不能为空");
        ValidUtil.length(value, 200, "处理器值长度不能超过200");

        if (type == 1) {
            ValidUtil.isJsonObject(value, "headers syntax error");
        } else if (type == 2) {
            ValidUtil.isJsonObject(value, "params syntax error");
        } else if (type == 3) {
            ValidUtil.isJsonObject(value, "data syntax error");
        }
    }
}
