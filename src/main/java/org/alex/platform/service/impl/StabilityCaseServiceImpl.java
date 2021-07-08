package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.StabilityCaseMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.StabilityCaseService;
import org.alex.platform.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StabilityCaseServiceImpl implements StabilityCaseService {

    @Autowired
    StabilityCaseMapper stabilityCaseMapper;
    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;

    /**
     * 新增稳定性测试用例
     * @param stabilityCaseDO stabilityCaseDO
     * @throws ValidException ValidException
     */
    @Override
    public void saveStabilityCase(StabilityCaseDO stabilityCaseDO) throws ValidException {
        Date date = new Date();
        stabilityCaseDO.setCreatedTime(date);
        stabilityCaseDO.setUpdateTime(date);
        Byte executeType = stabilityCaseDO.getExecuteType(); // 调度方式0执行总次数   1截止至指定时间
        Byte protocol = stabilityCaseDO.getProtocol(); // 0http(s)1ws(s)2dubbo
        Integer caseId = stabilityCaseDO.getCaseId();
        if (executeType == 0) {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteTimes(), "请输入执行次数");
            stabilityCaseDO.setExecuteEndTime(null);
        } else {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteEndTime(), "请选择任务终止时间");
            stabilityCaseDO.setExecuteTimes(null);
        }
        if (protocol == 0) {
            ValidUtil.notNUll(interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId), "接口测试用例编号不存在");
        }
        stabilityCaseMapper.insertStabilityCase(stabilityCaseDO);
    }

    /**
     * 修改稳定性测试用例
     * @param stabilityCaseDO stabilityCaseDO
     * @throws ValidException ValidException
     */
    @Override
    public void modifyStabilityCase(StabilityCaseDO stabilityCaseDO) throws ValidException {
        stabilityCaseDO.setUpdateTime(new Date());
        Byte executeType = stabilityCaseDO.getExecuteType(); // 调度方式0执行总次数   1截止至指定时间
        Byte protocol = stabilityCaseDO.getProtocol(); // 0http(s)1ws(s)2dubbo
        Integer caseId = stabilityCaseDO.getCaseId();
        if (executeType == 0) {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteTimes(), "请输入执行次数");
            stabilityCaseDO.setExecuteEndTime(null);
        } else {
            ValidUtil.notNUll(stabilityCaseDO.getExecuteEndTime(), "请选择任务终止时间");
            stabilityCaseDO.setExecuteTimes(null);
        }
        if (protocol == 0) {
            ValidUtil.notNUll(interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId), "接口测试用例编号不存在");
        }
        stabilityCaseMapper.updateStabilityCase(stabilityCaseDO);
    }

    /**
     * 查看稳定性测试用例详情
     * @param id id
     * @return StabilityCaseListVO
     */
    @Override
    public StabilityCaseInfoVO findStabilityCaseById(Integer id) {
        StabilityCaseInfoVO stabilityCaseInfoVO = stabilityCaseMapper.selectStabilityCaseById(id);
        Byte protocol = stabilityCaseInfoVO.getProtocol();
        Integer caseId = stabilityCaseInfoVO.getCaseId();
        if (protocol == 0) { //http(s)
            InterfaceCaseInfoVO interfaceCaseInfoVO = interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId);
            if (interfaceCaseInfoVO != null) {
                stabilityCaseInfoVO.setCaseDesc(interfaceCaseInfoVO.getDesc());
            }
        }
        return stabilityCaseInfoVO;
    }

    /**
     * 查看稳定性测试用例列表
     * @param stabilityCaseDTO stabilityCaseDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo<StabilityCaseListVO>
     */
    @Override
    public PageInfo<StabilityCaseListVO> findStabilityCaseList(StabilityCaseDTO stabilityCaseDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(stabilityCaseMapper.selectStabilityCaseList(stabilityCaseDTO));
    }

    /**
     * 删除稳定性测试用例
     * @param id id
     */
    @Override
    public void removeStabilityCaseById(Integer id) {
        stabilityCaseMapper.deleteStabilityCaseById(id);
    }
}
