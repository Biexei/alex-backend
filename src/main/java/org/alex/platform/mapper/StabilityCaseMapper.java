package org.alex.platform.mapper;

import org.alex.platform.pojo.StabilityCaseDO;
import org.alex.platform.pojo.StabilityCaseDTO;
import org.alex.platform.pojo.StabilityCaseInfoVO;
import org.alex.platform.pojo.StabilityCaseListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StabilityCaseMapper {
    void insertStabilityCase(StabilityCaseDO stabilityCaseDO);

    void updateStabilityCase(StabilityCaseDO stabilityCaseDO);

    StabilityCaseInfoVO selectStabilityCaseById(Integer id);

    List<StabilityCaseListVO> selectStabilityCaseList(StabilityCaseDTO stabilityCaseDTO);

    void deleteStabilityCaseById(Integer id);

    void updateStabilityCaseLastExecuteTime(@Param("stabilityTestId") Integer stabilityTestId, @Param("lastExecuteTime") Date lastExecuteTime);
}
