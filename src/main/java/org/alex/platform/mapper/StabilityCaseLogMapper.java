package org.alex.platform.mapper;

import org.alex.platform.pojo.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StabilityCaseLogMapper {
    Integer insertStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO);

    void updateStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO);

    StabilityCaseLogVO selectStabilityCaseLogById(Integer id);

    StabilityCaseLogVO selectStabilityCaseLogByNo(String no);

    List<StabilityCaseLogVO> selectStabilityCaseLogByStabilityCaseId(Integer caseId);

    List<StabilityCaseLogVO> selectStabilityCaseLogList(StabilityCaseLogDTO stabilityCaseLogDTO);

    Integer countExecuting();

    Integer countExecutingByCaseId(Integer stabilityTestId);
}
