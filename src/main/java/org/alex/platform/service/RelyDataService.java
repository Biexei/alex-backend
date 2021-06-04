package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.RelyDataDO;
import org.alex.platform.pojo.RelyDataDTO;
import org.alex.platform.pojo.RelyDataVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RelyDataService {
    void saveRelyData(RelyDataDO relyDataDO) throws BusinessException;

    void modifyRelyData(RelyDataDO relyDataDO, HttpServletRequest request) throws BusinessException;

    RelyDataVO findRelyDataById(Integer id);

    RelyDataVO findRelyDataByName(String name);

    PageInfo<RelyDataVO> findRelyDataList(RelyDataDTO relyDataDTO, Integer pageNum, Integer pageSize);

    void removeRelyDataById(Integer id, HttpServletRequest request) throws BusinessException;
}
