package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceCaseRelyDataDO;
import org.alex.platform.pojo.InterfaceCaseRelyDataDTO;
import org.alex.platform.pojo.InterfaceCaseRelyDataVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceCaseRelyDataMapper {
    void insertIfRelyData(InterfaceCaseRelyDataDO ifRelyDataDO);

    void updateIfRelyData(InterfaceCaseRelyDataDO ifRelyDataDO);

    List<InterfaceCaseRelyDataVO> selectIfRelyDataList(InterfaceCaseRelyDataDTO ifRelyDataDTO);

    InterfaceCaseRelyDataVO selectIfRelyData(Integer relyId);

    void deleteIfRelyData(Integer relyId);
}
