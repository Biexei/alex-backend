package org.alex.platform.mapper;

import org.alex.platform.pojo.RelyDataDO;
import org.alex.platform.pojo.RelyDataDTO;
import org.alex.platform.pojo.RelyDataVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelyDataMapper {
    void insertRelyData(RelyDataDO relyDataDO);

    void updateRelyData(RelyDataDO relyDataDO);

    RelyDataVO selectRelyDataById(Integer id);

    RelyDataVO selectRelyDataByName(String name);

    List<RelyDataVO> selectRelyDataList(RelyDataDTO relyDataDTO);

    List<RelyDataVO> checkName(RelyDataDO relyDataDO);

    void deleteRelyDataById(Integer id);
}
