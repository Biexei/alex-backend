package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.StabilityCaseLogDO;
import org.alex.platform.pojo.StabilityCaseLogDTO;
import org.alex.platform.pojo.StabilityCaseLogVO;

public interface StabilityCaseLogService {
    Integer saveStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO);

    void modifyStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO);

    StabilityCaseLogVO findStabilityCaseLogById(Integer id);

    StabilityCaseLogVO findStabilityCaseLogByNo(String no);

    PageInfo<StabilityCaseLogVO> findStabilityCaseLogByStabilityCaseId(Integer caseId, Integer pageNum, Integer pageSize);

    PageInfo<StabilityCaseLogVO> findStabilityCaseLogList(StabilityCaseLogDTO stabilityCaseLogDTO, Integer pageNum, Integer pageSize);

    Integer countExecuting();

    Integer countExecutingByCaseId(Integer stabilityTestId);

    void removeStabilityCaseLogById(Integer id) throws BusinessException;

}
