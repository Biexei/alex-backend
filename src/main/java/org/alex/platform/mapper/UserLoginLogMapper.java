package org.alex.platform.mapper;

import org.alex.platform.pojo.UserLoginLogDO;
import org.alex.platform.pojo.UserLoginLogDTO;
import org.alex.platform.pojo.UserLoginLogVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginLogMapper {
    List<UserLoginLogVO> selectUserLoginLogList(UserLoginLogDTO userLoginLogDTO);

    void insertUserLoginLog(UserLoginLogDO userLoginLogDO);
}
