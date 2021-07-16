package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.StabilityCaseDO;
import org.alex.platform.pojo.StabilityCaseDTO;
import org.alex.platform.pojo.StabilityCaseInfoVO;
import org.alex.platform.pojo.StabilityCaseListVO;

public interface StabilityCaseService {
    void saveStabilityCase(StabilityCaseDO stabilityCaseDO) throws ValidException;

    void modifyStabilityCase(StabilityCaseDO stabilityCaseDO) throws ValidException;

    StabilityCaseInfoVO findStabilityCaseById(Integer id);

    PageInfo<StabilityCaseListVO> findStabilityCaseList(StabilityCaseDTO stabilityCaseDTO, Integer pageNum, Integer pageSize);

    void removeStabilityCaseById(Integer id);

    void executeStabilityCaseById(Integer id, Integer executeId) throws BusinessException;

    void executable(Integer id) throws BusinessException;

    void stopStabilityCaseByLogId(Integer stabilityTestLogId, Integer executeId) throws BusinessException;

}
