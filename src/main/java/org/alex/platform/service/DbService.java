package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.DbDO;
import org.alex.platform.pojo.DbDTO;
import org.alex.platform.pojo.DbVO;

public interface DbService {
    void saveDb(DbDO dbDO) throws BusinessException;

    void modifyDb(DbDO dbDO) throws BusinessException;

    void removeDbById(Integer dbId) throws BusinessException;

    DbVO findDbById(Integer id);

    DbVO findDbByName(String name);

    PageInfo<DbVO> findDbList(DbDTO dbDTO, Integer pageNum, Integer pageSize);

    String dbConnectInfo(Integer dbId);
}
