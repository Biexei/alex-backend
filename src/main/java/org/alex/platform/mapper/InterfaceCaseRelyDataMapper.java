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

    List<InterfaceCaseRelyDataVO> checkRelyName(InterfaceCaseRelyDataDO ifRelyDataDO);

    InterfaceCaseRelyDataVO selectIfRelyDataById(Integer relyId);

    InterfaceCaseRelyDataVO selectIfRelyDataByName(String relyName);

    void deleteIfRelyData(Integer relyId);
}
