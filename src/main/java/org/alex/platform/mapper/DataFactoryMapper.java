package org.alex.platform.mapper;

import org.alex.platform.pojo.DataFactoryDO;
import org.alex.platform.pojo.DataFactoryDTO;
import org.alex.platform.pojo.DataFactoryVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataFactoryMapper {
    void insertDataFactory(DataFactoryDO dataFactoryDO);

    void updateDataFactory(DataFactoryDO dataFactoryDO);

    void deleteDataFactoryById(Integer id);

    DataFactoryVO selectDataFactoryById(Integer id);

    List<DataFactoryVO> selectDataFactoryList(DataFactoryDTO dataFactoryDTO);
}
