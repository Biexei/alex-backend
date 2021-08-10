package org.alex.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.StabilityCaseLogDO;
import org.alex.platform.pojo.StabilityCaseLogDTO;
import org.alex.platform.pojo.StabilityCaseLogInfoVO;
import org.alex.platform.pojo.StabilityCaseLogListVO;

public interface StabilityCaseLogService {
    Integer saveStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO);

    void modifyStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO);

    StabilityCaseLogInfoVO findStabilityCaseLogById(Integer id);

    StabilityCaseLogInfoVO findStabilityCaseLogByNo(String no);

    PageInfo<StabilityCaseLogListVO> findStabilityCaseLogByStabilityCaseId(Integer caseId, Integer pageNum, Integer pageSize);

    PageInfo<StabilityCaseLogListVO> findStabilityCaseLogList(StabilityCaseLogDTO stabilityCaseLogDTO, Integer pageNum, Integer pageSize);

    Integer countExecuting();

    Integer countExecutingByCaseId(Integer stabilityTestId);

    void removeStabilityCaseLogById(Integer id) throws BusinessException;

    JSONArray chartResponseTime(Integer stabilityTestLogId);

}
