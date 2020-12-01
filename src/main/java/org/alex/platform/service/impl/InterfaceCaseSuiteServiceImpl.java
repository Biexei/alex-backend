package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceCaseSuiteMapper;
import org.alex.platform.mapper.InterfaceSuiteCaseRefMapper;
import org.alex.platform.mapper.TaskMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.InterfaceCaseSuiteService;
import org.alex.platform.service.InterfaceSuiteProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class InterfaceCaseSuiteServiceImpl implements InterfaceCaseSuiteService {
    @Autowired
    InterfaceCaseSuiteMapper interfaceCaseSuiteMapper;
    @Autowired
    InterfaceSuiteCaseRefMapper interfaceSuiteCaseRefMapper;
    @Autowired
    InterfaceSuiteProcessorService interfaceSuiteProcessorService;
    @Autowired
    TaskMapper taskMapper;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceCaseSuiteServiceImpl.class);

    /**
     * 新增测试套件
     * @param interfaceCaseSuiteDO interfaceCaseSuiteDO
     * @return interfaceCaseSuiteDO
     */
    @Override
    public InterfaceCaseSuiteDO saveInterfaceCaseSuite(InterfaceCaseSuiteDO interfaceCaseSuiteDO) {
        interfaceCaseSuiteMapper.insertInterfaceCaseSuite(interfaceCaseSuiteDO);
        return interfaceCaseSuiteDO;
    }

    /**
     * 新增测试套件及其附表
     * @param interfaceSuiteInfoDTO interfaceSuiteInfoDTO
     */
    @Override
    public void saveInterfaceCaseSuiteAndProcessor(InterfaceSuiteInfoDTO interfaceSuiteInfoDTO) throws ValidException {
        // 插入主表
        InterfaceCaseSuiteDO interfaceCaseSuiteDO = this.saveInterfaceCaseSuite(interfaceSuiteInfoDTO);
        // 插入附表
        List<InterfaceSuiteProcessorDO> suiteProcessors = interfaceSuiteInfoDTO.getSuiteProcessors();
        Date date = new Date();
        if (suiteProcessors!=null && !suiteProcessors.isEmpty()) {
            for(InterfaceSuiteProcessorDO interfaceSuiteProcessorDO : suiteProcessors) {
                interfaceSuiteProcessorDO.setCreatedTime(date);
                interfaceSuiteProcessorDO.setUpdateTime(date);
                interfaceSuiteProcessorDO.setSuiteId(interfaceCaseSuiteDO.getSuiteId());
                interfaceSuiteProcessorService.saveInterfaceSuiteProcessor(interfaceSuiteProcessorDO);
            }
        }
    }

    /**
     * 修改测试套件
     *
     * @param interfaceSuiteInfoDTO interfaceSuiteInfoDTO
     */
    @Override
    public void modifyInterfaceCaseSuite(InterfaceSuiteInfoDTO interfaceSuiteInfoDTO) throws ValidException {
        // 修改主表
        interfaceCaseSuiteMapper.updateInterfaceCaseSuite(interfaceSuiteInfoDTO);
        // 由于前端不方便传主键ID，删除所有的后置处理器后重新新增
        Integer suiteId = interfaceSuiteInfoDTO.getSuiteId();
        interfaceSuiteProcessorService.removeInterfaceSuiteProcessorBySuiteId(suiteId);
        // 新增测试套件后置处理器
        Date date = new Date();
        List<InterfaceSuiteProcessorDO> suiteProcessors = interfaceSuiteInfoDTO.getSuiteProcessors();
        if (suiteProcessors != null && !suiteProcessors.isEmpty()) {
            for (InterfaceSuiteProcessorDO interfaceSuiteProcessorDO : suiteProcessors) {
                interfaceSuiteProcessorDO.setSuiteId(suiteId);
                interfaceSuiteProcessorDO.setCreatedTime(date);
                interfaceSuiteProcessorDO.setUpdateTime(date);
                interfaceSuiteProcessorService.saveInterfaceSuiteProcessor(interfaceSuiteProcessorDO);
            }
        }
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
            LOG.warn("该测试套件下已存在用例, suiteId={}", suiteId);
            throw new BusinessException("请先删除该测试套件下的用例");
        }
        // 存在定时任务的不准删除
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setSuiteId(suiteId);
        if (!taskMapper.selectTaskList(taskDTO).isEmpty()) {
            LOG.warn("存在关于该测试套件的定时任务, suiteId={}", suiteId);
            throw new BusinessException("存在关于该测试套件的定时任务");
        }
        // 删除主表
        interfaceCaseSuiteMapper.deleteInterfaceCaseSuiteById(suiteId);
        // 删除处理器附表
        interfaceSuiteProcessorService.removeInterfaceSuiteProcessorBySuiteId(suiteId);
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
     * 获取测试套件详情，包括处理器
     * @param suiteId suiteId
     * @return InterfaceSuiteInfoVO
     */
    @Override
    public InterfaceSuiteInfoVO findInterfaceCaseSuiteInfoById(Integer suiteId) {
        return interfaceCaseSuiteMapper.selectInterfaceCaseSuiteInfoById(suiteId);
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

    /**
     * 获取测试套件列表 不分页
     * @param interfaceCaseSuiteDTO interfaceCaseSuiteDTO
     * @return List<InterfaceCaseSuiteVO>
     */
    @Override
    public List<InterfaceCaseSuiteVO> findInterfaceCaseSuiteAll(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO) {
        return interfaceCaseSuiteMapper.selectInterfaceCaseSuite(interfaceCaseSuiteDTO);
    }

    /**
     * 复制测试套件
     *
     * @param suiteId 被复制的测试套件编号
     * @param creator 创建人
     * @return 复制测试用例个数
     */
    @Override
    public HashMap<String, Integer> copyInterfaceCaseSuiteById(Integer suiteId, String creator) {
        Integer copyCaseCount = 0;
        // 1.获取被复制测试套件信息
        InterfaceCaseSuiteVO copiedSuite = this.findInterfaceCaseSuiteById(suiteId);
        // 2.获取被测试套件所有的测试用例
        List<InterfaceSuiteCaseRefDO> copiedSuiteCaseList = interfaceSuiteCaseRefMapper.selectSuiteAllCase(suiteId);
        // 3.新增测试套件，并获取自增主键
        InterfaceCaseSuiteDO suiteDO = new InterfaceCaseSuiteDO();
        suiteDO.setSuiteName(copiedSuite.getSuiteName());
        suiteDO.setDesc(copiedSuite.getDesc());
        Date date = new Date();
        suiteDO.setCreatedTime(date);
        suiteDO.setUpdateTime(date);
        suiteDO.setCreator(creator);
        suiteDO.setExecuteType(copiedSuite.getExecuteType());
        suiteDO.setRunDev(copiedSuite.getRunDev());
        InterfaceCaseSuiteDO incrementSuiteDO = this.saveInterfaceCaseSuite(suiteDO);
        Integer incrementSuiteId = incrementSuiteDO.getSuiteId();
        // 4.往新增测试套件添加用例
        List<InterfaceSuiteCaseRefDO> toInsertSuiteCase = new LinkedList<>();
        for(InterfaceSuiteCaseRefDO suiteCase : copiedSuiteCaseList) {
            suiteCase.setSuiteId(incrementSuiteId);
            toInsertSuiteCase.add(suiteCase);
            copyCaseCount ++;
        }
        if (!toInsertSuiteCase.isEmpty()) {
            interfaceSuiteCaseRefMapper.insertSuiteCase(toInsertSuiteCase);
        }
        HashMap<String, Integer> map = new HashMap<>();
        map.put("copyCaseCount", copyCaseCount);
        return map;
    }
}
