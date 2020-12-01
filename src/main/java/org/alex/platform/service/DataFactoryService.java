package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.DataFactoryDO;
import org.alex.platform.pojo.DataFactoryDTO;
import org.alex.platform.pojo.DataFactoryVO;


public interface DataFactoryService {
    void saveDataFactory(DataFactoryDO dataFactoryDO) throws ValidException;

    void modifyDataFactory(DataFactoryDO dataFactoryDO) throws ValidException;

    void removeDataFactoryById(Integer id);

    DataFactoryVO findDataFactoryById(Integer id);

    PageInfo<DataFactoryVO> findDataFactoryList(DataFactoryDTO dataFactoryDTO, Integer pageNum, Integer pageSize);

    long executeDataFactory(Integer id, String executor) throws BusinessException;
}
