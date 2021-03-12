package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.UserDO;
import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.UserVO;

import javax.servlet.http.HttpServletRequest;


public interface UserService {
    PageInfo<UserVO> findUserList(UserDO userDO, Integer pageNum, Integer pageSize);

    void modifyUser(UserDO userDO) throws BusinessException;

    void saveUser(UserDO userDO) throws BusinessException;

    void registerUser(UserDO userDO) throws BusinessException;

    UserDO findUserToLogin(UserDO userDO);

    UserVO findUserById(Integer userId);

    void removeUserById(HttpServletRequest request, Integer userId) throws BusinessException;

    String findPwdByUserId(Integer userId);

    void changePwd(HttpServletRequest request, String oldPwd, String newPwd) throws BusinessException;

    void pwdReset(Integer userId, String password) throws BusinessException;
}
