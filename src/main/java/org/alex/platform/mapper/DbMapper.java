package org.alex.platform.mapper;

import org.alex.platform.pojo.DbDO;
import org.alex.platform.pojo.DbDTO;
import org.alex.platform.pojo.DbVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbMapper {
    void insertDb(DbDO dbDO);

    void updateDb(DbDO dbDO);

    void deleteDbById(Integer dbId);

    DbVO selectDbById(Integer id);

    DbVO selectDbByName(String name);

    List<DbVO> selectDbList(DbDTO dbDTO);

    List<DbVO> checkDbName(DbDO dbDO);
}
