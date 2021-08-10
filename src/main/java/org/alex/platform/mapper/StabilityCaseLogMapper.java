package org.alex.platform.mapper;

import org.alex.platform.pojo.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StabilityCaseLogMapper {
    Integer insertStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO);

    void updateStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO);

    StabilityCaseLogInfoVO selectStabilityCaseLogById(Integer id);

    StabilityCaseLogInfoVO selectStabilityCaseLogByNo(String no);

    List<StabilityCaseLogListVO> selectStabilityCaseLogByStabilityCaseId(Integer caseId);

    List<StabilityCaseLogListVO> selectStabilityCaseLogList(StabilityCaseLogDTO stabilityCaseLogDTO);

    Integer countExecuting();

    Integer countExecutingByCaseId(Integer stabilityTestId);

    void deleteStabilityCaseLogById(Integer id);
}
