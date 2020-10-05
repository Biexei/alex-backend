package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.InterfaceCaseSuiteMapper;
import org.alex.platform.mapper.InterfaceSuiteCaseRefMapper;
import org.alex.platform.mapper.TaskMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceCaseSuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterfaceCaseSuiteServiceImpl implements InterfaceCaseSuiteService {
    @Autowired
    InterfaceCaseSuiteMapper interfaceCaseSuiteMapper;
    @Autowired
    InterfaceSuiteCaseRefMapper interfaceSuiteCaseRefMapper;
    @Autowired
    TaskMapper taskMapper;

    /**
     * 新增测试套件
     *
     * @param interfaceCaseSuiteDO interfaceCaseSuiteDO
     */
    @Override
    public void saveInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO) {
        interfaceCaseSuiteMapper.insertInterfaceCaseSuite(interfaceCaseSuiteDO);
    }

    /**
     * 修改测试套件
     *
     * @param interfaceCaseSuiteDO interfaceCaseSuiteDO
     */
    @Override
    public void modifyInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO) {
        interfaceCaseSuiteMapper.updateInterfaceCaseSuite(interfaceCaseSuiteDO);
    }

    /**
     * 删除测试套件
     *
     * @param suiteId 测试套件编号
     */
    @Override
    public void removeInterfaceCaseSuiteById(Integer suiteId) throws BusinessException {
        // 存在测试用例的不准删除
        InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO = new InterfaceSuiteCaseRefDTO();
        interfaceSuiteCaseRefDTO.setSuiteId(suiteId);
        if (!interfaceSuiteCaseRefMapper.selectSuiteCaseList(interfaceSuiteCaseRefDTO).isEmpty()) {
            throw new BusinessException("请先删除该测试套件下的用例");
        }
        // 存在定时任务的不准删除
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setSuiteId(suiteId);
        if (!taskMapper.selectTaskList(taskDTO).isEmpty()) {
            throw new BusinessException("存在关于该测试套件的定时任务");
        }
        interfaceCaseSuiteMapper.deleteInterfaceCaseSuiteById(suiteId);
    }

    /**
     * 获取测试套件详情
     *
     * @param suiteId 测试套件编号
     * @return InterfaceCaseSuiteVO
     */
    @Override
    public InterfaceCaseSuiteVO findInterfaceCaseSuiteById(Integer suiteId) {
        return interfaceCaseSuiteMapper.selectInterfaceCaseSuiteById(suiteId);
    }

    /**
     * 获取测试套件列表
     *
     * @param interfaceCaseSuiteDTO interfaceCaseSuiteDTO
     * @param pageNum               pageNum
     * @param pageSize              pageSize
     * @return PageInfo<InterfaceCaseSuiteVO>
     */
    @Override
    public PageInfo<InterfaceCaseSuiteVO> findInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO,
                                                                 Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(interfaceCaseSuiteMapper.selectInterfaceCaseSuite(interfaceCaseSuiteDTO));
    }
}
