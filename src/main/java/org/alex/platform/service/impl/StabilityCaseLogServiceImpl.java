package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.StabilityCaseLogMapper;
import org.alex.platform.pojo.StabilityCaseLogDO;
import org.alex.platform.pojo.StabilityCaseLogDTO;
import org.alex.platform.pojo.StabilityCaseLogVO;
import org.alex.platform.service.StabilityCaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StabilityCaseLogServiceImpl implements StabilityCaseLogService {

    @Autowired
    StabilityCaseLogMapper stabilityCaseLogMapper;

    @Override
    public Integer saveStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO) {
        return stabilityCaseLogMapper.insertStabilityCaseLog(stabilityCaseLogDO);
    }

    @Override
    public void modifyStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO) {
        stabilityCaseLogMapper.updateStabilityCaseLog(stabilityCaseLogDO);
    }

    @Override
    public StabilityCaseLogVO findStabilityCaseLogById(Integer id) {
        return stabilityCaseLogMapper.selectStabilityCaseLogById(id);
    }

    @Override
    public StabilityCaseLogVO findStabilityCaseLogByNo(String no) {
        return stabilityCaseLogMapper.selectStabilityCaseLogByNo(no);
    }

    @Override
    public PageInfo<StabilityCaseLogVO> findStabilityCaseLogByStabilityCaseId(Integer caseId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(stabilityCaseLogMapper.selectStabilityCaseLogByStabilityCaseId(caseId));
    }

    @Override
    public PageInfo<StabilityCaseLogVO> findStabilityCaseLogList(StabilityCaseLogDTO stabilityCaseLogDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(stabilityCaseLogMapper.selectStabilityCaseLogList(stabilityCaseLogDTO));
    }

    @Override
    public Integer countExecuting() {
        return stabilityCaseLogMapper.countExecuting();
    }

    @Override
    public Integer countExecutingByCaseId(Integer stabilityTestId) {
        return stabilityCaseLogMapper.countExecutingByCaseId(stabilityTestId);
    }
}
