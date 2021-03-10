package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.UserDO;
import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.UserVO;


public interface UserService {
    PageInfo<UserVO> findUserList(UserDO userDO, Integer pageNum, Integer pageSize);

    void modifyUser(UserDO userDO) throws BusinessException;

    void saveUser(UserDO userDO) throws BusinessException;

    void registerUser(UserDO userDO) throws BusinessException;

    UserDO findUserToLogin(UserDO userDO);

    UserVO findUserById(Integer userId);

    void removeUserById(Integer userId) throws BusinessException;
}
